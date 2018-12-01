package application;

import courses.CourseMetaData;
import database.H2DatabaseUtil;
import gui.AllCoursesPage;
import gui.PageLoader;
import org.jooq.*;
import org.jooq.grading_app.db.h2.tables.pojos.Major;
import org.jooq.grading_app.db.h2.tables.pojos.Student;
import org.jooq.grading_app.db.h2.tables.pojos.StudentType;
import org.jooq.grading_app.db.h2.tables.pojos.TimeOfYear;
import org.jooq.grading_app.db.h2.tables.records.MajorRecord;
import org.jooq.grading_app.db.h2.tables.records.StudentTypeRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import students.StudentMetaData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.jooq.grading_app.db.h2.Tables.*;

public class GradingApplication {
    private final static Logger LOG = LoggerFactory.getLogger(GradingApplication.class);
    private final static String APPLICATION_NAME = "Grading Application";

    public final static PageLoader PAGE_LOADER = new PageLoader(APPLICATION_NAME);

    public static void main (String[] args) {
        startApplication();
    }

    private static void startApplication() {
        GradingApplication main = new GradingApplication();
        try {
            main.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test() throws SQLException {
        List<Major> majors;
        List<StudentType> studentTypes;
        List<TimeOfYear> timeOfYears;
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);
            // get major and studentType pojos from daos
            majors = create
                    .selectFrom(MAJOR)
                    .fetchInto(Major.class);

            studentTypes = create
                    .selectFrom(STUDENT_TYPE)
                    .fetchInto(StudentType.class);

            timeOfYears = create
                    .selectFrom(TIME_OF_YEAR)
                    .fetchInto(TimeOfYear.class);
        }

        // create students
        StudentMetaData studentMetaData1 = new StudentMetaData("Brian", "Siao Tick Chong", "bstc@bu.edu", majors.get(0), (short) 2018, studentTypes.get(0));
        StudentMetaData studentMetaData2 = new StudentMetaData("Bob", "Ross", "br@bu.edu", majors.get(0), (short) 1970, studentTypes.get(1));

        // create courses
        CourseMetaData courseMetaData1 = new CourseMetaData("CS591D1", timeOfYears.get(0), "Class on object-oriented programming.");
        CourseMetaData courseMetaData2 = new CourseMetaData("CS101", timeOfYears.get(1), "Intro to Computer Science");

        // enroll students in courses
        studentMetaData1.enrollInCourse(courseMetaData2.getCourse());
        studentMetaData2.enrollInCourse(courseMetaData1.getCourse());

        // create notes
        studentMetaData1.addNote(courseMetaData1.getCourse(), "This student doesn't come to class");
        studentMetaData1.addNote(courseMetaData1.getCourse(), "But at least he does his homework.");

        // print student data from query
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            // read stuff
            List<Student> students = create.select().from(STUDENT).fetch().into(Student.class);
            for (Student s : students) {
                System.out.println(s.toString());
            }
        }

        // print student data from objects
        studentMetaData1.printMetaData();
        studentMetaData2.printMetaData();

        // print course data
        courseMetaData1.printMetaData();
        courseMetaData2.printMetaData();

        PAGE_LOADER.instantiate(new AllCoursesPage());
    }

    private static int createStudentType(DSLContext create,
                                          String name) {
        StudentTypeRecord studentTypeRecord = create.newRecord(STUDENT_TYPE);

        studentTypeRecord.setName(name);

        return studentTypeRecord.store();
    }

    private static int createMajor(DSLContext create,
                                    String name) {
        MajorRecord majorRecord = create.newRecord(MAJOR);

        majorRecord.setName(name);

        return majorRecord.store();
    }
}
