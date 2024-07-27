--DROP TABLE person IF EXISTS;
CREATE TABLE IF NOT EXISTS person(
		id BIGINT PRIMARY KEY AUTO_INCREMENT,
        user_name VARCHAR(50) NOT NULL,
        password VARCHAR(50) NOT NULL,
        email VARCHAR(256) NOT NULL,
        role ENUM('admin','student') NOT NULL
);

--DROP TABLE token IF EXISTS;
CREATE TABLE IF NOT EXISTS token(
		id BIGINT PRIMARY KEY AUTO_INCREMENT,
		token VARCHAR(50) NOT NULL,
        user_id int not null ,
		foreign key (user_id) references person(id)
);
--DROP TABLE IF EXISTS student;
CREATE TABLE IF NOT EXISTS student(
		id BIGINT PRIMARY KEY AUTO_INCREMENT,
        first_name VARCHAR(50) NOT NULL,
        last_name VARCHAR(50) NOT NULL,
        email VARCHAR(256) NOT NULL,
        date_Of_Birth VARCHAR(50) NOT NULL
);