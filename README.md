# Projeto básico de CRUD

## Usando as seguintes dependências

*   H2(in-disk), JPA, WEB, DEV, QueryDSL, JWT, Security

* JDK 11, Maven

**.  Inicializar**

+  ./mvnw spring-boot:run

**. Processo de autenticação**

+ Acessos de ADMIN, usar *admin* e senha *adminpw*
+ Acessos de USER, usar *user* e senha *userpw*

<code>

curl --location --request POST 'http://localhost:8080/api/authenticate' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username":"admin",
    "password" : "adminpw"
}'

</code>


O app disponibilizado nesse endereço <http://localhost:8080>.

## Explore as APIs

A aplicação define as APIs abaixo.

    GET /api/product
    POST /api/product
    GET /api/product/querydsl?search=propriedade:valor      Ex.: name:car
    GET /api/product/{Id}   -- ADMIN --
    PUT /api/product/
    DELETE /api/product/{Id}

** Coleção POSTMAN disponibilizada no repositório
