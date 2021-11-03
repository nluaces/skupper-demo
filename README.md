## Requirements
- Minikube installation. 
- Kamel installation in the clusters that run camel routes.
  ```
  $ minikube addons enable registry
  $ kamel install
  ```
- Twitter developer account
- Telegram bot
- Telegram chat id

### Step 1. Set up kubernetes clusters and skupper
`TBD`

### Step 2. deploy a simple Postgres DB to private Kubernetes cluster

This is a very simple example to show how to create a Postgres database. Note, this is not ready for any production purpose.
Create a Kubernetes Deployment
```
kubectl create -f postgres-configmap.yaml
kubectl create -f postgres-storage.yaml
kubectl create -f postgres-deployment.yaml
kubectl create -f postgres-service.yaml
```
Test the connection

```
kubectl run pg-shell -i --tty --image quay.io/skupper/simple-pg --env="PGUSER=postgresadmin" --env="PGPASSWORD=admin123" --env="PGHOST=$(kubectl get service postgres -o=jsonpath='{.spec.clusterIP}')" -- bash

psql --dbname=postgresdb
```

Create a test database and table
```
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE tw_feedback (
    id uuid DEFAULT uuid_generate_v4 (),
    sigthning VARCHAR(255),
    created TIMESTAMP default CURRENT_TIMESTAMP,
    PRIMARY KEY(id));
INSERT INTO tw_feedback(sigthning) VALUES ('hello');
```

Read the test database and table
```
SELECT * FROM tw_feedback;
```
Resume using `kubectl attach pg-shell -c pg-shell -i -t` command when the pod is running

Expose the database in skupper:
```
skupper expose deployment postgres --address postgres --port 5432 -n private1
``` 

### Step 3. Deploy TwitterRoute and Postgres Sink

Install Camel integrations in the public1 cluster.
```
    $ kubectl apply -f postgres-sink.kamelet.yaml
    $ kamel run TwitterRoute.java
```
To check the camel integration logs:
```
    $ kamel logs twitter-route
```

To delete the TwitterRoute camel integration from the cluster: 
```
$ kamel delete twitter-route
```
### Step 4. Deploy Postgres polling integration 

Create a secret in the cluster with database credentials:
```
 $ kubectl create secret generic tw-datasource --from-file=datasource.properties
```

Run the camel integration
```
 $ kamel run SelectPostgres.java --dev --build-property quarkus.datasource.camel.db-kind=postgresql  --config secret:tw-datasource -d mvn:io.quarkus:quarkus-jdbc-postgresql
```

