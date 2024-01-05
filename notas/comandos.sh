bin/spark-class org.apache.spark.deploy.master.Master  --help
bin/spark-class org.apache.spark.deploy.master.Master
bin/spark-class org.apache.spark.deploy.worker.Worker --help 
bin/spark-class org.apache.spark.deploy.worker.Worker spark://172.31.46.205:7077
bin/spark-submit --master spark://172.31.46.205:7077 --class com.curso.CalcularPI curso.jar


