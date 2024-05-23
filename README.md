# OTUS
## Highload Architect

1. Запустить команды

``` 
docker image build -t otus-network:latest .
docker-compose up -d
```

2. Сваггер доступен по адресу: http://127.0.0.1:8080/swagger-ui/index.html
3. Postman-коллекция в файле postman_collection.json

**Реализованы методы:**
* /login
* /user/register
* /user/get/{id}