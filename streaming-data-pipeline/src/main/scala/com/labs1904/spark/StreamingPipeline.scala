package com.labs1904.spark

import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.types.StructType
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Get, Put, Scan, Delete, Table}
import org.apache.hadoop.hbase.util.Bytes



/**
 * Spark Structured Streaming app
 *
 */
case class Reviews(marketplace: String, customer_id: Int, review_id: String, product_id: String, product_parent: Int,
                   product_title: String, product_category: String, star_rating: Int, helpful_votes: Int, total_votes: Int,
                   vine: String, verified_purchase: String, review_headline: String, review_body: String, review_date: String)
case class ReviewUser(marketplace: String, customer_id: Int, review_id: String, product_id: String, product_parent: Int,
                      product_title: String, product_category: String, star_rating: Int, helpful_votes: Int, total_votes: Int,
                      vine: String, verified_purchase: String, review_headline: String, review_body: String, review_date: String,
                      user_name: String, name: String, sex: String, DOB: String, mail: String)

//hdfs://hbase01.labs1904.com:8020/user/
object StreamingPipeline {
  lazy val logger: Logger = Logger.getLogger(this.getClass)
  val jobName = "StreamingPipeline"
  val hdfsUrl = "hdfs://hbase01.labs1904.com:8020"
  val bootstrapServers = "b-3-public.hwekafkacluster.6d7yau.c16.kafka.us-east-1.amazonaws.com:9196,b-2-public.hwekafkacluster.6d7yau.c16.kafka.us-east-1.amazonaws.com:9196,b-1-public.hwekafkacluster.6d7yau.c16.kafka.us-east-1.amazonaws.com:9196,"
  val username = "1904labs"
  val password = "1904labs"
  val Topic: String = "reviews"
  val junkTopic: String = "reviews-and-junk"
  val hdfsUsername = "zwright" // TODO: set this to your handle

  //Use this for Windows
  //val trustStore: String = "src\\main\\resources\\kafka.client.truststore.jks"
  //Use this for Mac
  val trustStore: String = "src/main/resources/kafka.client.truststore.jks"

  def main(args: Array[String]): Unit = {
    try {
      val spark = SparkSession.builder()
        .config("spark.sql.shuffle.partitions", "3")
//        .config("spark.hadoop.dfs.client.use.datanode.hostname", "true")
//        .config("spark.hadoop.fs.defaultFS", hdfsUrl)
        .appName(jobName)
        .master("local[*]")
        .getOrCreate()

      import spark.implicits._
      val junk = spark
        .readStream
        .format("kafka")
        .option("kafka.bootstrap.servers", bootstrapServers)
        .option("subscribe", junkTopic)
        .option("startingOffsets", "earliest")
        .option("maxOffsetsPerTrigger", "20")
        .option("startingOffsets", "earliest")
        .option("kafka.security.protocol", "SASL_SSL")
        .option("kafka.sasl.mechanism", "SCRAM-SHA-512")
        .option("kafka.ssl.truststore.location", trustStore)
        .option("kafka.sasl.jaas.config", getScramAuthString(username, password))
        .load()
        .selectExpr("CAST(value AS STRING)").as[String]

      val ds = spark
        .readStream
        .format("kafka")
        .option("kafka.bootstrap.servers", bootstrapServers)
        .option("subscribe", Topic)
        .option("startingOffsets", "earliest")
        .option("maxOffsetsPerTrigger", "20")
        .option("startingOffsets","earliest")
        .option("kafka.security.protocol", "SASL_SSL")
        .option("kafka.sasl.mechanism", "SCRAM-SHA-512")
        .option("kafka.ssl.truststore.location", trustStore)
        .option("kafka.sasl.jaas.config", getScramAuthString(username, password))
        .load()
        .selectExpr("CAST(value AS STRING)").as[String]

      // TODO: implement logic here
      // TODO: Parse each message from the "reviews" topic into a Scala case class.
      val result = ds
      val reviews = result.map(x => stringToReview(x))
      val regReviews = junk.map(x => junkToReview(x))


      // Write output to console
//      val query1 = reviews.writeStream
//        .outputMode(OutputMode.Append())
//        .format("console")
//        .option("truncate", false)
//        .trigger(Trigger.ProcessingTime("5 seconds"))
//        .start()
      //Construct a HBase get request for every review message. The customer_id corresponds to a HBase rowkey.

      val review_ids = reviews.mapPartitions(partition => {
        val conf = HBaseConfiguration.create()
        conf.set("hbase.zookeeper.quorum", "hbase01.labs1904.com:2181")
        val connection = ConnectionFactory.createConnection(conf)
        val table = connection.getTable(TableName.valueOf("zwright:users"))
        //customer id is the row key
        val iter = partition.map(review => {
          val id = review.customer_id
          val get = new Get(Bytes.toBytes(id.toString)).addFamily(Bytes.toBytes("f1"))
          val result = table.get(get)
          val user = ReviewUser(review.marketplace, review.customer_id, review.review_id, review.product_id, review.product_parent,
            review.product_title, review.product_category, review.star_rating, review.helpful_votes, review.total_votes,
            review.vine, review.verified_purchase, review.review_headline, review.review_body, review.review_date,
            Bytes.toString(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("username"))),
            Bytes.toString(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"))),
            Bytes.toString(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("sex"))),
            Bytes.toString(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("birthdate"))),
            Bytes.toString(result.getValue(Bytes.toBytes("f1"), Bytes.toBytes("mail"))))
          user
        }).toList.iterator
        connection.close()
        iter
      })


      val query2 = junk.writeStream
        .outputMode(OutputMode.Append())
        .format("console")
        .option("truncate", false)
        //.option("checkpointLocation", s"/user/zwright/reviews_checkpoint")
        .trigger(Trigger.ProcessingTime("5 seconds"))
        .start()
       //Write output to HDFS

//      val query3 = review_ids.writeStream
//        .outputMode(OutputMode.Append())
//        .format("csv")
//        .option("delimiter", "\t")
//        .option("path", s"/user/zwright/reviews_csv")
//        .option("checkpointLocation", s"/user/zwright/reviews_checkpoint")
//        .partitionBy("star_rating")
//        .trigger(Trigger.ProcessingTime("5 seconds"))
//        .start()
      query2.awaitTermination()
    } catch {
      case e: Exception => logger.error(s"$jobName error in main", e)
    }
  }

  def getScramAuthString(username: String, password: String) = {
    s"""org.apache.kafka.common.security.scram.ScramLoginModule required
   username=\"$username\"
   password=\"$password\";"""
  }
  def junkToReview(jor: String): Int = {
    //TODO: fix
    0
  }
  def stringToReview(review: String): Reviews = {
    val reviewArray = review.split("\t")
    val marketplace: String = reviewArray(0)
    val customer_id: String = reviewArray(1)
    val review_id: String = reviewArray(2)
    val product_id: String = reviewArray(3)
    val product_parent: String = reviewArray(4)
    val product_title: String = reviewArray(5)
    val product_category: String = reviewArray(6)
    val star_rating: String = reviewArray(7)
    val helpful_votes: String = reviewArray(8)
    val total_votes: String = reviewArray(9)
    val vine: String = reviewArray(10)
    val verified_purchase: String = reviewArray(11)
    val review_headline: String = reviewArray(12)
    val review_body: String = reviewArray(13)
    val review_date: String = reviewArray(14)
    val rev = Reviews(marketplace, customer_id.toInt, review_id, product_id, product_parent.toInt, product_title, product_category, star_rating.toInt, helpful_votes.toInt, total_votes.toInt, vine, verified_purchase, review_headline, review_body, review_date)
    return rev
  }
}
