package ejemplos.pi;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CalcularPi {
    
    public static void main(String[] args) {
        // Forma tradicional: PROGRAMACION IMPERATIVA
        int N = 100*1000*1000; // Podría abrir 8 hilos y que cada uno procesase 150.000.000    
        int n = 0;
        for (int i = 0; i < N; i++) {
            double x = Math.random();
            double y = Math.random();
            double d = x*x + y*y; //Math.sqrt(x*x + y*y);
            if(d <= 1) n++;
        }
        double pi = 4.0 * n / N;
        System.out.println("El valor de pi es: " + pi);

        // Cuántos hilos está usando mi programa? 1: El hilo principal ... pongo un core al 100%... 
        // vamos... que estoy haciendo el gilipollas

        // Dado que tengo una computadora capaz de ejecutar 8 hilos a la vez, podría abrir más hilos....
        // lo cuál en JAVA es divertidísimo! (No es divertido en absoluto)


        // Forma funcional: PROGRAMACION FUNCIONAL
        // IntStream == Stream<Integer> ? NO
        // DoubleStream == Stream<Double> ? NO... sino un Stream<double>
        // Realmente es un Stream<int> ESTO ES UNA CAGADITA DEL JAVA
                                                // ---- ????? ---> PI... o al menos algo que me permita calcular PI: n
        //                   mapToObject           map                filter          
        //IntStream  ---> Stream<Double[]> --> Stream<Double> --> Stream<Double> --> int

               //.map( posiciones -> Math.pow(posiciones[0], 2) + Math.pow(posiciones[1],2) )  // Mirar la distancia al origen. NOTA ES MUCHO MAS INEFICIENTE QUE LA DE ABAJO: MULTIPLICAR EL NUMERO POR SI MISMO


        long n2 = IntStream.range(0, N)                                           // Para cada tirada
                 .parallel()                                                                     // Paralelizar el trabajo
                                                                                                 // Va a abrir tantos hilos como cores tenga disponibles para efectuar el trabajo. 
                                                                                                 // El trabajo se reparte entre todos los hilos
                                                                                                 // Y se espera a que todos acaben, para dar el resultado consolidado.
                 .mapToObj( numeroDeTirada -> new double[] {Math.random(), Math.random()} )      // Calculo el punto de impacto
                 .map( posiciones -> posiciones[0]*posiciones[0] + posiciones[1]*posiciones[1] ) // Mirar la distancia al origen
                 .filter( distancia -> distancia <=1 )                                           // Quedarme con los que están en el círculo
                 .count();                                                                       // Contar cuántos hay

        pi = 4.0 * n2 / N;
        System.out.println("El valor de pi es: " + pi);
    }

}