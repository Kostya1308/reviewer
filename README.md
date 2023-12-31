# reviewer

Сервис для проверки тестовых заданий

---

## Технологический стек проекта

* Java 21;
* Spring Boot 3.2.0;
* Gradle 8.4;
* Lombok;
* OpenCSV;
* Apache Tika.

**Зависимости от сервисов**

* Сервис оповещения в Telegram

---

## Сборка, запуск, тесты

**Сборка**

* ./gradlew clean build

**Запуск тестов**

* ./gradlew test

**Запуск приложения**

* ./gradlew bootRun -DBRANCH_NAME='feature/core' -DRECEIVED_TASKS_PATH='./csv/received/core' -DCORRECT_TASKS_PATH='./csv/correct/core'

---

## ENV

* BRANCH_NAME - Наименование git-ветки проекта тестируемого;
* RECEIVED_TASKS_PATH - Путь к файлам, сформированным посредством запуска проекта тестируемого;
* CORRECT_TASKS_PATH - Путь к зараннее сформированным корректным файлам;
