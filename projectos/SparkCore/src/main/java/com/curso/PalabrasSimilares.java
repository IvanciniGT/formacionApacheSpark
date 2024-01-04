package com.curso;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PalabrasSimilares {

    private static final int DISTANCIA_MAXIMA_PERMITIDA      = 2;
    private static final int MAXIMO_DE_PALABRAS_A_DEVOLVER   = 10;

    public static void main(String[] args) {

        // PASO 1: Abrir conexión con Spark
        final SparkConf configuracionDeLaConexion = new SparkConf();
        configuracionDeLaConexion.setAppName("Palabras similares");
        configuracionDeLaConexion.setMaster("local[2]");
        JavaSparkContext conexion = new JavaSparkContext(configuracionDeLaConexion);

        // PASO 2: Convertir mis datos en un objeto de tipo RDD (el equivalente en Spark a los Stream de Java
        final List<String> diccionario = getPalabrasDelDiccionario();
        if(args.length == 0) {
            System.out.println("Debe introducir una palabra");
            System.exit(1);
        }

        // PASO 3: Aplicar el Map-Reduce
        final String palabraObjetivo = args[0].toLowerCase();
        JavaRDD<String> palabras = conexion.parallelize(diccionario, 50);

        List<String> palabrasSimilares = palabras
                .filter(  palabra -> Math.abs(palabra.length() - palabraObjetivo.length()) <= DISTANCIA_MAXIMA_PERMITIDA)   // Me quedo con palabras de similar longitud
                .map(     palabra -> new PalabraPuntuada(palabra, distanciaLevenshtein(palabra, palabraObjetivo)))          // Calculo la distancia de Levenshtein
                .filter(  palabraPuntuada -> palabraPuntuada.puntuacion <= DISTANCIA_MAXIMA_PERMITIDA)                      // Me quedo con las palabras similares a la objetivo
                .sortBy(  palabraPuntuada -> palabraPuntuada.puntuacion, true, 1)                     // Ordeno por distancia
                .map(     palabraPuntuada -> palabraPuntuada.palabra )                                                      // Descarto la puntuación
                .take(    MAXIMO_DE_PALABRAS_A_DEVOLVER );                                                                  // Recojo en una lista las primeras N sugerencias

        // PASO 4: Procesar el resultado: (guardarlo en una BBDD, fichero..., imprimirlo por pantalla)
        System.out.println("Palabras similares a: "+ palabraObjetivo);
        palabrasSimilares.forEach(System.out::println);

        // PASO 5: Cerrar conexión con el cluster
        conexion.close();
    }

    private static List<String> getPalabrasDelDiccionario() {
        //return List.of("manzana", "manzano", "mañana", "melocotón");
        // En Java 11, dentro de la clase Files, añadireon POR FIN!!!, ALELUYA!!!,
        // un método que nos permite leer un fichero de texto
        try{
            URL recurso=PalabrasSimilares.class.getClassLoader().getResource("diccionario.ES.txt");
            if(recurso == null) {
                System.out.println("Diccionario no encontrado");
                System.exit(1);
            }
            return Files.readString(Path.of(recurso.toURI()))
                    .lines()
                    .filter( linea -> ! linea.isBlank()) // ME quedo con la linea si tiene algo
                    .filter( linea -> linea.contains("=")) // Me quedo con la linea si contiene un =
                    .map(    linea -> linea.split("=")[0].toLowerCase())
                    .toList();
        }catch(IOException io){
            System.out.println("Error al leer el fichero");
            io.printStackTrace();
            System.exit(1);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static class PalabraPuntuada implements Serializable {
        public String palabra;
        public int puntuacion;

        public PalabraPuntuada(String palabra, int puntuacion) {
            this.palabra = palabra;
            this.puntuacion = puntuacion;
        }
    }

    private static int distanciaLevenshtein(String a, String b) {
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
}