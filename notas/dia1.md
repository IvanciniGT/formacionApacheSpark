
# Qué vamos a hacer en la formación:
 
1. Entender qué es la programación funcional.
2. Entender qué es el modelo de programación MapReduce (Desde JAVA con Streams)
3. Llevarnos los programas que hagamos en JAVA(con modelo MapReduce) a Apache Spark.
    ^^ Nos obligará a cambiar 2 líneas de código.
4. Vamos a usar una de las librerías que nos ofrece Spark para hacernos la vida más fácil: SparkSQL.
5. Haremos algunos ejemplos de programas tipo, según los casos de uso del banco.
6. Formato: AVRO o PARQUET

---

# Bigdata

Tiene que ver con las estrategias que usamos al lidiar con:
- Grandes volúmenes de datos que quiero manipular
- Datos que se producen a gran velocidad
- O que deban ser consumidos en una ventana muy pequeña de tiempo.
- Datos con estructura muy compleja.

Qué quiero hacer con la información:
- Almacenarla       (Cassandra, MongoDB, HBase, Hive, etc...)
- Transformarla     (Spark, Flink, Storm, etc...)
- Transmitirla      (Kafka, Flume, etc...)

ClashRoyale: 24 mensajes/seg en una partida promedio.
- 50.000 partidas simultáneas: 1.200.000 mensajes/seg
No quiero almacenar esa información en ningún sitio. No me interesa
No quiero transformarla
Solo quiero transmitirla.

Y no hay máquina en el mundo capaz de soportar esa carga de trabajo.
Tengo un problema.

## Ejemplo 1:

Imaginad que quiero apuntar los productos que quiero comprar en el mercadona... 
Qué programa puedo usar para hacer la lista: Producto, cantidad.
- Excel                 200... 50.000 productos
- BBDD (Access)         200.000 datos
- MariaDB/MySQL         1.000.000
- MS SQL Server         10.000.000
- Oracle                100.000.000

## Ejemplo 2:

Imaginad que un primo de un amigo de mi tío segundo ha descargado un a película de Internet.
- Esa película ocupa 5 Gbs... y tengo un pincho USB libre (vacío) de 16Gbs. Entra?
  Quizás o quizás no. Depende del Formato (sistema de archivos).
    - FAT16: 2 Gbs
    - FAT32: 4 Gbs
    - NTFS:  Archivos mucho más grandes.

Y qué pasa cuando quiero guardar u archivo de 10 Pb... o de 1 Eb... 
Ni FAT, ni NTFS, ni EXT4, ni XFS, ni ZFS, ni BTRFS, ni APFS, ni HFS+...
De hecho ni siquiera tengo un disco duro de 10 Pb.

Quién le dió una primera solución a todo esto fue Google.
Montaron el BigTable (BBDD) y el GFS (Sistema de archivos distribuido).
Todo ello funcionaba sobre un cluster de máquinas de mierda (commodity hardware).
En lugar de tener una gran (enorme, gigante, aberrante) máquina, tenían un montón de máquinas de mierda.

Y lo que hacemos es usar la RAM y CPU de todas esas máquinas para hacer una gran máquina virtual que sea capaz de sacar mucho trabajo.

La gente de Google, publicó un paper (artículo científico) sobre cómo habían montado todo eso.
Y un hombrecillo, amable, llamado Doug Cutting, leyó ese paper y dijo: "Yo quiero hacer eso".
Y montó un software basado en esas ideas que se llamó Hadoop.... y que fue auspiciado por la fundación Apache.

# APACHE HADOOP

Viene a ser el equivalente a un SO para un cluster de máquinas.
Es capaz de ejecutar un tipo de programa muy especial (no cualquier programa) usando la RAM y CPU de todas las máquinas del cluster. Además, es capaz de guardar información en disco de forma distribuida entre todas las máquinas del cluster.

Hadooop ofrece 3 servicios:
- YARN: Yet Another Resource Negotiator: Se encarga de controlar/monitorizar los recursos de las máquinas del cluster.
- HDFS: Hadoop Distributed File System: Sistema de archivos distribuido.
- MapReduce: Es una forma de diseñar programas para manipular datos de forma altamente paralelizable.
             Apache Hadoop no es el único software que nos permite usar un modelo de programación MAP - REDUCE.
             Lo  tenemos de serie en JAVA (Streams) y en Python (map, filter, reduce, lambda, list comprehension, etc...), y en JS....
  La implementación de MapReduce en Hadoop es muy poco eficiente, ya que el resultado de cualquier operación se guarda en HDD.

