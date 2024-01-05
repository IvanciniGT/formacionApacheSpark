package com.curso;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.udf;

public class ProcesarFicheroPersonas {

    private static final boolean guardarEnDisco = true;

    public static void main(String[] args){
        // PASO 1: Abrir la conexión con el cluster de Spark
        SparkSession conexion = SparkSession.builder()
                .appName("Procesar fichero Personas")
                //.master("local[2]")
                .getOrCreate();

        // PASO 2: Hacernos con los datos, en este caso no usaremos los RDDs, sino los Datasets
        Dataset<Row> datosPersonas = conexion.read()
                .json("src/main/resources/personas.json");
        Dataset<Row> datosCps = conexion.read()
                .option("header", "true")
                .option("sep", ",")
                .csv("src/main/resources/cps.csv");

        // PASO 3: Procesar los datos
        var miFuncionValidacion = udf( (String dni) -> Persona.validarDNI(dni), DataTypes.BooleanType);
        conexion.udf().register("esUnDniValido", miFuncionValidacion);
                                /// ^ Es el nombre que usaré en SQL

        // Registrar la tabla para poder usarla con sintaxis SQL
        datosPersonas.createOrReplaceTempView("personas");

        var personasConDNIValido = conexion.sql("SELECT * FROM personas WHERE esUnDniValido(dni)");
        var personasConDNIInvalido = conexion.sql("SELECT * FROM personas WHERE not esUnDniValido(dni)");

        var mayoresDeEdadConDNIValido = personasConDNIValido.filter(col("edad").geq(18));
        var menoresDeEdadConDNIValido = personasConDNIValido.filter(col("edad").lt(18));

        var datosFinales = mayoresDeEdadConDNIValido.join(datosCps, "cp");
        var cpNoDetectados = mayoresDeEdadConDNIValido.select("nombre","apellidos","edad","dni","cp","email")
                                  .except(datosFinales.select("nombre","apellidos","edad","dni","cp","email"));

        // PASO 4: Mostrar los resultados
        guardarDatos(personasConDNIInvalido, "src/main/resources/personasConDNIInvalido");
        guardarDatos(menoresDeEdadConDNIValido, "src/main/resources/menoresDeEdadConDNIValido");
        guardarDatos(datosFinales, "src/main/resources/datosFinales");
        guardarDatos(cpNoDetectados, "src/main/resources/cpNoDetectados");

        // PASO 5: cerrar la conexión
        conexion.close();
    }
    private static void guardarDatos(Dataset<Row> datos, String ruta) {
        System.out.println("Guardando datos en " + ruta);
        if(guardarEnDisco) {
            datos.write()
                    .mode("overwrite")
                    .parquet(ruta);
        }else{
            datos.show();
        }
    }
}
