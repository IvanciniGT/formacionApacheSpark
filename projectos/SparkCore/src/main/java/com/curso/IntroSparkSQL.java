package com.curso;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.udf;

public class IntroSparkSQL {

    public static void main(String[] args){
        // PASO 1: Abrir la conexión con el cluster de Spark
        SparkSession conexion = SparkSession.builder()
                .appName("Introducción a Spark SQL")
                //.master("local[2]")
                .getOrCreate();

        // PASO 2: Hacernos con los datos, en este caso no usaremos los RDDs, sino los Datasets
            // Un dataset es como un RDD, donde lo que tengo almacenado son FILAS estructuradas de datos (en columnas)
            // Es el equivalente a una tabla de una BBDD relacional.
            // Y en lo objetos Dataset no tendremos las funciones típicas de MapReduce...
            // sino funciones de más alto nivel, equivalentes a las que existen en el lenguaje SQL
            // Al final, la librería SQL se basa en la librería CORE... y todas las operaciones que define se
            // implementan intermente con operaciones MAP REDUCE Básicas.
        Dataset<Row> datos = conexion.read()
                //.readStream() similar al read... pero que mantendrá leyendo del fichero indefinidamente.
                // Lo estará monitorizando, y cada vez que detecte un cambio, lo procesará
                .json("src/main/resources/personas.json");

            // Asociado a un Dataset, siempre hay un Schema (esquema) que define la estructura de los datos:
            // Qué columnas tiene, qué tipo de datos tiene cada columna, si la columna puede tomar valor null o no.
            // Habituialmente, Ese squema se crea automáticamente por Spark SQL.
            // En ocasiones el que me crea no me es válido... o quiero cambiarlo... o quiero crear el mio propio (no es habituial, pero puede ocurrir)
        datos.printSchema();


        // PASO 3: Tratamiento de los datos (sin MAP REDUCE)... mediante algo parecido a SQL
        Dataset<Row> resultado = datos;

        // PASO 4: Hacer lo que necesite con el resultado
        resultado.show();

        // Ejemplos:
        // SELECT [CAMPOS] FROM [TABLA] WHERE [CONDICIONES]
        datos.select("nombre", "edad").show();
        // Trabajando con nombre, accedo al dato tal cual viene en la tabla
        datos.select(col("nombre"), col("edad")).show();
        // Trabajando con columnas, puedo hacer modificaciones sobre ese dato
        datos.select(col("nombre"), col("edad").plus(1)).show();
        // WHERE: LA ESCRIBIMOS MEDIANTE FILTROS, IGUAL QUE HACÍAMOS CON RDD O STREAMS... PERO SIN PROGRAMACIÓN FUNCIONAL
        datos.select(col("nombre"), col("edad")).filter(col("edad").gt(30)).show();
        // Podemos hacer básicamente todas las cosas que hacemos con SQL:
        datos.groupBy("nombre").sum("edad").orderBy(col("sum(edad)").desc()).show();

        datos.createOrReplaceTempView("personas");
        conexion.sql("SELECT nombre, edad FROM personas WHERE edad > 30").show();
        conexion.sql("SELECT nombre, sum(edad) FROM personas GROUP BY nombre ORDER BY sum(edad) DESC").show();
        conexion.sql("SELECT nombre, sum(edad) FROM personas GROUP BY nombre ORDER BY sum(edad) DESC")
                .repartition(5)
                .write()
                .mode("overwrite")
                .json("src/main/resources/resultado.json");

        // En SparkSQL los datos del fichero Personas.json se están leyendo en un Dataset<Row>
        // Si estuvieramos trabajando con SparkCore... que me interesaría tener? JavaRDD<Persona>
        // Y qué es una persona? Vamos a definirlo: CLASE Persona

        // Pasar de un Dataset<Rows> a un JavaRDD<Persona>
        JavaRDD<Persona> personasRDD = datos.toJavaRDD() // JavaRDD<Row>
                .map( row -> new Persona(
                        row.getString(row.fieldIndex("nombre")),
                        row.getString(row.fieldIndex("apellidos")),
                        row.getLong(row.fieldIndex("edad")),
                        row.getString(row.fieldIndex("cp")),
                        row.getString(row.fieldIndex("email")),
                        row.getString(row.fieldIndex("dni")))
                );
        JavaRDD<Persona> dnisValidos = personasRDD.filter(Persona::isDniValido);
        dnisValidos = dnisValidos.map( persona -> {
            persona.setDni(persona.normalizarDni(true, true, "-").orElseThrow());
            return persona;
        });

        // Aplicar el filtro sobre un Dataset<Row>
        // Es SparkSQL existe el concepto de los UDF (User Defined Functions)
        // Un UDF es una función que yo defino, y que puedo usar en mis consultas SQL
        // Para ello, he de registrarla en el contexto de SparkSQL

        var miFuncionValidacion = udf( (String dni) -> Persona.validarDNI(dni), DataTypes.BooleanType);
        conexion.udf().register("esUnDniValido", miFuncionValidacion);
                                /// ^ Es el nombre que usaré en SQL
        conexion.sql("SELECT nombre, dni FROM personas WHERE esUnDniValido(dni)").show();


        // Pasar de un JavaRDD<Persona> a un Dataset<Row>
        Dataset<Row> filtradasComoDataset = conexion.createDataFrame(dnisValidos, Persona.class);
        filtradasComoDataset.show();

        // Hay operaciones que me va a interasr hacerlas sobre un Dataset<Row> y otras sobre un JavaRDD<Persona>

        // PASO 5: cerrar la conexión
        conexion.close();
    }

}
