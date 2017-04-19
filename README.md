## Подготовка к запуску
Перед запуском, необходимо в директории *EkoLab/eko-server/src/main/resources* 
создать файл application-custom.properties  со следующим содержанием:  

        > spring.datasource.url=jdbc:h2:file:C:/work/EkoLab/EkoLab-H2DB;IFEXISTS=TRUE
        > spring.datasource.username=user
        > spring.datasource.password=password
переопределив параметры конфигурации источника данных, 
в котором будет вестись разработка и тестирование.