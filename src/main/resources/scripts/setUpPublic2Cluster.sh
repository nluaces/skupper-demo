#!/bin/bash

kamel install
minikube addons enable registry
kubectl create secret generic tw-datasource --from-file=src/main/resources/database/datasource.properties
kamel run --property AUTHORIZATION_TOKEN=your_authorization_token --property CHAT_ID=your_chat_id src/main/java/TelegramRoute.java --dev --build-property quarkus.datasource.camel.db-kind=postgresql  --config secret:tw-datasource -d mvn:io.quarkus:quarkus-jdbc-postgresql -d mvn:org.apache.camel:camel-jackson