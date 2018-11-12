package students;

import database.H2DatabaseUtil;
import database.MetaData;
import org.jooq.DSLContext;
import org.jooq.grading_app.db.h2.tables.pojos.*;
import org.jooq.grading_app.db.h2.tables.records.StudentRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.jooq.grading_app.db.h2.Tables.*;

public class StudentMetaData implements MetaData {

    private final static Logger LOG = LoggerFactory.getLogger(StudentMetaData.class);

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private Major major;
    private Short year;
    private StudentType studentType;

    public StudentMetaData(String firstName,
                           String lastName,
                           String email,
                           Major major,
                           Short year,
                           StudentType studentType) throws SQLException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.major = major;
        this.year = year;
        this.studentType = studentType;

        createAndStoreRecord();
        LOG.debug("StudentMetaData added with ID: {}", this.id);
    }

    @Override
    public int createAndStoreRecord() throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);
            StudentRecord studentRecord = create.newRecord(STUDENT);

            studentRecord.setFirstName(firstName);
            studentRecord.setLastName(lastName);
            studentRecord.setEmail(email);
            studentRecord.setMajorId(major.getId());
            studentRecord.setYear(year);
            studentRecord.setStudentTypeId(studentType.getId());

            int result = studentRecord.store();

            this.id = studentRecord.getId();    // get auto-generated ID
            return result;
        } catch (SQLException e) {
            LOG.error("Could not create StudentRecord");
            throw e;
        }
    }

    public int setFirstName(String firstName) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            StudentRecord studentRecord = getStudentRecord(conn);
            studentRecord.setFirstName(firstName);
            return studentRecord.store();
        } catch (SQLException e) {
            LOG.error("Could not set first name");
            throw e;
        }
    }

    public int setLastName(String lastName) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            StudentRecord studentRecord = getStudentRecord(conn);
            studentRecord.setLastName(lastName);
            return studentRecord.store();
        } catch (SQLException e) {
            LOG.error("Could not set last name");
            throw e;
        }
    }

    public int setEmail(String email) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            StudentRecord studentRecord = getStudentRecord(conn);
            studentRecord.setEmail(email);
            return studentRecord.store();
        } catch (SQLException e) {
            LOG.error("Could not set email");
            throw e;
        }
    }

    public int setMajor(Major major) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            StudentRecord studentRecord = getStudentRecord(conn);
            studentRecord.setMajorId(major.getId());
            return studentRecord.store();
        } catch (SQLException e) {
            LOG.error("Could not set major");
            throw e;
        }
    }

    public int setYear(Short year) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            StudentRecord studentRecord = getStudentRecord(conn);
            studentRecord.setYear(year);
            return studentRecord.store();
        } catch (SQLException e) {
            LOG.error("Could not set year");
            throw e;
        }
    }

    public int setStudentType(StudentType studentType) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            StudentRecord studentRecord = getStudentRecord(conn);
            studentRecord.setStudentTypeId(studentType.getId());
            return studentRecord.store();
        } catch (SQLException e) {
            LOG.error("Could not set student type");
            throw e;
        }
    }

    private StudentRecord getStudentRecord(Connection conn) {
        DSLContext create = H2DatabaseUtil.createContext(conn);
        return create.fetchOne(STUDENT, STUDENT.ID.eq(id));
    }

    public int enrollInCourse(Course course) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .insertInto(ENROLLMENT,
                            ENROLLMENT.COURSE_ID, ENROLLMENT.STUDENT_ID)
                    .values(course.getId(), id)
                    .execute();
        } catch (SQLException e) {
            LOG.error("Could not enroll student in course");
            throw e;
        }
    }

    public int addNote(Course course, String noteText) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .insertInto(NOTE,
                            NOTE.COURSE_ID, NOTE.STUDENT_ID, NOTE.NOTE_TEXT)
                    .values(course.getId(), id, noteText)
                    .execute();
        } catch (SQLException e) {
            LOG.error("Could not insert note");
            throw e;
        }
    }

    public int removeNote(Note note) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .deleteFrom(NOTE)
                    .where(NOTE.ID.eq(note.getId()))
                    .execute();
        } catch (SQLException e) {
            LOG.error("Could not delete note");
            throw e;
        }
    }

    public List<Note> getNotes(Course course) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .selectFrom(NOTE)
                    .where(NOTE.COURSE_ID.eq(course.getId()))
                    .and(NOTE.STUDENT_ID.eq(id))
                    .fetchInto(Note.class);
        } catch (SQLException e) {
            LOG.error("Could not retrieve notes");
            throw e;
        }
    }

    public List<Course> getCourses() throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .select(COURSE.asterisk())
                    .from(ENROLLMENT
                            .join(COURSE)
                            .on(ENROLLMENT.COURSE_ID.eq(COURSE.ID)))
                    .where(ENROLLMENT.STUDENT_ID.eq(this.id))
                    .fetchInto(Course.class);
        }
    }

    @Override
    public void printMetaData() {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            String studentString = getStudentRecord(conn).into(Student.class).toString();
            StringBuilder sb = new StringBuilder();

            sb.append(studentString);
            sb.append(",\t");
            sb.append(major.toString());
            sb.append(",\t");
            sb.append(studentType.toString());
            sb.append(",\t");

            for (Course course : getCourses()) {
                sb.append(course.toString());
                sb.append("\t");
            }

            LOG.info(sb.toString());
        } catch (SQLException e) {
            LOG.error("Error printing StudentMetaData: " + e.getMessage());
        }
    }
}
