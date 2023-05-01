package com.labs1904.hwe

import org.apache.log4j.Logger
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions._

object WordCountBatchApp {
  lazy val logger: Logger = Logger.getLogger(this.getClass)
  val jobName = "WordCountBatchApp"

  def main(args: Array[String]): Unit = {
    logger.info(s"$jobName starting...")
    try {
      val spark = SparkSession.builder()
        .appName(jobName)
        .config("spark.sql.shuffle.partitions", "3")
        .master("local[*]")
        .getOrCreate()
      import spark.implicits._
      //option + return (buttons to generate type)
      val sentences: Dataset[String] = spark.read.csv("src/main/resources/sentences.txt").as[String]
      //sentences.printSchema
      //sentences.show(truncate = false) //shows the RDD/DataFrame/DataSet

      val words = sentences.flatMap(word => splitSentenceIntoWords(word.toLowerCase))
      val wordCount1 = words.withColumnRenamed("value", "Words").groupBy("Words").count()
      val wordCount = wordCount1.sort(col("count").desc)
      //val wordCount = words.groupBy("value")
      //words.groupBy("value")
      wordCount.printSchema()
      wordCount.show(truncate = false)



      // TODO: implement me
      //Update the application to output each word and the number of times it occurs, sorted by count descending. Output:
      //val counts = ???

      //counts.foreach(wordCount=>println(wordCount))
    } catch {
      case e: Exception => logger.error(s"$jobName error in main", e)
    }
  }

  // TODO: implement this function
  // HINT: you may have done this before in Scala practice...
  //takes each sentence and splits it into words
  def splitSentenceIntoWords(sentence: String): Array[String] = {
    sentence.split(" ")
  }

}
