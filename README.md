## Используемые технологии

1. Spring Framework
2. Vaadin Framework
3. Gradle
4. JDK 8
5. Jetty HTTP Server

## Сборка
Для сборки необходимо выполнить команду __gradle build__.
Будет создана тестовая БД (H2).

Настройки БД находятся в __gradle.properties__ и при сборке проекта __eko-server__ копируются в файл
 *EkoLab/eko-server/src/main/resources/application-custom.properties*, после чего он имеет следующее содержание:  

        > spring.datasource.url=jdbc:h2:file:C:/work/EkoLab/EkoLab-H2DB;IFEXISTS=TRUE
        > spring.datasource.username=user
        > spring.datasource.password=password
Это параметры конфигурации источника данных, в котором будет вестись разработка и тестирование.
