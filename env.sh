#!/bin/sh

echo "Setting env variables"

export AZ_RESOURCE_GROUP=dream11-group
export AZ_DATABASE_NAME=Dream11
export AZ_LOCATION=westindia
export AZ_MYSQL_USERNAME=dream11
export AZ_MYSQL_PASSWORD=D11#2021
export AZ_LOCAL_IP_ADDRESS=217.100.148.210

export SPRING_DATASOURCE_URL=jdbc:mysql://$AZ_DATABASE_NAME.mysql.database.azure.com:3306/demo?serverTimezone=UTC
export SPRING_DATASOURCE_USERNAME=spring@$AZ_DATABASE_NAME
export SPRING_DATASOURCE_PASSWORD=$AZ_MYSQL_PASSWORD