# user-service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application using the following command:

```shell script
cd user-service
./mvnw clean package
docker compose up --build

```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## How to use the APIs

You can create a user using the following command:
```shell script
curl -X POST 'http://localhost:9090/users' -H 'accept: application/json' -H 'Idempotency-Key: xxxaqqq1' -H 'Content-Type: application/json' -d '{"name": "Mozammal Hossain", "email": "mozammal@gmail.com"}'
```

You can get a user using the following command:
```shell script
curl http://localhost:9090/users/1
```

