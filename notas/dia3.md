# Uso de Apache Spark

En el banco se usa Spark porque trabajamos con grandes volúmenes de datos? NO
En el banco se usa Spark porque tenemos datos que se producen a gran velocidad? NO
En el banco usamos Spark porque tenemos datos que deben ser consumidos en una ventana muy pequeña de tiempo? NO

Porqué usamos Apache Spark en el banco?... o en muchas otras empresas?

Apache Spark es la única opción para montar ETLs customizadas/programáticamente? Ni de coña:

- Spring iOC (inyección de dependencias) / Java
  + módulos adicionales: Spring Batch -> Se usa para montar ETLs

Apache Spark me ofrece FLEXIBILIDAD en el entorno de producción que voy a usar para procesar los datos.

Yo voy a montar un programa, que por abajo (aunque lo monte con sintaxis alternativas) trabaja con map-reduce, y que por tanto es altamente paralelizable. 

Y podré ejecutar ese mismo programa en un cluster de 2 nodos... o de 200 nodos.
Si tengo más nodos... menos tiempo tardará en ejecutarse mi programa.
Si tengo menos nodos... más tiempo tardará en ejecutarse mi programa.

Pero a mi me la trae al fresco. Yo no tengo que preocuparme de cuántos nodos tengo en mi cluster. Yo sólo tengo que preocuparme de que mi programa funcione.

Ese programa se ejecutará por la noche.... junto con otros 850 programas.
Y ... por el día? no por el día no.... los ejecutamos por la noche... YA !
Y... las máquinas donde se ejecuta aquello, qué hacen por el día? Pues nada... están paradas.
Ya... y voy a tener 50-200-3 máquinas paradas por el día? VOY A COGERLAS BAJO DEMANDA... 
se las pediré a un proveedor de cloud... y cuando termine de ejecutar mis programas... las devolveré.
Y cuántas máquinas cojo? Pues las que necesite para que mis programas se ejecuten en el tiempo que necesito.
Es decir, cuántos datos han venido hoy? 10M -> 2 máquinas
                                        50M -> 10 máquinas

---

## Entorno de producción on-premise / cloud

- Cabina de almacenamiento que se conecta por fibra óptica a los servidores
- Dentro de esa cabina tendrás VOLUMENES DE DATOS (como si fueran capetas de tu HDD)
- Cada volumen se identifica por un PATH
- Cuando arranco un servidor le enchufo un volumen
                     ^
                     Normalmente es un servidor virtual

Por abajo tendré una máquina con 96 cores y 4TB de RAM 

----
# Nuevo proyectito

Leer fichero de personas
 Y quedarme con las personas con DNI correcto y mayores de edad. ***
 Las que no tengan dni correcto a un fichero parquet
 Las que no sean mayores de edad pero tengan un DNI correcto a otro fichero parquet
 Las que si sean buenas ***:
    Leo el fichero de códigos postales
        ES UN CSV
            conexion.read()
                    .option("sep", ";")
                    .option("header", "true")
                    .csv("path")
    Y añado la información de este fichero a cada persona
        Y guardo el resultado en un fichero parquet
        Siempre y cuando la persona tenga un cp que exista en mi fichero de códigos postales
            Si no... a otro fichero parquet

La idea es que tendremos 2 datasets:
    - Personas con DNI correcto y mayores de edad
    - CPs

Y quiero hacer un JOIN de esos datasets (igual que si fueran tablas de una BBDD relacional)

---

# Consideraciones con respecto a los JOIN

Spark NO ES UNA BBDD RELACIONAL.

Esta guay que podamos hacer algunos joins para ENRIQUECER INFORMACION, antes de su almacenamiento.
Pero cuidado con los joins que hacemos.

Spark NO ESTA PREPARADO para juntar 2 tablas de 10M de registros.
Si tengo esta necesidad, cargo los datos de forma independiente en una BBDD y hago el JOIN allí.

Repito, esto sirve para enriquecer datos (LOOKUPs sobre una tabla pequeña: 100M datos) antes de su almacenamiento.
Está bien si tengo una tabla de CP, con 20.000 registros, y quiero añadir esa información a 10M de personas.
Pero tener en cuenta que al menos 1 de las tablas ha de ser distribuida a todos los nodos del cluster:

    10M de personas y particionar ese conjunto de datos: 10 particiones de 1M de personas

    Cada partición la mando a 1 nodo del cluster... tengo la suerte de tener un cluster de 10 nodos.

    Pero cada uno de esos nodos puede tener personas con cualquier CP.
    Por lo tanto todos ellos necesitan tener la tabla de CP en memoria.... y necesit6o que entre en memoria para poder hacer la operación.

    ESTE ES EL USO de los joins en Spark.... REPITO: Y NO EL DE HACER UN JOIN DE 2 TABLAS DE 10M DE REGISTROS.

Estamos hablando en GENERICO de un tipo de programas llamados ETLs (Extract, Transform, Load)
    - Extraer datos de una fuente
    - Transformarlos
    - Cargarlos en otra fuente

Spark se está encargando de la parte de transformación.

Pero... digo GENERICO porque en el mundo real, hay muchas variantes de estos tipos de programas:
    - ETLs
    - ETLTs
    - TEL
    - TETL

En ocasiones hago una transformación (mediante una query SQL) en una BBDD, saco los datos resultantes y los cargo donde sea (fichero parquet)

En ocasiones hago una transformación (mediante una query SQL) en una BBDD, saco los datos resultantes, los vuelvo a transformar (con Spark) y los cargo donde sea (fichero parquet)

En ocasiones saco los datos de una BBDD, los transformo con Spark, y los cargo en otra BBDD, y los transformo de nuevo allí (QUERY)

