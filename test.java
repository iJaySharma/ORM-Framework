import com.thinking.machines.orm.*;
import dto.*;
public class test
{
public static void main(String gg[])
{
try
{
Student student = new Student();
student.setRollnumber(191017);
student.setName("JAy");
student.setAge(22);
DataManager dataManager = new DataManager();
dataManager.begin();
dataManager.save(student);
dataManager.end();
}catch(Exception exception)
{
exception.printStackTrace();
}
}
}