# Overview

Kafka has many moving pieces, but also has a ton of helpful resources to learn available online. In this homework, your
challenge is to write answers that make sense to you, and most importantly, **in your own words!**
Two of the best skills you can get from this class are to find answers to your questions using any means possible, and to
reword confusing descriptions in a way that makes sense to you. 

### Tips
* You don't need to write novels, just write enough that you feel like you've fully answered the question
* Use the helpful resources that we post next to the questions as a starting point, but carve your own path by searching on Google, YouTube, books in a library, etc to get answers!
* We're here if you need us. Reach out anytime if you want to ask deeper questions about a topic 
* This file is a markdown file. We don't expect you to do any fancy markdown, but you're welcome to format however you like

### Your Challenge
1. Create a new branch for your answers 
2. Complete all of the questions below by writing your answers under each question
3. Commit your changes and push to your forked repository

## Questions
#### What problem does Kafka help solve? Use a specific use case in your answer 
* Helpful resource: [Confluent Motivations and Use Cases](https://youtu.be/BsojaA1XnpM)
#### A: Kafka helps solve faulty machines. For example, data stored on one machine would be completely inaccessable when that machine goes down. Kafka allows users to prevent data loss using replication and more

#### What is Kafka?
* Helpful resource: [Kafka in 6 minutes](https://youtu.be/Ch5VhJzaoaI) 
##### A: Kafka is a messaging system that allows users to send and receive messages between programs, applications, and processes

#### Describe each of the following with an example of how they all fit together: 
 * Topic
   * a topic is a stream of messages (data where messages are stored) consisting of one or more partitions
 * Producer
   * a producer is an application that sends messages/records to a kafka cluster (with a key that specifies the partition)
 * Consumer 
   * a consumer is an application that receives messages (or a topic of messages) from a specific partition
 * Broker
   * a broker is a machine or node on a cluster that stores the topics, messages/records, and partitions
 * Partition
   * a partition is where messages are stored aka a log file and messages are appended to the end

#### Describe Kafka Producers and Consumers
* kafka producers publish messages (with an accompanying key when order matters) the key is usually an id that matches with a partition id, to specify which partition it's going to
* kafka consumers read messages from the kafka cluster. they work as part of a consumer group to read a topic

#### How are consumers and consumer groups different in Kafka? 
* Helpful resource: [Consumers](https://youtu.be/lAdG16KaHLs)
* Helpful resource: [Confluent Consumer Overview](https://youtu.be/Z9g4jMQwog0)
  * consumer groups are different than consumers because consumer groups consist of consumers. each consumer reads from multiple partitions. There cannot be more consumers than partitions.  

#### How are Kafka offsets different than partitions? 
* kafka offsets are different than partitions because they are the messages numeric ordering in a partition. since messages are appended in an order, offsets keep track of the order of the messages to read from 

#### How is data assigned to a specific partition in Kafka? 
* kafka decides where to send the messages on each parition in the brokers/kafka cluster

#### Describe immutability - Is data on a Kafka topic immutable? 
* yes, kafka messages cannot be updated or deleted. so in that sense, they are immutable. But, data can be lost, and hence "deleted" after a certain time period (retention)

#### How is data replicated across brokers in kafka? If you have a replication factor of 3 and 3 brokers, explain how data is spread across brokers
* Helpful resource [Brokers and Replication factors](https://youtu.be/ZOU7PJWZU9w)
* data is replicated across brokers depending on if there is a replication factor. if there is, then the data is replicated to x amount of brokers, x being the replication factor. if x is 3, and you have 3 brokers, the message is sent to a specific partition that is the leader for that message, and the message is replicated on the other two brokers, and these messages are the in sync replicas, aka ISRs.

#### What was the most fascinating aspect of Kafka to you while learning? 
* I really liked learning about the speed at which kafka operates. It's cool knowing that reading and appending messages is how this was built off of, without using search or other complex data structures to create such a powerful tool!


