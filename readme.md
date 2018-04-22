# Small REST API test task

## Description

Тестовое задание на тему Java + REST

## Особенности
- Используется база H2, ничего специально инициализировать не надо, структура инициализируется автоматически
- все пути - согласно требованиям
- пример xml на PUT:
```
<?xml version="1.0" encoding="UTF-8"?>
<vacancy><id>1</id><name>New Vacancy</name><salary>1000</salary><experience>No Experience</experience><city>Moscow</city></vacancy>
```
- проверки: name, id - не пустые, salary - не отрицательное, длина строковых полей не больше определенного значения
- в случае ошибки PUT вернет xml со списком ошибок