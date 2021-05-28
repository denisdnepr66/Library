# Library
Spring Boot Rest API application 

## Instalation
Open terminal and clone the directory
```https://github.com/denisdnepr66/Library.git```

Open project with ide

## Database

The application uses MySQL Database, in order to create in and run in Docker:
1. Open terminal and write following
```docker run  --name ms -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password mysql```
2. Open new terminal
```docker exec -it ms bash```
3. Connect to MySQL server
```mysql -u root -ppassword```
4. Create database called library
```create database library;```
5. Go to this database
```use database library;```
6. Create table users and insert first librarian there

```
create table users (
    user_id bigint not null auto_increment,
    email varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255),
    role varchar(255),
    status varchar(255),
    primary key (user_id)
 ) engine=InnoDB;
 insert into users values('1', 'libararian@email.com', 'Denys', 'Shyshliannykov', '$2y$12$fYjVQP4rG5wJvQxhf6zk8.5ZhdAWmgl9rGpnrrfpTqAWaHX2A4aNa', 'LIBRARIAN', 'ACTIVE');
 ```