# Apache Spark

Framework para la manipulación/transformación de datos dentro de un cluster BIGDATA.
Ofrece librerías para su uso desde: 
- Java
- Python
- R
- Scala

Es una herramienta que instalamos dentro de un cluster.
Y sobre esa herramienta vamos a ejecutar nuestros programas.

Ofrece una reimplementación de MapReduce que es mucho más eficiente que la de Hadoop, ya que el resultado de cualquier operación se guarda en RAM.

TODO Lo que vamos a estar haciendo en la formación es programas bajo un modelo MAP - REDUCE.
Es lo mismo que los Streams de JAVA... pero a lo bestia.

El modelo de programación MapReduce está basado en el concepto de Programación Funcional!
Y lo primero que tenemos que hacer es entender y familiarizarnos con qué es la programación funcional.

Usos de Apache Spark:
- Montar ETLs.
  Una ETL (Extract, Transform, Load) es un programa que se encarga de extraer datos de una fuente (normalmente una BBDD o un fichero), transformarlos y cargarlos en otra fuente (Otra BBDD u otro fichero).
  En el caso de ficheros... con formato: AVRO o PARQUET.

    2 escenarios típicos de uso de ETLs:
        - Mover datos de las BBDD de producción a un datawarehouse
        - Consolidar información que recibo desde diferentes fuentes (BBDD, ficheros, APIs, etc...)
            Tengo un conjunto de archivos de texto con información que me mandan muchos proveedores. 

# Tipos de software

- Sistema operativo
- Driver
- Aplicación
- Servicios/Demonios
- Scripts             <<< Este es el tipo de programa que montaremos con Spark
- Comandos

# JAVA

Lenguaje de programación hoy en día propiedad de Oracle... anteriormente Sun Microsystems.
Sun hacía servidores con micro-procesadores propios (SPARC) y sistemas operativos propios (Solaris).

---

# Programación funcional

En el mundo de la programación usamos distintos paradigmas de programación:
- Programación imperativa               Cuando damos instrucciones (órdenes) que deben procesarse secuencialmente.
                                        En ocasiones debemos romper esa secuencialidad: (if, for, while, until, goto, break, continue, etc...)
- Programación procedural               Cuando el lenguaje me permite definir procedimientos (funciones, métodos, subrutinas,
                                        módulos...) e invocarlos posteriormente.
                                        Me permite crear unidades de código reutilizables.... y estructurar mejor el código.
- Programación funcional                Cuando el lenguaje me permite que una variable apunte a una función
                                        y posteriormente ejecutar(invocar) a esa función desde la variable.
                                        En JAVA, de los últimos lenguajes en soportar programación funcional, esto se
                                        incorporó en la versión 8.
- Programación orientada a objetos      Cuando el lenguaje me permite definir mis propios tipos de datos.. con sus
                                        propiedades y métodos.
                                        
                                                   se caracteriza por (propiedades)         operaciones/funciones
                                        String      secuencia de caracteres                  concatenar, extraer, mayúsculas
                                                                                             minúsculas, etc...
                                        Date        día, mes, año                            restar, si es un año bisiesto,
                                                                                             en qué día de la semana cae,  etc...
                                        Persona     nombre, apellidos, fecha de nacimiento   calcular edad, eres mayor de
                                                                                             edad etc...
- Programación declarativa
- ...
Un paradigma no es sino una forma de usar el lenguaje para transmitir una cierta información.

## Ejemplo: 

Felipe, pon una silla debajo de la ventana                  IMPERATIVO
Felipe, debajo de la ventana tiene que haber una silla      DECLARATIVO

## Programación funcional. Variables

```java
String texto = "hola";      # Statement: Declaración, Sentencia, Enunciado, Frase, Oración
                            # Guardamos el texto "hola" en la variable "texto"
                            # Haciendo que la variable "texto", apunte al texto "hola"
```

Esa línea de código (en programación llamada Statement) se compone de 3 partes:
- "hola"                    Crea un objeto de tipo String(texto) en RAM con valor "hola"
- String texto              Crea una variable de tipo String llamada texto.
- =                         Asignamos la variable al dato que tengo en memoria (en nuestro caso un texto: "hola")

