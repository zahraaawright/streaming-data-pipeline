package com.labs1904.hwe.producers

import com.labs1904.hwe.consumers
import com.labs1904.hwe.consumers.HweConsumer
import com.labs1904.hwe.util.Constants._
import com.labs1904.hwe.util.Util
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object SimpleProducer {
  val Topic: String = "question-1-output"

  def main(args: Array[String]): Unit = {
    // Create Kafka Producer
    val properties = Util.getProperties(BOOTSTRAP_SERVER)
    val producer = new KafkaProducer[String, String](properties)
    val messageToSend = HweConsumer.list.toString()
    val myuser = HweConsumer.EnrichedUser(13, "aaronkelley", "Steven Howard MD", "vaughnbrandy@hotmail.com", "1958-06-15", Util.mapNumberToWord(13), "Zahra W")

    //while(true){
      val record = new ProducerRecord[String, String](Topic, myuser.toString)
      println(record.value)
      producer.send(record)
    //}




    producer.close()
  }


}
