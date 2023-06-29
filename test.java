import dto.*;
import com.thinking.machines.orm.*;

class test
{
public static void main(String[] gg)
{
try
{
DataManager dataManager = new DataManager();
Student student = new Student();
student.setRollnumber(222);
dataManager.begin();
dataManager.delete(student);
dataManager.end();
}catch(Exception ee)
{
System.out.print(ee);
}
}
}