package students;

import assignments.AssignmentMetaData;
import courses.CourseMetaData;
import database.H2DatabaseUtil;
import database.MetaData;
import org.jooq.DSLContext;
import org.jooq.grading_app.db.h2.tables.pojos.*;
import org.jooq.grading_app.db.h2.tables.records.NoteRecord;
import org.jooq.grading_app.db.h2.tables.records.StudentGradeRecord;
import org.jooq.grading_app.db.h2.tables.records.StudentRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.jooq.grading_app.db.h2.Tables.*;
import static org.jooq.impl.DSL.selectFrom;

public class StudentMetaData implements MetaData {

    private final static Logger LOG = LoggerFactory.getLogger(StudentMetaData.class);

    private int id;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String email;
    private Major major;
    private Short year;
    private StudentType studentType;

    public StudentMetaData(String firstName,
                           String middleInitial,
                           String lastName,
                           String email,
                           Major major,
                           Short year,
                           StudentType studentType) throws SQLException {
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.email = email;
        this.major = major;
        this.year = year;
        this.studentType = studentType;

        createAndStoreRecord();
        LOG.debug("StudentMetaData added with ID: {}", this.id);
    }

    public StudentMetaData(String firstName,
                           String lastName,
                           String email,
                           Major major,
                           Short year,
                           StudentType studentType) throws SQLException {
        this.firstName = firstName;
        this.middleInitial = "";
        this.lastName = lastName;
        this.email = email;
        this.major = major;
        this.year = year;
        this.studentType = studentType;

        createAndStoreRecord();
        LOG.debug("StudentMetaData added with ID: {}", this.id);
    }

    // Constructor which creates a MetaData object from an existing Pojo/record
    public StudentMetaData(Student student) throws SQLException {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.middleInitial = student.getMiddleInitial();
        this.lastName = student.getLastName();
        this.email = student.getEmail();
        this.major = getMajorFromId(student.getMajorId());
        this.year = student.getYear();
        this.studentType = getStudentTypeFromId(student.getStudentTypeId());
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
            int res = studentRecord.store();
            this.firstName = firstName;
            return res;
        } catch (SQLException e) {
            LOG.error("Could not set first name");
            throw e;
        }
    }

    public int setMiddleInitial(String middleInitial) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            StudentRecord studentRecord = getStudentRecord(conn);
            studentRecord.setMiddleInitial(middleInitial);
            int res = studentRecord.store();
            this.middleInitial = middleInitial;
            return res;
        } catch (SQLException e) {
            LOG.error("Could not set middle initial");
            throw e;
        }
    }

    public int setLastName(String lastName) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            StudentRecord studentRecord = getStudentRecord(conn);
            studentRecord.setLastName(lastName);
            int res = studentRecord.store();
            this.lastName = lastName;
            return res;
        } catch (SQLException e) {
            LOG.error("Could not set last name");
            throw e;
        }
    }

    public int setEmail(String email) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            StudentRecord studentRecord = getStudentRecord(conn);
            studentRecord.setEmail(email);
            int res = studentRecord.store();
            this.email = email;
            return res;
        } catch (SQLException e) {
            LOG.error("Could not set email");
            throw e;
        }
    }

    public int setMajor(Major major) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            StudentRecord studentRecord = getStudentRecord(conn);
            studentRecord.setMajorId(major.getId());
            int res = studentRecord.store();
            this.major = major;
            return res;
        } catch (SQLException e) {
            LOG.error("Could not set major");
            throw e;
        }
    }

    public int setYear(Short year) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            StudentRecord studentRecord = getStudentRecord(conn);
            studentRecord.setYear(year);
            int res = studentRecord.store();
            this.year = year;
            return res;
        } catch (SQLException e) {
            LOG.error("Could not set year");
            throw e;
        }
    }

    public int setStudentType(StudentType studentType) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            StudentRecord studentRecord = getStudentRecord(conn);
            studentRecord.setStudentTypeId(studentType.getId());
            int res = studentRecord.store();
            this.studentType = studentType;
            return res;
        } catch (SQLException e) {
            LOG.error("Could not set student type");
            throw e;
        }
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public StudentType getStudentType() {
        return studentType;
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

    // helper function TODO: place inside manager
    private Major getMajorFromId(int majorId) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .selectFrom(MAJOR)
                    .where(MAJOR.ID.eq(majorId))
                    .fetchOneInto(Major.class);
        }
    }

    // helper function TODO: place inside manager
    private StudentType getStudentTypeFromId(int studentTypeId) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .selectFrom(STUDENT_TYPE)
                    .where(STUDENT_TYPE.ID.eq(studentTypeId))
                    .fetchOneInto(StudentType.class);
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

    public StudentGrade getGradeForAssignment(AssignmentMetaData assignmentMetaData) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            boolean hasGrade = create
                    .fetchExists(
                            selectFrom(STUDENT_GRADE)
                            .where(STUDENT_GRADE.STUDENT_ID.eq(this.id))
                            .and(STUDENT_GRADE.ASSIGNMENT_ID.eq(assignmentMetaData.getId()))
                    );

            // Create student_grade row for this student and assignment if it doesn't exist
            if (!hasGrade) {
                StudentGradeRecord studentGradeRecord = create.newRecord(STUDENT_GRADE);

                studentGradeRecord.setStudentId(this.id);
                studentGradeRecord.setAssignmentId(assignmentMetaData.getId());
                studentGradeRecord.setGrade(0d);
                studentGradeRecord.setNoteText("");

                int result = studentGradeRecord.store();
            }

            return create
                    .selectFrom(STUDENT_GRADE)
                    .where(STUDENT_GRADE.STUDENT_ID.eq(this.id))
                    .and(STUDENT_GRADE.ASSIGNMENT_ID.eq(assignmentMetaData.getId()))
                    .fetchOneInto(StudentGrade.class);
        }
    }

    public int setGrade(StudentGrade studentGrade) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .update(STUDENT_GRADE)
                    .set(STUDENT_GRADE.GRADE, studentGrade.getGrade())
                    .where(STUDENT_GRADE.ID.eq(studentGrade.getId()))
                    .execute();
        }
    }

    public Note addNote(String noteText, CourseMetaData courseMetaData) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            NoteRecord noteRecord = create.newRecord(NOTE);
            noteRecord.setCourseId(courseMetaData.getId());
            noteRecord.setStudentId(this.id);
            noteRecord.setNoteText(noteText);

            noteRecord.store();

            return create
                    .selectFrom(NOTE)
                    .where(NOTE.ID.eq(noteRecord.getId()))
                    .fetchOneInto(Note.class);
        }
    }

    public int updateNote(Note note, String updatedNoteText) throws SQLException {
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create
                    .update(NOTE)
                    .set(NOTE.NOTE_TEXT, updatedNoteText)
                    .where(NOTE.ID.eq(note.getId()))
                    .execute();
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