```java
texto = "adios";            # Statement: Declaración, Sentencia, Enunciado, Frase, Oración
```
- "adios"                   Crea un objeto de tipo String(texto) en RAM con valor "adios", en otro sitio distinto.
                            Y llegados a este punto, tengo 2 objetos de tipo String en memoria RAM: "hola" y "adios"
- texto                     Despega el post-it de donde estaba.
- =                         Asignamos la variable al dato que tengo en memoria (en nuestro caso un texto: "adios")
                            En este punto, el dato "hola" se queda sin variable que lo apunte... y no tengo forma de volver a referenciar (ni recuperar) ese valor. Se ha convertido en basura: Garbage.
                            Y quizás... solo quizás... el recolector de basura de JAVA, lo elimine de memoria.

## Programación funcional. Funciones

Un lenguaje soporta programación funcional cuando me permite que una variable apunte a una función y posteriormente ejecutar(invocar) a esa función desde la variable.

El concepto es sencillo... lo complejo... y donde nos estalla la cabeza cuando comenzamos a usar programación funcional... usando las nuevas formas de estructurar un programa que tengo a mi disposición cuando el lenguaje soporta programación funcional.

Al poder hacer que una variable apunte a una función... puedo crear funciones que acepten otras funciones como argumentos. Y puedo crear funciones que devuelvan otras funciones como resultado.
CATAPUM !!!!

## Expresiones lambda

Una expresión que devuelve una función anónima que defino en linea... y que ejecutaré... npi de donde... ya lo veré.

```java

int numero = 17;      // Statement
int texto = "hola";   // Statement
int cuadrado = numero * numero ; // Statement
            // ^- Expresión -^
```

Una expresión es una porción de código que devuelve un valor.

Una expresión lambda, es una porción de código que devuelve una función anónima definida dentro de un statement.

# Modelo de programación MAP-REDUCE

Es una forma de diseñar programas para manipular datos de forma altamente paralelizable.
Se basa en programación funcional.
No es posible hacer un programa MAP-REDUCE en un lenguaje que no soporte programación funcional.

Lo que hacemos es diseñar un programa que partiendo de una colección de datos, 
vaya transformando esos datos en otras colecciones de datos, hasta que obtengamos el resultado que me interese.

Cuando escribimos un algoritmo según un modelo map-reduce, tenemos a nuestra disposición 2 tipos de funciones. Hay muchas funciones tipo MAP y muchas funciones tipo REDUCE.

## Funciones tipo MAP

Es una función que se aplica sobre una colección de datos que acepte programación funcional (en el caso de JAVA: Stream)
y que devuelve otra colección de datos que acepte más programación funcional (en el caso de JAVA: Stream)

Las funciones de tipo map tienen una característica muy importante: Son funciones que se ejecutan en modo perezoso: LAZY.
Es decir, realmente no se ejecutan cuando el programa pasa por esa línea de código... sino que se ejecutan cuando el programa necesita el resultado de esa función.

Ejemplos de funciones tipo map:
- map

    <T>map(Function<T,R>) -> Stream<R>
    Genera una nueva colección de datos, que contendrá el resultado de aplicar una función de mapeo (transformación) sobre cada elemento de la colección original.

    Stream1                    Stream2
      1       .map( doblar )      2
      2                           4
      3                           6
      4                           8

        public int doblar(int numero) {
            return numero * 2;
        }

        (int numero) -> {
            return numero * 2;
        }
        
        numero ->  numero * 2

      Usamos MUCHISIMO la función MAP. Nos permite transformar datos, aplicándoles una función de transformación.

- filter
- flatMap
- sorted

## Funciones tipo REDUCE

Es una función que se aplica sobre una colección de datos que acepte programación funcional (en el caso de JAVA: Stream)
y devuelve algo que no es una colección de datos que acepte programación funcional (en el caso de JAVA: Stream)

Las funciones de tipo reduce tienen una característica muy importante: Son funciones que se ejecutan en modo ansioso: EAGER.
Es decir, realmente se ejecutan cuando el programa pasa por esa línea de código.

Ejemplos de funciones tipo reduce:
- reduce
- collect
- count
- sum

Apache Spark lo que nos ofrece es una alternativa a la interfaz Stream de JAVA, que puede ejecutarse en un cluster de Hadoop/Spark. En Apache Spark en lugar de trabajar con Streams, trabajamos con RDDs (Resilient Distributed Dataset)

