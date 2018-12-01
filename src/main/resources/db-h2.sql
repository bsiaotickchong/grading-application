DROP TABLE IF EXISTS major;
CREATE TABLE major (
  id INT auto_increment PRIMARY KEY,
  name VARCHAR (50) UNIQUE NOT NULL,
);

INSERT INTO major (name) VALUES
    ('Computer Science'),
    ('Computer Engineering'),
    ('Mechanical Engineering'),
    ('Electrical Engineering'),
    ('Undecided')
;

DROP TABLE IF EXISTS student_type;
CREATE TABLE student_type (
  id INT auto_increment PRIMARY KEY,
  name VARCHAR (50) UNIQUE NOT NULL,
);

INSERT INTO student_type (name) VALUES
    ('Undergraduate'),
    ('Graduate')
;

DROP TABLE IF EXISTS student;
CREATE TABLE student (
  id INT auto_increment PRIMARY KEY,
  first_name VARCHAR(50) NOT NULL,
  middle_initial VARCHAR(1),
  last_name VARCHAR(50) NOT NULL,
  email VARCHAR(50),
  major_id INT,
  year SMALLINT,
  student_type_id INT NOT NULL,

  FOREIGN KEY (major_id) REFERENCES major(id),
  FOREIGN KEY (student_type_id) REFERENCES student_type(id),
);

DROP TABLE IF EXISTS time_of_year;
CREATE TABLE time_of_year (
  id INT auto_increment PRIMARY KEY,
  year SMALLINT NOT NULL,
  semester ENUM('Spring', 'Summer 1', 'Summer Twelve-Week', 'Summer 2', 'Fall') NOT NULL,
);

INSERT INTO time_of_year (semester, year) VALUES
    ('Fall', 2018),
    ('Spring', 2019),
    ('Summer 1', 2019),
;

DROP TABLE IF EXISTS course;
CREATE TABLE course (
  id INT auto_increment PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  time_of_year_id INT NOT NULL,
  description VARCHAR(255) NOT NULL,

  FOREIGN KEY (time_of_year_id) REFERENCES time_of_year(id),
);

DROP TABLE IF EXISTS enrollment;
CREATE TABLE enrollment (
  id INT auto_increment PRIMARY KEY,
  course_id INT NOT NULL,
  student_id INT NOT NULL,
  date_of_enrollment DATE DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT enrollment_course_id FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
  FOREIGN KEY (student_id) REFERENCES student(id),
);

DROP TABLE IF EXISTS category;
CREATE TABLE category (
  id INT auto_increment PRIMARY KEY,
  course_id INT NOT NULL,
  name VARCHAR(50) NOT NULL,

  CONSTRAINT category_course_id FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
);

DROP TABLE IF EXISTS assignment;
CREATE TABLE assignment (
  id INT auto_increment PRIMARY KEY,
  category_id INT NOT NULL,
  name VARCHAR(50) NOT NULL,
  extra_credit BOOLEAN DEFAULT false,

  CONSTRAINT assignment_category_id FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
);

DROP TABLE IF EXISTS student_grade;
CREATE TABLE student_grade (
  id INT auto_increment PRIMARY KEY,
  student_id INT NOT NULL,
  assignment_id INT NOT NULL,
  grade DECIMAL DEFAULT 0 NOT NULL,
  exception_flag BOOLEAN DEFAULT FALSE NOT NULL,
  note_text VARCHAR(255),

  FOREIGN KEY (student_id) REFERENCES student(id),
  CONSTRAINT student_grade_assignment_id FOREIGN KEY (assignment_id) REFERENCES assignment(id) ON DELETE CASCADE,
);

DROP TABLE IF EXISTS assignment_weight;
CREATE TABLE assignment_weight (
  id INT auto_increment PRIMARY KEY,
  assignment_id INT NOT NULL,
  student_type_id INT NOT NULL,
  max_grade DECIMAL DEFAULT 100 NOT NULL,
  weight_percent SMALLINT DEFAULT 0 NOT NULL,

  CONSTRAINT assignment_weight_assignment_id FOREIGN KEY (assignment_id) REFERENCES assignment(id) ON DELETE CASCADE,
  FOREIGN KEY (student_type_id) REFERENCES student_type(id),
);

DROP TABLE IF EXISTS note;
CREATE TABLE note (
  id INT auto_increment PRIMARY KEY,
  course_id INT NOT NULL,
  student_id INT NOT NULL,
  note_text VARCHAR(255) NOT NULL,

  CONSTRAINT note_course_id FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
  FOREIGN KEY (student_id) REFERENCES student(id),
);

DROP TABLE IF EXISTS grade_bin;
CREATE TABLE grade_bin (
  id INT auto_increment PRIMARY KEY,
  course_id INT NOT NULL,
  student_type_id INT NOT NULL,
  a INT         DEFAULT 93 NOT NULL,
  a_minus INT   DEFAULT 90 NOT NULL,
  b_plus INT    DEFAULT 87 NOT NULL,
  b INT         DEFAULT 83 NOT NULL,
  b_minus INT   DEFAULT 80 NOT NULL,
  c_plus INT    DEFAULT 77 NOT NULL,
  c INT         DEFAULT 73 NOT NULL,
  c_minus INT   DEFAULT 70 NOT NULL,
  d_plus INT    DEFAULT 67 NOT NULL,
  d INT         DEFAULT 63 NOT NULL,
  d_minus INT   DEFAULT 60 NOT NULL,

  CONSTRAINT grade_bin_course_id FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
  FOREIGN KEY (student_type_id) REFERENCES student_type(id),
);

DROP TABLE IF EXISTS assignment_weight_exception;
CREATE TABLE assignment_weight_exception (
  id INT auto_increment PRIMARY KEY,
  student_id INT NOT NULL,
  assignment_id INT NOT NULL,
  weight_percent SMALLINT NOT NULL,

  FOREIGN KEY (student_id) REFERENCES student(id),
  CONSTRAINT assignment_weight_exception_assignment_id FOREIGN KEY (assignment_id) REFERENCES assignment(id) ON DELETE CASCADE,
);

DROP TABLE IF EXISTS login;
CREATE TABLE login (
  username VARCHAR(50) PRIMARY KEY,
  password VARCHAR(50) NOT NULL,
);