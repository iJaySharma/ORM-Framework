import dto.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import com.thinking.machines.orm.*;

class test
{
public static void main(String[] gg)
{
try
{
DataManager dataManager = new DataManager();
/* 
Employee employee = new Employee();

employee.setId(11);
employee.setName("ss");
employee.setDesignationCode(13);
//java.sql.Date date = java.sql.Date.valueOf("2009-03-03");
//employee.setDateOfBirth(date);
employee.setGender("M");
employee.setIsIndian(true);
BigDecimal bigDecimal = new BigDecimal(50.00);
employee.setBasicSalary(bigDecimal);
employee.setPanNumber("ss");
employee.setAadharCardNumber("ss");
dataManager.begin();
dataManager.update(employee);
dataManager.end();
*/

dataManager.begin();
List<Object> students=dataManager.select(new Student()).where("rollnumber").le(105).fire();
for(Object object: students)
{
Student student = (Student) object;
System.out.println(student.getName());
System.out.println(student.getAge());
System.out.println(student.getRollnumber());
}
dataManager.end();

}catch(Exception ee)
{
ee.printStackTrace();
}
}
}