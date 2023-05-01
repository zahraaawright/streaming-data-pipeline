# Overview

Similar to the work you did for Kafka, this is your crash course into Spark through different questions. In this homework, your
challenge is to write answers that make sense to you, and most importantly, **in your own words!**
Two of the best skills you can get from this class are to find answers to your questions using any means possible, and to
reword confusing descriptions in a way that makes sense to you. 

### Tips
* You don't need to write novels, just write enough that you feel like you've fully answered the question
* Use the helpful resources that we post next to the questions as a starting point, but carve your own path by searching on Google, YouTube, books in a library, etc to get answers!
* We're here if you need us. Reach out anytime if you want to ask deeper questions about a topic 
* This file is a markdown file. We don't expect you to do any fancy markdown, but you're welcome to format however you like
* Spark By Examples is a great resources to start with - [Spark By Examples](https://sparkbyexamples.com/)

### Your Challenge
1. Create a new branch for your answers 
2. Complete all of the questions below by writing your answers under each question
3. Commit your changes and push to your forked repository

## Questions
#### What problem does Spark help solve? Use a specific use case in your answer 
* Spark is fault-tolerant! When using distributed systems, it has protocols that account for nodes/machines being down
* it also helps to run transformations on large amounts of data in an efficient way across machines
* it improved upon MapReduce!
* Helpful resource: [Apache Spark Use Cases](https://www.toptal.com/spark/introduction-to-apache-spark)
* [Overivew of Apache Spark](https://www.youtube.com/watch?v=znBa13Earms&t=42s)

#### What is Apache Spark?
* Apache Spark is a program that handles how to run code across clusters of machines, it knows how to efficiently process data and can handle errors such as nodes crashing when executing the code 
* Helpful resource: [Spark Overview](https://www.youtube.com/watch?v=ymtq8yjmD9I) 

#### What is distributed data processing? How does it relate to Apache Spark?  
* **distributed data processing** is using multiple machines/nodes to handle large amounts of data - this relates to Spark because it allows for the transformation of large amounts of data on multiple machines
[Apache Spark for Beginners](https://medium.com/@aristo_alex/apache-spark-for-beginners-d3b3791e259e)

#### On the physical side of a spark cluster, you have a driver and executors. Define each and give an example of how they work together to process data
* a **driver** is the controller of the spark appication. it is responsible for asking cluster manager for resources and launching executors
  * it also monitors the successes and failures of the executors and can run on edge node or data node
* an **executor** is a process which runs on a data node inside the cluster. it is responsible for receiving tasks from the driver and executing them on their individual slice of data
  * it also reports back the success and failures of its execution to the driver

#### Define each and explain how they are different from each other 
* RDD (Resilient Distributed Dataset)
  * **RDD** is an immutable distributed collection of elements of your data, partitioned across nodes in your cluster that can be operated in parallel with a low-level API that offers transformations and actions.
    * Spark handles distrbution of data execution to nodes in the cluster
    * Spark UI only referenced RDD
    * closer to scala
    * (trying to discourage RDD use)
* DataFrame
  * A **DataFrame** is not type safe, and is a structure that holds data. if you reference a column in the code that is not in a DF, there will only be an error during runtime
  * availible in Scala, Python, and Java
  * Strongly typed RDD
* DataSet
  * A **DataSet** is similar to a dataframe in that it is a structure that holds data (like a table), however, it is strongly typed. The code will not compile if you use a column that is not a part of the data set
  * you can save a lot of heartache with DataSets without having the run the code to find errors.
  * Only availible in Java and Scala

#### What is a spark transformation?
* A Spark transformation is any operation that returns a dataframe/dataset
[Spark By Examples-Transformations](https://sparkbyexamples.com/apache-spark-rdd/spark-rdd-transformations/)

#### What is a spark action? How do actions differ from transformations? 
* a spark action is any operation that that DOESN'T return a dataframe/dataset
* The two differ because transformations only execute in spark once an action is called, such as "print"

#### What is a partition in spark? Why would you ever need to repartition? 
* a way to split the data into multiple partitions so that you can execute transformations on multiple partitions in parallel which allows completing the job faster.
* repartition() is used to increase or decrease the RDD, DataFrame, Dataset partitions whereas the coalesce() is used to only decrease the number of partitions in an efficient way.
[Spark Partitioning](https://sparkbyexamples.com/spark/spark-repartition-vs-coalesce/)

#### What was the most fascinating aspect of Spark to you while learning? 
* The most fascinating aspect of Spark was that it needs an action so that transformations will execute, and it will find the most efficient way to get the result of all the transformations! I'm curious as to how this works on the backend!!
