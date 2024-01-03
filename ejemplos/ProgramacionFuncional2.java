package ejemplos;

import java.util.function.*; // Este esa el aporta grande de JAVA 1.8
// Un paquete que define interfaces funcionales...
// Es decir, interfaces que representan un tipo de dato compatible con funciones.
// - Consumer<T>    Representa una función que acepta un dato de tipo T y no devuelve nada
                    // Setter
// - Supplier<T>    Representa una función que no acepta nada y devuelve un dato de tipo T
                    // Getter
// - Function<T,R>  Representa una función que acepta un dato de tipo T y devuelve un dato de tipo R
// - Predicate<T>   Representa una función que acepta un dato de tipo T y devuelve un booleano
                    // Is, Has, ...

import java.util.*;
import java.util.stream.*;

public class ProgramacionFuncional2 {
    
    public static void main(String[] args) {

        List<Integer> numeros = List.of(1,2,3,4,5,6,7,8,9,10);
        // Bucles. Pre java 1.5
        for (int i = 0; i < numeros.size(); i++) {
            System.out.println(numeros.get(i));
        }
        // Bucles for-each: pre Java 1.8
        for (Integer numero : numeros) {
            System.out.println(numero);
        }
        // Programación funcional: Post Java 1.8
        // En cualquier colección encontramos el método: forEach, que nos permite recorrer la colección (ITERAR), 
        // aplicando una función a cada elemento
        numeros.forEach(numero -> System.out.println(numero));
        numeros.forEach(System.out::println);

        // En Java 1.8, se añade un segundo paquete, 
        // que usamos muchísimo: java.util.stream, donde se define la interfaz Stream<T>
        // Un Stream<T> es una colección de datos de ese tipo (Similar a una lista), solo que no tiene las mismas funciones que la clase List:
        // - List: get, add, remove...
        // - Stream: filter, map, reduce, ...
        // Stream es una colección de datos pensada para aplicar programación funcional.

        // Cualquier colección de JAVA: List, Set, Map, Array, puede transformarse a un Stream mediante el método: .stream()
        // Cualquier Stream puede transformarse a una colección mediante el método: .collect( COLECTOR)
        // El COLECTOR es una interfaz que define a qué tipo de colección queremos transformar el Stream

        /* 
            Map     .stream()    -> Stream -> .collect(Collectors.toList()) -> List
            Set                               .collect(Collectors.toSet())  -> Set
            List                              .collect(Collectors.toMap())  -> Map
            Array
        */ 

        Stream<Integer> streamDeNumeros = numeros.stream();
        // Dentro de la interfaz Stream encontramos métodos para realizar programación MAP-REDUCE
        Stream<Integer> numerosDoblados = streamDeNumeros.map( numero -> numero * 2);

        /// MÁS CODIGO
        // Llegados a esta lkinea de código, qué tenemos dentro de la variable: numerosDoblados?
        // 2, 4, 6, 8, 10, 12, 14, 16, 18, 20? NO. Aún JAVA no ha ejecutado esa transformación (MODO LAZY) solo apunta que hay que hacerla

        numerosDoblados.forEach(System.out::println); // Aquí se calculan los dobles y se van imprimiendo

        // En programación imperativa, para calcular el doble:
        for (Integer numero : numeros) {        // Para cada numero
            System.out.println(numero * 2);     // Multiplica por 2 e imprime
        }
        // En programación map-reduce:
        numeros.stream()                        // Para cada numero
               .map(numero -> numero * 2)       // Multiplica por dos
               .forEach(System.out::println);   // E imprime
        // Una de las grandes ventajas de la programación map-reduces es la legibilidad que aporta al código

        numeros.stream()                        // Para cada numero
               .map(numero -> numero * 2)       // Multiplica por dos
               .map(numero -> numero + 5)       // Suma 5
               .map(numero -> numero / 3)       // Divides por 3
               .forEach(System.out::println);   // E imprime

        numeros.stream()                                    // Para cada numero
               .filter( numero -> (numero % 2 == 0) )       // Me quedo con los pares
               .forEach(System.out::println);               // E imprime
    }

}
