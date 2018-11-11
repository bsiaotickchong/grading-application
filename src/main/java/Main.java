import database.H2Database;
import org.jooq.*;
import org.jooq.grading_app.db.h2.tables.daos.MajorDao;
import org.jooq.grading_app.db.h2.tables.daos.StudentTypeDao;
import org.jooq.grading_app.db.h2.tables.pojos.Major;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.jooq.grading_app.db.h2.tables.records.MajorRecord;
import org.jooq.grading_app.db.h2.tables.records.StudentRecord;
import org.jooq.grading_app.db.h2.tables.records.StudentTypeRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.sql.Connection;

import static org.jooq.grading_app.db.h2.Tables.*;

public class Main {
    public static void main (String[] args) {
        startApplication();
    }

    private static void startApplication() {

        H2Database h2Database;
        try {
            h2Database = new H2Database();

            try {
                test(h2Database);
            } catch (Exception e) {
                throw e;
            } finally {
                h2Database.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test(H2Database h2Database) {
        Connection conn = h2Database.getConnection();
//
//        Statement stmt = conn.createStatement();
//
//        stmt.executeUpdate( "CREATE TABLE table1 ( user varchar(50) )" );
//        stmt.executeUpdate( "INSERT INTO table1 ( user ) VALUES ( 'Claudio' )" );
//        stmt.executeUpdate( "INSERT INTO table1 ( user ) VALUES ( 'Bernasconi' )" );
//
//        ResultSet rs = stmt.executeQuery("SELECT * FROM table1");
//        while(rs.next()) {
//            String name = rs.getString("user");
//            System.out.println(name);
//        }
//        stmt.close();

        // insert stuff
        DSLContext create = DSL.using(conn, SQLDialect.H2);

        // get Daos
        Configuration configuration = new DefaultConfiguration().set(conn).set(SQLDialect.H2);
        MajorDao majorDao = new MajorDao(configuration);
        StudentTypeDao studentTypeDao = new StudentTypeDao(configuration);

        createStudentType(create, "Undergraduate");
        createStudentType(create, "Graduate");
        createMajor(create, "Computer Science");

        // get major and studentType pojos from daos
        Major major = majorDao.fetchOne(MAJOR.NAME, "Computer Science");
        StudentType studentType = studentTypeDao.fetchOne(STUDENT_TYPE.NAME, "Undergraduate");

        // create students

        createStudent(create, "Brian", "Siao Tick Chong", "bstc@bu.edu", major, (short) 2018, studentType);
        createStudent(create, "Bob", "Ross", "br@bu.edu", major, (short) 1970, studentType);

        // read stuff
        Result<Record> result = create.select().from(STUDENT).fetch();

        for (Record r : result) {
            StudentRecord sr = (StudentRecord) r;

            System.out.println(String.format("id: %d, firstName: %s, lastName: %s, email: %s, major: %s, year: %d, studentType: %s",
                    sr.getId(),
                    sr.getFirstName(),
                    sr.getLastName(),
                    sr.getEmail(),
                    majorDao.fetchOneById(sr.getMajorId()).getName(),
                    sr.getYear(),
                    studentTypeDao.fetchOneById(sr.getStudentTypeId())));
        }
    }

    private static void createStudentType(DSLContext create,
                                          String name) {
        StudentTypeRecord studentTypeRecord = create.newRecord(STUDENT_TYPE);

        studentTypeRecord.setName(name);

        studentTypeRecord.store();
    }

    private static void createMajor(DSLContext create,
                                    String name) {
        MajorRecord majorRecord = create.newRecord(MAJOR);

        majorRecord.setName(name);

        majorRecord.store();
    }


}
