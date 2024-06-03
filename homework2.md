# Индексы: оптимизация запросов // ДЗ

В БД импортированы тестовые данные из предложенного источника

![Count*](homework/2/count.png)

Проведены нагрузочные тесты для метода /user/search c 10/100/1000 одновременных пользователями.
## 10
![Count*](homework/2/10.png)
## 100
![Count*](homework/2/100.png)
## 1000
![Count*](homework/2/1000.png)

## Индекс
Создан составной индекс на firstname и secondname, в соответствии с форматом поиска:
```sql
create index users_indx_first_second_name on users (firstname text_pattern_ops, secondname text_pattern_ops);
```
EXPLAIN ANALIZE запроса после создания индекса
![Count*](homework/2/explain.png)

Проведены нагрузочные тесты после создания индекса для метода /user/search c 10/100/1000 одновременных пользователями.
## 10
![Count*](homework/2/10i.png)
## 100
![Count*](homework/2/100i.png)
## 1000
![Count*](homework/2/1000i.png)