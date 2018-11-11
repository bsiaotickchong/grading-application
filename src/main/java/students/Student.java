package students;

import database.PersistedObject;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.grading_app.db.h2.tables.daos.MajorDao;
import org.jooq.grading_app.db.h2.tables.daos.StudentTypeDao;
import org.jooq.grading_app.db.h2.tables.pojos.Major;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.jooq.grading_app.db.h2.tables.records.StudentRecord;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

import static org.jooq.grading_app.db.h2.Tables.STUDENT;

public class Student implements PersistedObject {

    private final static Logger LOG = LoggerFactory.getLogger(Student.class);
    private final DSLContext create;

    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Major major;
    private final Short year;
    private final StudentType studentType;

    public Student(Connection conn,
                   MajorDao majorDao,
                   StudentTypeDao studentTypeDao,
                   String firstName,
                   String lastName,
                   String email,
                   Major major,
                   Short year,
                   StudentType studentType) {
        this.create = DSL.using(conn, SQLDialect.H2);

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.major = major;
        this.year = year;
        this.studentType = studentType;

        this.id = createAndStoreRecord();
        LOG.debug("Student added with ID: %d", this.id);
    }

    @Override
    public int createAndStoreRecord() {
        StudentRecord studentRecord = create.newRecord(STUDENT);

        studentRecord.setFirstName(firstName);
        studentRecord.setLastName(lastName);
        studentRecord.setEmail(email);
        studentRecord.setMajorId(major.getId());
        studentRecord.setYear(year);
        studentRecord.setStudentTypeId(studentType.getId());

        return studentRecord.store();
    }
}
