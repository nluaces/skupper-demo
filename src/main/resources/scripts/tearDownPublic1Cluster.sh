#!/bin/bash

kubectl delete -f src/main/resources/twitter-postgresql-sink.kamelet.yaml
kamel delete twitter-route
skupper delete
