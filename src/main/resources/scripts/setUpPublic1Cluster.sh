#!/bin/bash

kubectl apply -f src/main/resources/twitter-postgresql-sink.kamelet.yaml
kamel run  src/main/java/TwitterRoute.java --resource file:src/main/resources/config.properties
kamel logs twitter-route
