#!/bin/bash

kubectl create secret generic tw-datasource --from-file=src/main/resources/database/datasource.properties
kamel run src/main/java/TelegramRoute.java --dev --build-property quarkus.datasource.camel.db-kind=postgresql  --config secret:tw-datasource -d mvn:io.quarkus:quarkus-jdbc-postgresql -d mvn:org.apache.camel:camel-jackson --resource src/main/resources/config.properties