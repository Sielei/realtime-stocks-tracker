# Realtime Stock Price Tracker Challenge

The Realtime Stock Tracker is a challenge to create an application that fetches realtime stock data from 
Yahoo finance and publish them to kafka then display the realtime prices and historical data on a chart.
The challenge was initially meant for python developers, but I wanted to try the same in Java and see how it goes.

## Challenge Requirements

Create an application consists of three main components:

1). Producer: This component fetches real-time stock prices from Yahoo Finance and publishes them to a Kafka topic.

2). Consumer: The consumer subscribes to the Kafka topic and receives real-time stock price updates.

3). Web Application: create a simple web application using Flask to display historical and real-time stock price charts


## Project Dependencies
* [Java 17](https://openjdk.org/projects/jdk/17/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Kafka](https://kafka.apache.org/): Docker compose file provided
* [PostgreSQL](https://www.postgresql.org/)
* [CanvasJS](https://canvasjs.com/) charts