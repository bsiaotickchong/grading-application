DROP TABLE IF EXISTS major;
CREATE TABLE major (
  id INT auto_increment PRIMARY KEY,
  name VARCHAR (50) UNIQUE NOT NULL,
);

DROP TABLE IF EXISTS student_type;
CREATE TABLE student_type (
  id INT auto_increment PRIMARY KEY,
  name VARCHAR (50) UNIQUE NOT NULL,
);

DROP TABLE IF EXISTS student;
CREATE TABLE student (
  id INT auto_increment PRIMARY KEY,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  email VARCHAR(50) NOT NULL,
  major_id INT NOT NULL,
  year SMALLINT,
  student_type_id INT NOT NULL,

  FOREIGN KEY (major_id) REFERENCES major(id),
  FOREIGN KEY (student_type_id) REFERENCES student_type(id),
);
