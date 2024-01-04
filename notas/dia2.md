
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