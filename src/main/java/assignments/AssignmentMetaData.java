package assignments;

import database.H2DatabaseUtil;
import database.MetaData;
import org.jooq.DSLContext;
import org.jooq.grading_app.db.h2.tables.pojos.*;
import org.jooq.grading_app.db.h2.tables.records.AssignmentRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import students.StudentMetaData;

import java.sql.Connection;  
import java.sql.SQLException;
import java.util.List;

import static org.jooq.grading_app.db.h2.Tables.*;

public class AssignmentMetaData implements MetaData {
    private final static Logger LOG = LoggerFactory.getLogger(AssignmentMetaData.class);

    private int id;
    private int categoryID;
    private Boolean extra_credit;
    public String name;

    public AssignmentMetaData(Integer categoryID,
                              Boolean extra_credit,
                              String name) throws SQLException{
        this.categoryID = categoryID;
        this.extra_credit = extra_credit;
        this.name = name;

        createAndStoreRecord();
        LOG.debug("StudentMetaData added with ID: {}", this.id);
    }

    public AssignmentMetaData(Assignment assignment) throws SQLException{
        this.id = assignment.getId();
        this.name = assignment.getName();
        this.categoryID = assignment.getCategoryId();
        this.extra_credit = assignment.getExtraCredit();
    }

    @Override
    public int createAndStoreRecord() throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);
            AssignmentRecord assignmentRecord = create.newRecord(ASSIGNMENT);

            assignmentRecord.setCategoryId(categoryID);
            assignmentRecord.setExtraCredit(extra_credit);
            assignmentRecord.setName(name);

            int result = assignmentRecord.store();

            this.id = assignmentRecord.getId();
            return result;
        } catch (SQLException e) {
            LOG.error("Could not create AssignmentRecord");
            throw e;
        }
    }

    public int setAssignmentName(String name) throws SQLException{
        try(Connection conn = H2DatabaseUtil.createConnection()){
            AssignmentRecord assignmentRecord = getAssignmentRecord(conn);
            assignmentRecord.setName(name);
            return assignmentRecord.store();
        }catch (SQLException e){
            LOG.error("Could not set name");
            throw e;
        }
    }

    public String getName() {
        return name;
    }

    public int setCategory(Category category) throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()){
            AssignmentRecord assignmentRecord = getAssignmentRecord(conn);
            assignmentRecord.setCategoryId(category.getId());
            return assignmentRecord.store();
        } catch(SQLException e){
            LOG.error("Could not set category");
            throw e;
        }
    }

    //right now, I just leave extra point as it was, and we can change this after ask
    //professor about the implementation she wants.
    private int setExtraCredit(Boolean extra_credit) throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()){
            AssignmentRecord assignmentRecord = getAssignmentRecord(conn);
            assignmentRecord.setExtraCredit(extra_credit);
            return assignmentRecord.store();
        }catch(SQLException e){
            LOG.error("Could not set category");
            throw e;
        }
    }

    //get all the students' grade who takes the assignments
    public float getStudentsGrade(StudentMetaData studentMetaData) throws SQLException{
        try (Connection conn = H2DatabaseUtil.createConnection()){
            DSLContext create = H2DatabaseUtil.createContext(conn);

           return create.select(STUDENT_GRADE.GRADE)
                   .from(STUDENT)
                   .join(STUDENT).on(STUDENT_GRADE.STUDENT_ID.eq(STUDENT.ID))
                   .where(STUDENT_GRADE.ASSIGNMENT_ID.eq(this.id))
                   .and(STUDENT.ID.eq(studentMetaData.getId()))
                   .fetchOneInto(float.class);
        }
    }

    //get all the students who have taken the assignment
    public List<StudentMetaData> getStudent() throws SQLException{
        try(Connection conn = H2DatabaseUtil.createConnection()){
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create.select()
                    .from(STUDENT)
                    .join(STUDENT_GRADE).on(STUDENT.ID.eq(STUDENT_GRADE.STUDENT_ID))
                    .where(STUDENT_GRADE.ASSIGNMENT_ID.eq(this.id))
                    .fetchInto(StudentMetaData.class);
        }
    }

    //get information inside assignment weight
    public List<AssignmentWeight> getWeight() throws SQLException{
        try(Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create.select()
                    .from(ASSIGNMENT_WEIGHT)
                    .where(ASSIGNMENT_WEIGHT.ASSIGNMENT_ID.eq(this.id))
                    .fetchInto(AssignmentWeight.class);
        }
    }

    public AssignmentWeight getWeightForStudentType(StudentType studentType) throws SQLException{
        try(Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create.select()
                    .from(ASSIGNMENT_WEIGHT)
                    .where(ASSIGNMENT_WEIGHT.ASSIGNMENT_ID.eq(this.id))
                    .and(ASSIGNMENT_WEIGHT.STUDENT_TYPE_ID.eq(studentType.getId()))
                    .fetchOneInto(AssignmentWeight.class);
        }
    }

    public List<StudentMetaData> getStudentsByStudentType(StudentType studentType) throws SQLException{
        try(Connection conn = H2DatabaseUtil.createConnection()){
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create.select(STUDENT.asterisk())
                    .from(STUDENT_GRADE)
                    .join(STUDENT).on(STUDENT_GRADE.ID.eq(STUDENT.ID))
                    .where(STUDENT_GRADE.ASSIGNMENT_ID.eq(this.id))
                    .and(STUDENT.STUDENT_TYPE_ID.eq(studentType.getId()))
                    .fetchInto(StudentMetaData.class);

        }
    }

    //get all the exception weight
    public List<AssignmentWeightException> getException() throws SQLException{
        try(Connection conn = H2DatabaseUtil.createConnection()) {
            DSLContext create = H2DatabaseUtil.createContext(conn);

            return create.select()
                    .from(ASSIGNMENT_WEIGHT_EXCEPTION)
                    .where(ASSIGNMENT_WEIGHT_EXCEPTION.ASSIGNMENT_ID.eq(this.id))
                    .fetchInto(AssignmentWeightException.class);
        }
    }




    private AssignmentRecord getAssignmentRecord(Connection conn){
        DSLContext create = H2DatabaseUtil.createContext(conn);
        return create.fetchOne(ASSIGNMENT, ASSIGNMENT.ID.eq(id));
    }



    @Override
    public void printMetaData(){
        try (Connection conn = H2DatabaseUtil.createConnection()) {
            String assignmentString = getAssignmentRecord(conn).into(Assignment.class).toString();
            StringBuilder sb = new StringBuilder();

            sb.append(name);
            sb.append(",\t");
            sb.append(assignmentString);
            sb.append(",\t");
            sb.append(extra_credit.toString());
            sb.append(",\t");
            sb.append(categoryID);

            for(StudentMetaData student : getStudent()){
                sb.append(student.toString());
                sb.append("\t");
            }

            LOG.info(sb.toString());
        } catch (SQLException e) {
            LOG.error("Error printing AssignmentMetaData: " + e.getMessage());
        }
    }



}
