package ejemplos;
public class Tipado {
    public static void main(String[] args) {
        String texto = "hola";
        var texto2 = "hola"; // La palabra var en JAVA se añade en la versión 10
        // A qué tipo de datos puede referenciar la variable texto2? 
        // A cualqiera? 
            // SI:              IIIII ... Si esto fuese JS habríamos acertado... en JAVA NO
            // Solo a String:   JAVA sigue siendo un lenguaje de tipado ESTATICO
            // Y las variables, por mucho que ponga la palabra "var" al definirlas, 
            // siguen teniendo un tipo de dato... Lo que pasa es que a partir de java 10,
            // el tipo de dato se puede inferir
        // texto2 = 33.67; // ESTO NO FUNCIONA... NO ES JS
    }
}
