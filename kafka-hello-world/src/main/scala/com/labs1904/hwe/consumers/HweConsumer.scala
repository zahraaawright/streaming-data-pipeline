package com.labs1904.hwe.consumers
import scala.collection.mutable.ListBuffer
import com.labs1904.hwe.util.Constants._
import com.labs1904.hwe.util.Util
import net.liftweb.json.DefaultFormats
import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

import org.slf4j.LoggerFactory

import java.time.Duration
import java.util.Arrays

object HweConsumer {
  private val logger = LoggerFactory.getLogger(getClass)
  case class EnrichedUser(id: Int, username: String, name: String, email: String, DOB: String, numberAsWord: String, hweDeveloper: String)
  case class listOfRecords(list: List[String])
  val consumerTopic: String = "question-1"
  val producerTopic: String = "question-1-output"

  implicit val formats: DefaultFormats.type = DefaultFormats
  var list = ListBuffer[String]()
  def main(args: Array[String]): Unit = {

    // Create the KafkaConsumer
    val consumerProperties = Util.getConsumerProperties(BOOTSTRAP_SERVER)
    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](consumerProperties)

    // Create the KafkaProducer
    val producerProperties = Util.getProperties(BOOTSTRAP_SERVER)
    val producer = new KafkaProducer[String, String](producerProperties)

    // Subscribe to the topic
    consumer.subscribe(Arrays.asList(consumerTopic))

    case class RawUser(id: Int, username: String, name: String, email: String, DOB: String)
    val me = new RawUser(23, "zahraaawright", "Zahra Wright", "zahra.wright@email.com", "1999-05-09")
    case class EnrichedUser(id: Int, username: String, name: String, email: String, DOB: String, numberAsWord: String, hweDeveloper: String)
    var message2: String = ""

    while ( {
      true
    }) {
      // poll for new data
      val duration: Duration = Duration.ofMillis(100)
      val records: ConsumerRecords[String, String] = consumer.poll(duration)

      //val p_records: ProducerRecord[String, String](producerTopic, message2)

      records.forEach((record: ConsumerRecord[String, String]) => {
        // Retrieve the message from each record
        //val user = new EnrichedUser(23, "zahraaawright", "Zahra Wright", "zahra.wright@email.com", "1999-05-09", 0, "Zahra W")
        val message = record.value()
        logger.info(s"Message Received: $message")
        val thing = record.value
        val split_thing = thing.split("\t")
        val userid = split_thing(0).toInt
        val username = split_thing(1)
        val name = split_thing(2)
        val email = split_thing(3)
        val DOB = split_thing(4)
        val num = Util.mapNumberToWord(userid)
        val myuser = EnrichedUser(userid, username, name, email, DOB, num, "Zahra W")
        message2 += myuser.toString
        list.append(message2+",")
        val new_list = listOfRecords(list.toList)
        // println(list)
//        producer.send(list)
        //println(message2)
      })

      //records.forEach()
    }

    // producer.close()
  }
}