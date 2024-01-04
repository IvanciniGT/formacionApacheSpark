
# Ejemplo Trending Topics

Partimos de una lista donde cada item es un tweet.

```txt
En la playa con mis amigos #summerLove#goodVibes
En navidades estudiando #mierdaDeNavidad#odioLaNavidad #odioElTurrón#odioMiVida. PD: No me gusta el turrón.
En la bolera con los primos #familyTime#bowling#goodvibes
```
                            vvvvvvv
    summerLove  10
    goodVibes   57
    mierdaDeNavidad <<< ELIMINADO por contener la palabra mierda... que todo el mundo sabe que si la escuchas se te explota el cerebro
    odioLaNavidad  5
                            vvvvvvv
    goodVibes   57
    summerLove  10



---
En
la
playa
con
mis
amigos
#summerLove
#goodVibes
En
navidades
estudiando
#mierdaDeNavidad
#odioLaNavidad
#odioElTurrón
#odioMiVida
PD
No
me
gusta
el
turrón
En
la
bolera
con
los
primos
#familyTime
#bowling
#goodvibes

---

# Apache Spark

Es un framework que nos ofrece una implementación alternativa a la de Hadoop para Map-Reduce.
De forma que el resultado de todas las operaciones no se guarde en HDD, sino en RAM.

## Instalación / Arquitectura de un cluster de hadoop-spark

    <---------------------------- CLUSTER ------------------------>
    NODO MAESTRO            NODO TRABAJADOR         NODO TRABAJADOR
    NODO1(Máquina1)         NODO2(Máquina2)         NODO3(Máquina3)
    SO Linux                SO Linux                SO Linux
    Hadoop                  Hadoop                  Hadoop
    Spark                   Spark                   Spark
    JVM                     JVM                     JVM
     ^ Abre un puerto de comunicaciones en RED (por defecto 7077)
     ^
     ^
    Otro computador
        desde el que lanzo trabajos al cluster
        JVM

    El trabajo que se realice se reparte entre los nodos trabajadores.
    En Apache Spark, lo que ocurre es que el conjunto inicial de datos (lo que en JAVA llamábamos un Stream) se particiona. Y cada partición es enviada a un nodo trabajador. 
    Un nodo trabajador puede recibir muchas particiones de trabajo.

    10M de datos -> 100 particiones de 100K cada una -> 10 nodos trabajadores -> 10 particiones por nodo trabajador

    Un factor crítico a la hora de trabajar con Spark es el control de las comunicaciones de red. 
    Pueden destrozar literalmente el rendimiento de un cálculo o una transformación de datos.

    En Spark, los datos sobre los que se aplican operaciones Map-Reduce se denominan RDD (Resilient Distributed Dataset).

    Si tengo n nodos trabajadores disponibles, cuántas particiones me interesa hacer de los datos?
        Al menos n... para que todo nodo obtenga trabajo
        Me interesa hacer muchas más? Cuantas más particiones empeoran las comunicaciones de red y la consolidación de los resultados parciales.
        Pero... si lanzo un trabajo a un nodo... que tarda 20 minutos y al minuto 19 el nodo se cae, pierdo todo el trabajo.... 20 minutos. Spark lo manda de nuevo a otro nodo... pero los 20 minutos ya están perdidos.
        Si por contra, ese mismo trabajo lo parto 20 veces... de forma que se tarde 1 minuto en procesar cada partición, si un nodo se cae, solo pierdo 1 minuto de trabajo.

    Cuando mande datos por red, me tengo que asegurar que son Serializables, en JAVA que implementan la interfaz Serializable.
    La serialización es el proceso de convertir un objeto en RAM en un stream de bytes, para poder enviarlo por red, o guardarlo en disco.
    La deserialización es el proceso inverso, convertir un stream de bytes en un objeto en RAM.

    Pero el problema es más gordo.
    Si tengo un objeto de tipo "Persona", serializable... que he construido en el computador desde el que lanzo el trabajo, y lo quiero mandar al nodo maestro del cluster no basta con que el objeto sea serializable.
    Voy a necesitar también que la definición del objeto "Persona" (la clase... el código) esté disponible en el ordenador/computadora que recibirá los datos.

## Estructura típica de un programa Spark

1. Abrir una conexión con el cluster de Spark
2. Transformar los datos de entrada en un objeto de tipo RDD (algo así como un Stream de Java)
3. Aplicar operaciones Map-Reduce sobre el RDD
4. Recoger los resultados
5. Cerrar la conexión con el cluster de Spark

## Tenemos un cluster de Spark a mano para ejecutar pruebas? NO verdad

La propia librería Spark nos ofrece un cluster de spark de pruebas que podemos ejecutar en local en automático.
Para desarrollo es genial !

# Qué era UNIX®?

