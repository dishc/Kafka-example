# Kafka Banking Transactions

### Description

Test project for Kafka produce-consume communication

## How to deploy
1. Use docker for all external services
2. Define proper credentials in <b>.env</b> file
3. Run <b>docker-compose up -d</b> to download kafka, postgres and zookeeper(for kafka)
4. Run this springBoot app using the same .env file as env props source