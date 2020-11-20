Some EndPoints
--------------

Query and Ordering explicitly
-----------------------------
http://localhost:8080/api/shows/q/RATING/DESC?filter=language:english&filter=genres:comedy-drama-family
http://localhost:8080/api/shows/q/CHANNEL/DESC?filter=language:english&filter=genres:comedy-drama-family
http://localhost:8080/api/shows/q/GENRE/DESC?filter=language:spanish

Query and Ordering(ASC)
-----------------------
http://localhost:8080/api/shows/q/RATING?filter=language:english&filter=genres:comedy-drama-family
http://localhost:8080/api/shows/q/CHANNEL?filter=language:english&filter=genres:comedy-drama-family
http://localhost:8080/api/shows/q/GENRE?filter=language:spanish

(qp)Query, Pagination and Ordering
----------------------------------
http://localhost:8080/api/shows/qp/5/3?filter=language:english&filter=keywords:the
http://localhost:8080/api/shows/qp/5/3/CHANNEL?filter=language:english&filter=keywords:the
http://localhost:8080/api/shows/qp/5/3/CHANNEL/DESC?filter=language:english&filter=keywords:the
http://localhost:8080/api/shows/qp/5/3/RATING/DESC?filter=language:english&filter=keywords:the


Database Inserts and Query
--------------------------
The database is created when the program run

spring.datasource.url=jdbc:sqlserver://localhost;databaseName=springbootdb
spring.datasource.username=sa
spring.datasource.password=Z2Z*bbv1gnP
spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.jpa.show-sql=true
spring.jpa.hibernate.dialect=org.hibernate.dialect.SQLServer2012Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

INSERT INTO user_tbl (firstname, lastname, username, password, rol, age) VALUES ('Alex','Knr', 'alex123','$2a$04$4vwa/ugGbBVDvbWaKUVZBuJbjyQyj6tqntjSmG8q.hi97.xSdhj/2', 'admin', 33);
INSERT INTO user_tbl (firstname, lastname, username, password, rol, age)  VALUES ('Tom', 'Asr', 'tom234', '$2a$04$QED4arFwM1AtQWkR3JkQx.hXxeAk/G45NiRd3Q4ElgZdzGYCYKZOW', 'user', 23);
INSERT INTO user_tbl (firstname, lastname, username, password, rol, age)  VALUES ('Adam', 'Psr', 'adam', '$2a$04$WeT1SvJaGjmvQj34QG8VgO9qdXecKOYKEDZtCPeeIBSTxxEhazNla', 'user', 45);

SELECT * FROM user_tbl;

Log Path
--------
C:\Logs\log.txt

BackEnd
-------
C:\Projects\tvmaze

FrontEnd (ng serve)
-------------------
C:\Projects\tvmaze\src\main\webapp

Shortcut de Chrome
------------------
"C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" --disable-web-security --user-data-dir="C:/data"

Youtube Video
-------------
https://youtu.be/CXpyWFIMS7Q