Unix era un Sistema Operativo (el que ha sido el SO más influyente del mundo) que se desarrolló en los años 70 en los laboratorios Bell de AT&T.

Ese sistema operativo se licenciaba a grandes compañías, que lo adaptaban a sus necesidades (generando su propia versión) y lo vendían a sus clientes.

En un momento llego a haber más de 200 versiones de Unix.
Los programas que se montaban para una versión a veces no funcionaban en otras versiones del SO.

Hubo que poner un poco de orden: Y salen 2 estándares para regular cómo debían evolucionar esas versiones de UNIX: 
- POSIX (Portable Operating System Interface)
- SUS (Single Unix Specification)

Unix dejo de fabricarse en los años 90.

# Qué es UNIX®?

Hoy en día UNIX son esos 2 estándares (POSIX y SUS) que regulan cómo puede crearse un SO, que quiera ser compatible con estas especificaciones.

Los grandes fabricantes de hardware montan sus propios SO para sus equipos. cumpliendo con los estándares POSIX y SUS.

Oracle: SOLARIS (Unix®)
IBM:    AIX (Unix®)
HP:     HP-UX (Unix®)
Apple:  MacOS (Unix®)

La certificación cuesta un pastizal.
Hubo gente que intentó montar un SO según las especificaciones de POSIX y SUS, pero sin pagar la certificación, para ofrecerlo de forma gratuita al mundo:
- BSD (Berkeley Software Distribution): 386-BSD
    El problema es que se atrevieron a decir que era un UNIX®... y AT&T les metió un puro que casi los mata.
    Posteriormente, cuando se resolvieron los litigios interpuestos por AT&T, se usó su código para montar nuevos SO: 
        - FreeBSD
        - NetBSD
        - MacOS X
- GNU (GNU is Not Unix): 
    El problema es que no terminaron de desarrollarlo nunca. Montarón casi todo lo necesario para tener un SO completo... compiladores, interfaz gráfica (gnome), editores de texto (gedit), chess, etc... pero no terminaron de montar el núcleo del SO.
- Por ese entonces, encabronado que no hubiera un SO libre cojonudo, sale un hombrecillo llamado Linus Torvalds, que se pone a montar un kernel de SO desde cero, que se llamó Linux (Linus + Unix).

Como os podéis imaginar Linus se unió a la gente de GNU para montar un SO: GNU/Linux (70%, 30%), que se deistribnuye como compendios de software(las distros): RedHat, Debian, Ubuntu, etc...

El SO GNU/Linux se supone que cumple con las especificaciones POSIX y SUS, pero no está certificado.

El kernel Linux es el Kernel de SO más usado del mundo. Mucho más que el de windows (windows NT)
Hay un SO , que sólo él convierte a Linux en el Kernel de SO más usado del mundo: Android.

---

En el estandar POSIX se define cómo debe ser un sistema de archivos:
/
    bin/
    etc/
    var/
    opt/
    home/
    tmp/

Además, se definen una serie de utilizades(comandos) que todo SO que quiera cumplir con POSIX debe tener:
- mkdir
- cp
- mv
- touch

HDFS, Hadoop Distributed File System, es un sistema de archivos distribuido, que cumple con las especificaciones POSIX.
Y que requiere de un Kernel para su ejecución compatible con POSIX (que tenga los comanditos de antes)

En Windows o montamos el Win_utils... o vamos jodidos para levantas un sistema de archivos HDFS...
Lo cual tampoco es ninguna limitación, porque en Windows no voy a montar un cluster de Hadoop ni de coña.
Va a ir peor .. y encima tendré que pagar pasta por licencias.
Todos los cluster de hadoop van sobre linux... donde no voy a tener ese problema.
Otra cosa es que para jugar en desarrollo, estemos levantando un cluster de hadoop en local en windows. Pero para eso tenemos el win_utils.

A la hora de trabajar con Spark, a no ser que vayamos a trabajar con el sistema de archivos de hadoop, ésto no me supone ninguna limitación en los entornos de desarrollo. Si quiero trabajar con HDFS entonces si necesito montar el win_utils. Si no... no hay problema.

---

Exception in thread "main" java.lang.IllegalAccessError: class org.apache.spark.storage.StorageUtils$ (in unnamed module @0x6e6f2380) cannot access class sun.nio.ch.DirectBuffer (in module java.base) because module java.base does not export sun.nio.ch to unnamed module @0x6e6f2380

En Java 9, cambia la arquitectura de la máquina virtual de JAVA (JIGSAW).
La máquina virtual se modulariza. De forma que al levantar una JVM podemos elegir con qué módulos queremos que se levante.
Adicionalmente, alguno módulos considerados inseguros, se desactivan por defecto.

