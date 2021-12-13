# Reading Is Good Application

### Architecture

![](../docs/readingisgood.png?raw=true "Title")

### api-gateway:
    api gateway is responsible for delegating routes to related service and also validation token


### user-service:
    responsible for user operations like login register and admin operations


### stock-service :
    responsible order operations(creating order, listing etc), book stock (adding new book
     increasing book stock) and statistic operations (getting users monthly statistic)


To use application; start docker engine on your pc. Then cd to main project folder(/readingisgood)
> mvn clean install

this will create all docker images of microservices. Then simply cd to /readingisgood/docker
and
> docker compose up

when all services up; api-gateway url http://localhost:8081 (or http://host.docker.internal:8081 in windows)
eureka server url is : http://localhost:8761/ or (http://host.docker.internal:8761 in windows)

for postman collection see docs folder.

for auth register with
>curl --location --request POST 'http://localhost:8081/register' --header 'Content-Type: application/json' --data-raw '{ "username":"user", "password":"password"}'

all requests except register and get-token, you need to add "Authorization" returned 
from register or get-token(login). If an api responses with 401, you need to get-token (login) again.

### TODOs;
--Testing.
--Swagger.
--More documentation
--More validation
