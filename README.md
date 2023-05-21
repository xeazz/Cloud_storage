# Дипломная работа «Облачное хранилище»
## Цели и задачи
Разработать `REST`-сервис. Сервис должен предоставить `REST`-интерфейс для хранения, загрузки, удаления, скачивания и вывода списка (загруженных ранее) файлов пользователя по [заранее описанной спецификации](https://github.com/netology-code/jd-homeworks/blob/master/diploma/CloudServiceSpecification.yaml).

**ВАЖНО!** Все запросы к сервису должны быть авторизованы.

## Реализация
- Приложение (REST-сервис) разработано с использованием *Spring Boot 2.7.11*
- Реализована авторизация с помощью JWT-Token **Authentication**, **Spring Security**, **Cross-Origin Resource Sharing (CORS)**.
- База данных - **MySQL**
- Сборщик пакетов - **maven**
- Для запуска используется docker-compose
- В качестве **логирования** транзакций используется аннотация `@Slf4j`. На стороне сервера происходит сохранение информации о транзакциях, ошибках и успешных операциях в `logs/application.log`

## Запуск приложения
1. Необходимо собрать `jar`-архив со Spring Boot приложением. Для этого в терминале, в корне проекта необходимо выполнить команду:

   - Для gradle: `./gradlew clean build` (если пишет Permission denied, тогда сначала выполните `chmod +x ./gradlew`).

   - Для maven: `./mvnw clean package` (если пишет Permission denied, тогда сначала выполните `chmod +x ./mvnw`).

2. С помощью *`docker-compose.yaml`* необходимо запустить приложение с помощью команды `docker-compose up -d`.
    Описание портов:

    - Back-end: **8080**
    - Front-end: **8081**
    - Databsae: **3306**


3. "Стартовые" пользователи:
   
   `login: admin@gmail.com, password: admin`

   `login: user1@mail.ru, password: admin`