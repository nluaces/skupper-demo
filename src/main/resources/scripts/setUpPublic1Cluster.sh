#!/bin/bash

kamel install
minikube addons enable registry
kubectl apply -f src/main/resources/postgres-sink.kamelet.yaml
kamel run  --property CONSUMER_KEY=your_consumer_key --property CONSUMER_SECRET=your_consumer_secret --property ACCESS_TOKEN=your_access_token --property ACCESS_TOKEN_SECRET=your_access_token_secret src/main/java/TwitterRoute.java
kamel logs twitter-route
