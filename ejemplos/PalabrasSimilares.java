import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class PalabrasSimilares {

    private static final int DISTANCIA_MAXIMA_PERMITIDA      = 2;
    private static final int MAXIMO_DE_PALABRAS_A_DEVOLVER   = 10;

    public static void main(String[] args) {
        final List<String> diccionario = getPalabrasDelDiccionario();
        if(args.length == 0) {
            System.out.println("Debe introducir una palabra");
            System.exit(1);
        }
        final String palabraObjetivo = args[0].toLowerCase();
        List<String> palabrasSimilares = diccionario//.stream() // Para cada palabra
                                                    //.parallel()
                                                    .parallelStream()
                                                    .filter(  palabra -> Math.abs(palabra.length() - palabraObjetivo.length()) <= DISTANCIA_MAXIMA_PERMITIDA)   // Me quedo con palabras de similar longitud
                                                    .map(     palabra -> new PalabraPuntuada(palabra, distanciaLevenshtein(palabra, palabraObjetivo)))          // Calculo la distancia de Levenshtein   
                                                    .filter(  palabraPuntuada -> palabraPuntuada.puntuacion <= DISTANCIA_MAXIMA_PERMITIDA)                      // Me quedo con las palabras similares a la objetivo
                                                    .sorted(  Comparator.comparing(palabraPuntuada -> palabraPuntuada.puntuacion) )                             // Ordeno por distancia
                                                    .limit(   MAXIMO_DE_PALABRAS_A_DEVOLVER )                                                                   // Corto por las N más similares
                                                    .map(     palabraPuntuada -> palabraPuntuada.palabra )                                                      // Descarto la puntuación
                                                    .collect( Collectors.toList() );                                                                            // Recojo en una lista

        System.out.println("Palabras similares a: "+ palabraObjetivo);
        palabrasSimilares.forEach(System.out::println);
    }

    private static List<String> getPalabrasDelDiccionario() {
        //return List.of("manzana", "manzano", "mañana", "melocotón");
        // En Java 11, dentro de la clase Files, añadireon POR FIN!!!, ALELUYA!!!, 
        // un método que nos permite leer un fichero de texto
        try{
            return Files.readString(Path.of("diccionario.ES.txt"))
                 .lines()
                 .filter( linea -> ! linea.isBlank()) // ME quedo con la linea si tiene algo
                 .filter( linea -> linea.contains("=")) // Me quedo con la linea si contiene un =
                 .map(    linea -> linea.split("=")[0].toLowerCase())
                 .toList();
        }catch(IOException io){
            System.out.println("Error al leer el fichero");
            io.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private static class PalabraPuntuada {
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