--add-opens=java.base/java.lang=ALL-UNNAMED 
--add-opens=java.base/java.lang.invoke=ALL-UNNAMED 
--add-opens=java.base/java.lang.reflect=ALL-UNNAMED 
--add-opens=java.base/java.io=ALL-UNNAMED 
--add-opens=java.base/java.net=ALL-UNNAMED 
--add-opens=java.base/java.nio=ALL-UNNAMED 
--add-opens=java.base/java.util=ALL-UNNAMED 
--add-opens=java.base/java.util.concurrent=ALL-UNNAMED 
--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED 
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED 
--add-opens=java.base/sun.nio.cs=ALL-UNNAMED 
--add-opens=java.base/sun.security.action=ALL-UNNAMED 
--add-opens=java.base/sun.util.calendar=ALL-UNNAMED 
--add-opens=java.security.jgss/sun.security.krb5=ALL-UNNAMED

---

Spark particiona datos... y manda cada una de esas particiones a distintos nodos... para que todos hagan el mismo trabajo sobre las distintas particiones.

    Nodo 1  
        1-100M          map1 -> map2 -> filter -> map3 -> reduce

    Nodo 2
        100M-200M       map1 -> map2 -> filter -> map3 -> reduce

    Nodo 3
        200M-300M       map1 -> map2 -> filter -> map3 -> reduce

Apache Storm es otro framework de Big Data, que se usa para procesar streams de datos en tiempo real.
Pero trabaja diferente a Spark. En Apache Storm lo que repartimos entre los nodos no son datos, sino operaciones.

    Nodo1           Nodo2           Nodo3               Nodo4               Nodo5
    1-300M  map1->  1-300M  map2->   1-300M  filter->   1-300M  map3->     1-300M  reduce->

---

# Apache Spark SQL

Es una librería alternativa a SparkCore, que nos permite trabajar de una forma más sencilla con datos ESTRUCTURADOS.

Lo cierto es que a día de hoy el 90% del trabajo que se hace en Spark es con la librería SQL.

Si usamos la librería SQL, usamos la librería SQL y nos olvidamos de la librería CORE.
Podemos pasar datos de una a la otra.. lo cuál no siempre es directo.

Incluso la forma de conectarnos al cluster cambia en la librería SQL.

---

En la práctica, tendré un archivo más o menos grande guardado en:
- Un sitio externo al cluster de Spark
  - Lo tengo que leer de golpe... en un nodo... y luego repartirlo entre los nodos trabajadores
- Dentro del cluster en HDFS
  - El archivo estará particionado... y cada partición almacenada en un nodo del cluster
  - Al leerlo, puedo leer cada partición de forma independiente en cada nodo,
    procesar los datos de esa partición en ese nodo (evitando que se manden por la red)
    y al acabar en cada nodo: 
        - mandar el resultado a un nodo que consolide la información 
        - volver a guardar el resultado en un nuevo archivo particionado... para en el futuro poder volver a leerlo de forma distribuida... y aplicarle otras transformaciones.

Nunca vamos a trabajar con rutas locales en los programas Spark.
Esos programas se ejecutan simultáneamente en todos los nodos del cluster.
Y en cada nodo se tomaría una ruta local diferente.

Lo que trabajamos son con rutas disponibles en red, que todos los nodos del cluster pueden ver.
Habitualmente exportadas mediante HDFS.
    NO TENDREMOS:
        /home/usuario/Documentos/...
        c:\misDocumentos\...
    TENDREMOS:
        hdfs://almacenamiento/usuario/Documentos/...

---

DNIs nacionales... no NIEs ni pasaportes, ni CIF
1-8 números y 1 letra
Qué ocurre con la letra? está al final... y es mayúscula
La letra es una huella del número (un algoritmo de tipo HASH)

    23000001 | 23
            ------------
           1   1000000
           ^
           ^ Este es el dato que me interesa. Entre qué valores está? 0-22
           23 valores diferentes puede tomar... entre 0 y 22

           A este DNI, por tener resto 1 le corresponde la letra R

    Qué es un formato correcto?
         23.000.000-T
         23000000T
         02300000T
          2300000t
          2300000 T
         23.000000-T      NO
         23000000$T       NO
    Uno de los grandes problemas en el banco, es que estamos continuamente recibiendo ficheros 
    cargados de DNIs de distintos proveedores... y cada uno lo envía en un formato distinto.

Queremos hacer un programa Spark que:
- Lea unas personas de un fichero: JSON, CSV... lo que sea
- Valide que el DNI es correcto
- Y lo deje en un determinado formato de mi interés (normalizarlo)
  - ceros delante
  - usar puntos
  - usar separador
Guardar los datos en un fichero... con el formato adecuado... y solo de aquellos registros que tengan un DNI válido
Los datos que vengan con un DNI inválido, los quiero en otro fichero.

    ^^^^

ESCENARIO TIPICO DE USO DE SPARK EN EL BANCO