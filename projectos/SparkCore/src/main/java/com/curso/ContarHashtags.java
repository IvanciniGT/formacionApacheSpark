package com.curso;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ContarHashtags {

    private static final int DISTANCIA_MAXIMA_PERMITIDA      = 2;
    private static final int MAXIMO_DE_PALABRAS_A_DEVOLVER   = 10;

    public static void main(String[] args) {

        // PASO 1: Abrir conexión con Spark
        final SparkConf configuracionDeLaConexion = new SparkConf();
        configuracionDeLaConexion.setAppName("Palabras similares");
        //configuracionDeLaConexion.setMaster("local[2]");
        JavaSparkContext conexion = new JavaSparkContext(configuracionDeLaConexion);

        // PASO 2: Convertir mis datos en un objeto de tipo RDD (el equivalente en Spark a los Stream de Java
        List<String> tweets = getTweets();
        JavaRDD<String> tweetsRDD = conexion.parallelize(tweets, 20);

        // PASO 3: Aplicar el Map-Reduce
        var hashtags = tweetsRDD
                // Separar las palabras/tokens de cada tweet
                .map( tweet -> tweet.replace("#", " #") )   // Me aseguro que los hashtags quedan separados entre si         Stream<String>
                .map( tweet -> tweet.split("[ .,_;:()?¿!¡<>+@-]+") )     // Separo los tokens de las frases, incluyendo los hashtags      Stream<String[]>
                .flatMap( arrayDeTokens -> Arrays.asList(arrayDeTokens).iterator() )                                     // Convierto el array de palabras en un Stream de palabras       Stream<Stream<String>>
                .filter( palabra -> palabra.startsWith("#") )           // Me quedo solo con los hashtags                                Stream<String>
                .map( String::toLowerCase )                                    // Paso todos los hashtags a minúsculas                          Stream<String>
                .map( hashtag -> hashtag.substring(1) )             // Elimino el # de cada hashtag                                  Stream<String>
                .filter( hashtag -> getPalabrasProhibidas().stream().noneMatch( hashtag::contains ) )  // Eliminar los hashtags que contengan palabras prohibidas         Stream<String>

        /*

        En un java.util.Map, las claves (KEYS) son únicos a nivel de la colección. En un JavaPairRDD<K,V> Los keys no se tratan como elementos UNICOS
        new Tuple2<>(dato1, dato2) -> MAP ->  JavaRDD<Tuple2<String, Integer>>
                        ^    ^
                    palabra , 1
        En Spark existe un tipo especial de RDD: PairRDD<String, Integer> -> RDD<Tuple2<String, Integer>>
        Existe una función para ello, llamada mapToPair -> reduceByKey(sume los valores al juntar las entradas con el mismo Key)
                    goodVives 17
                    odioLasNavidades 3
                    enLaPlaya  14
        Tengo que ordenar... y me tengo que apañar con el sortByKey... es lo que hay! Que me toca entonces? Dar la vuelta a la tupla... otro map
        Una vez ordenado, extraigo los N primeros... Y TACHAN !!!! TRENDING TOPICS CALCULADOS!

        De una tupla, puedo sacar el primer valor con ._1... y el segundo valor con ._2
        Para al final sacar por pantalla el resultado
        */
                .mapToPair( hashtag -> new Tuple2<>(hashtag, 1))
                .reduceByKey(Integer::sum)
                .mapToPair(tupla -> new Tuple2<>(tupla._2, tupla._1))
                .sortByKey(false)
                .take(10);

        // PASO 4: Procesar el resultado: (guardarlo en una BBDD, fichero..., imprimirlo por pantalla)
        hashtags
                .forEach( tupla -> System.out.println("Hashtag: "+ tupla._2+", cantidad: "+tupla._1) );

        // PASO 5: Cerrar conexión con el cluster
        conexion.close();
    }

    private static List<String> getTweets() {
        try{
            URL recurso=PalabrasSimilares.class.getClassLoader().getResource("tweets.txt");
            if(recurso == null) {
                System.out.println("Diccionario no encontrado");
                System.exit(1);
            }
            return Files.readString(Path.of(recurso.toURI()))
                    .lines()
                    .filter( linea -> ! linea.isBlank())
                    .filter( linea -> linea.contains("#"))
                    .collect(Collectors.toList());
        }catch(IOException io){
            System.out.println("Error al leer el fichero");
            io.printStackTrace();
            System.exit(1);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    private static List<String> getPalabrasProhibidas() {
        return List.of("caca","culo","pedo","pis","mierda");
    }

}