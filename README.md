## Requirements
- Minikube installation. 
- Kamel installation in the clusters that run camel routes.
  ```
  $ minikube addons enable registry
  $ kamel install
  ```
- A Twitter developer account
- A Telegram bot
- A Telegram chat id

Configure all the credentials in the `config.properties` file.

### Step 1. Set up skupper in the clusters
`TBD`

### Step 2. deploy a simple Postgres DB to private Kubernetes cluster

This is a very simple example to show how to create a Postgres database. Note, this is not ready for any production purpose.
Create a Kubernetes Deployment
```
kubectl create -f src/main/resources/database/postgres-svc.yaml
```
Test the connection

```bash
kubectl run pg-shell -i --tty --image quay.io/skupper/simple-pg --env="PGUSER=postgresadmin" --env="PGPASSWORD=admin123" --env="PGHOST=$(kubectl get service postgres -o=jsonpath='{.spec.clusterIP}')" -- bash
```
```bash
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
skupper expose deployment postgres --port 5432 -n private1
``` 

### Step 3. Deploy TwitterRoute and Postgres Sink

Install Camel integrations in the public1 cluster.
```
    $ minikube addons enable registry
    $ kamel install
    $ src/main/resources/scripts/setUpPublic1Cluster.sh
```


### Step 4. Deploy Postgres polling - Telegram integration in Public2 cluster 

Run the camel integration
```
    $ minikube addons enable registry
    $ kamel install
    $ src/main/resources/scripts/setUpPublic2Cluster.sh
```

### Step 5. Tear down the environment after the demo

In Private1 Cluster:
`$ src/main/resources/scripts/tearDownPrivate1Cluster.sh`

In Public1 Cluster:
`$ src/main/resources/scripts/tearDownPublic1Cluster.sh`

In Public2 Cluster:
`$ src/main/resources/scripts/tearDownPublic2Cluster.sh`