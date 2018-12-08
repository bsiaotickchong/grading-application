package assignments;

import students.StudentMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;



public class AssignmentPageFunction {

    public Hashtable<String, List<StudentMetaData>> seperateBySType(List<StudentMetaData> S){
        Hashtable<String, List<StudentMetaData>> SepS = new Hashtable<>();

        for (StudentMetaData stu : S){
            String sType = stu.getStudentType().getName();
            if(SepS.containsKey(sType)){
                SepS.get(sType).add(stu);
            }else{
                SepS.put(sType, new ArrayList<StudentMetaData>());
                SepS.get(sType).add(stu);
            }
        }

        return SepS;
    }
}
