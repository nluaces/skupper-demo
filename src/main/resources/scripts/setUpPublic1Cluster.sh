#!/bin/bash

kamel install
minikube addons enable registry
kubectl apply -f src/main/resources/postgres-sink.kamelet.yaml
kamel run src/main/java/TwitterRoute.java
kamel logs twitter-route
