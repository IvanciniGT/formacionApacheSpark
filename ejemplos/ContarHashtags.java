import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class ContarHashtags {

    public static void main(String[] args) {
        List<String> tweets = getTweets();
        tweets
                .stream()                                                                                                                    // Stream<String>
                // Separar las palabras/tokens de cada tweet
                .map( tweet -> tweet.replace("#", " #"))    // Me aseguro que los hashtags quedan separados entre si         Stream<String>
                .map( tweet -> tweet.split("[ .,_;:()?¿!¡<>+@-]+") )     // Separo los tokens de las frases, incluyendo los hashtags      Stream<String[]>
                .flatMap( Arrays::stream )                                     // Convierto el array de palabras en un Stream de palabras       Stream<Stream<String>>
                .filter( palabra -> palabra.startsWith("#") )           // Me quedo solo con los hashtags                                Stream<String>
                .map( String::toLowerCase )                                    // Paso todos los hashtags a minúsculas                          Stream<String>
                .map( hashtag -> hashtag.substring(1) )             // Elimino el # de cada hashtag                                  Stream<String>
                .filter( hashtag ->  )                              // Eliminar los hashtags que contengan palabras prohibidas         Stream<String>
                .forEach( System.out::println );
    }

    private static List<String> getTweets() {
        try{
            return Files.readString(Path.of("tweets.txt"))
                 .lines()
                 .filter( linea -> ! linea.isBlank()) 
                 .filter( linea -> linea.contains("#"))
                 .toList();
        }catch(IOException io){
            System.out.println("Error al leer el fichero");
            io.printStackTrace();
            System.exit(1);
        }
        return null;
    }
    private static List<String> getPalabrasProhibidas() {
        return List.of("caca","culo","pedo","pis","mierda");
    }

}