## Algoritmos Map-Reduce

Cuando diseñamos un algoritmo Map-Reduce, lo que hacemos es encadenar funciones tipo MAP, de forma que vamos transformando los datos de entrada en otros datos que nos interesen más, para finalizar aplicando una función tipo REDUCE que nos devuelva el resultado que nos interesa.

Stream1 -> map1 -> Stream2 -> map2 -> Stream3 -> map3 -> Stream4 -> map4 -> Stream5 -> reduce -> algo que no es un Stream
         <-- se ejecutan en modo lazy ----------------------------------------------->   ^
                                                                              Se ejecuta en modo eager

  stream1.map(1)  // -> Stream2
         .map(2)  // -> Stream3
         .map(3)  // -> Stream4
         .map(4)  // -> Stream5
         .reduce(5) // -> algo que no es un Stream

Cada vez que usamos una función tipo MAP, lo que le indicamos a JAVA es:
  "quiero que hagas TAL COSA con los datos que tengo en memoria... y el resultado lo guardas en memoria..."
 ojo... no le estoy diciendo: 
  "haz TAL COSA con los datos que tengo en memoria y el resultado lo guardas en memoria"

Java se anota el trabajo (MODO LAZY) y lo ejecutará en el futuro... cuando el resultado de ese trabajo sea necesario.
Al ejecutar la función tipo REDUCE (MODO EAGER), JAVA ejecutará todas las funciones tipo MAP que se haya anotado.

---

Stream      MAP                MAP                                                              ---> n
1           ----> [x1, y1]   ---> d1   -->                            FILTRO       --->  CONTAR
2           ----> [x2, y2]   ---> d2   -->                              d2
3           ----> [x3, y3]   ---> d3   -->                              d14
...
999.999.999 ----> x999.999.999, y999.999.999 ---> d999.999.999 -->

           mapToObject           map                filter          
IntStream  ---> Stream<Double[]> --> Stream<Double> --> Stream<Double> --> int

IntStream define su propia función map... que devuelve un IntStream
mapToObject es una función que se define en IntStream... que devuelve un Stream de objetos

---

# Ejercicio 2: Identificar las palabras similares en un diccionario a una palabra X -> Ofrecer sugerencias de corrección de palabras. 

Partimos de una colección de palabras válidas (diccionario) -> List<String>
Me llega una palabra... y tengo que ver las que más se parecen de mi lista a ella.
Para ello, vamos a usar una función típica: Distancia de Levenshtein.
Esa función, aplicada sobre 2 palabras, nos devuelve el número de caracteres que es necesario:
- Añadir
- Eliminar
- Cambiar
de una palabra para convertirla en la otra.
Si la distancia entre 2 palabras es 0, eso significa: Son iguales... es la misma palabra.
Si la distancia entre 2 palabras es 1, eso significa: Son parecidas... se diferencian en 1 carácter.
  manana - mañana   -> 1
  manana - manzana  -> 1
Si la distancia entre 2 palabras es 2, eso significa: Son parecidas... se diferencian en 2 caracteres.
  manana - manzano  -> 2


  manana
                map                        sorted                                         limit(10)
  STREAM                             STREAM            ORDENAR DE MENOR A MAYOR    
  manzana   -> distancia contra manana: 1, manzana  -->                                     --> Las primeras 10 a una lista 
  mañana                                1, mañana       ordeno en base a la distancia
  manzano                               2, manzano                            ---> Descartar la inf de distancia
  melocotón                             7, melocotón                                    map

Stream<String>    ---> Stream<PalabraPuntuada>          --->                      Stream<String> ---> List<PalabraPuntuada>

      Querremos filtrar aquella palabra muy diferentes. Por ejemplo con una distancia de levenshtein > 2 caracteres.
      Lo haré antes de ordenar.

    Si la longitud de la palabra de entrada difiere en más de 2 caracteres de la longitud de la palabra del diccionario, no tiene sentido calcular la distancia de levenshtein.... ya que siempre será al menos 3... y será filtrada más adelante...
    Para qué procesarla... descartada de partida.

private static class PalabraPuntuada {
    public String palabra;
    public int puntuacion;

    public PalabraPuntuada(String palabra, int puntuacion) {
        this.palabra = palabra;
        this.puntuacion = puntuacion;
    }
}

archoplelagu
archipielago