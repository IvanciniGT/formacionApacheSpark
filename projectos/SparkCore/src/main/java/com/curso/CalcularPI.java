package com.curso;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.rdd.RDD;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CalcularPI {

    public static void main(String[] args) {

        // PASO 1: Abrir una conexión con el cluster de Spark
        // El objeto JavaSparkContext es el que refleja una conexión con el cluster
        // Para crear un objeto de este tipo, necesitamos suministra un objeto del tipo: SparkConf,
        // que incluye configuración de la conexión al cluster
        final SparkConf configuracionDeLaConexion = new SparkConf();
        configuracionDeLaConexion.setAppName("Calculador de PI");  // Identifica mi aplicación dentro de un cluster de Spark
        configuracionDeLaConexion.setMaster("local[2]"); // A qué cluster nos conectamos
                                                    // Aqui pondremos la dirección de un cluster... habitualmente de la forma: "spark://IP:7077"
                                                    // TRUCO: En desarrollo, si ponemos "local" Spark crea un cluster en local para pruebas,
                                                    // Que arranca cada vez que ejecuto mi programa, automáticamente
        JavaSparkContext conexion = new JavaSparkContext(configuracionDeLaConexion);


        // PASO 2: Convertir mis datos en un objeto de tipo RDD (el equivalente en Spark a los Stream de Java
        int N = 100*1000*1000;
        // Los RDDs se generan siempre desde la conexión al cluster.
        List<Integer> lanzamientos = List.of(N);
        JavaRDD<Integer> lanzamientosRDD = conexion.parallelize(lanzamientos);

        /*
            Coleccion inicial
                1000   ->  0,1,2,...,999 ->     0                             ----> n= numeroDeDardosDentro
                                                1
                                                2
                                                ...
                                                999
         */

        // PASO 3: Aplicar el Map-Reduce
        long n = lanzamientosRDD.flatMap( numeroDeLanzamientos -> IntStream.range(0,N).iterator())
                .map( numeroDeTirada -> new double[] {Math.random(), Math.random()} )           // Calculo el punto de impacto
                .map( posiciones -> posiciones[0]*posiciones[0] + posiciones[1]*posiciones[1] ) // Mirar la distancia al origen
                .filter( distancia -> distancia <=1 )                                           // Quedarme con los que están en el círculo
                .count();                                                                       // Contar cuántos hay

        // PASO 4: Procesar el resultado: (guardarlo en una BBDD, fichero..., imprimirlo por pantalla)
        double pi = 4.0 * n / N;
        System.out.println("El valor de pi es: " + pi);

        // PASO 5: Cerrar conexión con el cluster
        conexion.close();
    }

}