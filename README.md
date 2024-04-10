# Realtime Stock Price Tracker

## Requirements
The application consists of three main components:

1). Producer: This component fetches real-time stock prices from Yahoo Finance and publishes them to a Kafka topic.

2). Consumer: The consumer subscribes to the Kafka topic and receives real-time stock price updates.

3). Web Application: create a simple web application using Flask to display historical and real-time stock price charts