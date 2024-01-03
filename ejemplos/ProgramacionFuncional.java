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

public class ProgramacionFuncional {
    
    public static void main(String[] args) {

        String nombre = "Juan";
        saluda(nombre);

        // En JAVA 1.8, aparece un nuevo operador: "::",
        // Que nos permite referenciar a un método de una clase
        Consumer<String> miFuncion = ProgramacionFuncional::saluda; 
        miFuncion.accept(nombre);

        imprimirSaludo(nombre, ProgramacionFuncional::generarSaludoFormal);
        imprimirSaludo(nombre, ProgramacionFuncional::generarSaludoInformal);

        // En JAVA 1.8, aparece un segundo operador: "->", que nos permite definir expresiones lambda
        Function<String,String> miFuncionGeneradora = ProgramacionFuncional::generarSaludoFormal;
        Function<String,String> miFuncionGeneradora2 = (String unNombre) -> {
                                                                                return "Buenos días " + unNombre;
                                                                            };
        Function<String,String> miFuncionGeneradora3 = (unNombre) -> {
                                                                                return "Buenos días " + unNombre;
                                                                            };
        Function<String,String> miFuncionGeneradora4 = unNombre -> {
                                                                                return "Buenos días " + unNombre;
                                                                            };
        Function<String,String> miFuncionGeneradora5 = unNombre -> "Buenos días " + unNombre;

        imprimirSaludo(nombre, miFuncionGeneradora);
        imprimirSaludo(nombre, miFuncionGeneradora2);
        imprimirSaludo(nombre, miFuncionGeneradora3);
        imprimirSaludo(nombre, miFuncionGeneradora4);
        imprimirSaludo(nombre, miFuncionGeneradora5);
        imprimirSaludo(nombre, persona -> "Ciao " + persona);
    }

    public static void saluda(String nombre) {
        System.out.println("Hola " + nombre);
    }

    public static String generarSaludoFormal(String nombre) {
        return "Buenos días " + nombre;
    }

    public static String generarSaludoInformal(String nombre) {
        return "Qué pasa " + nombre;
    }

    public static void imprimirSaludo(String nombre, Function<String, String> funcionGeneradoraDeSaludos) {
        System.out.println(funcionGeneradoraDeSaludos.apply(nombre));
    }

}
