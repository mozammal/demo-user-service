# Demo user service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Development Environment
This project was developed and tested on the following environment:

* OS/Hardware: MacBook Pro M4 (macOS)
* Java Runtime: JDK 17 or higher
* Build Tool: Maven
* Deployment: Docker

## Running the application

You can run your application using the following command:

```shell script
git clone https://github.com/mozammal/demo-user-service.git
cd demo-user-service
chmod +x mvnw
./mvnw clean package
docker compose up --build

```

## How to use the APIs

You can create a user using the following command:
```shell script
curl -X POST 'http://localhost:9090/users' -H 'accept: application/json' -H 'Idempotency-Key: xxxaqqq1' -H 'Content-Type: application/json' -d '{"name": "Mozammal Hossain", "email": "mozammal@gmail.com"}'
```

You can get a user using the following command:
```shell script
curl http://localhost:9090/users/1
```

