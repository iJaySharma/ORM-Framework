import com.thinking.machines.orm.*;
import dto.*;

import java.math.BigDecimal;
import java.sql.Date;
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

Employee employee = new Employee();
employee.setId(1);
employee.setName("jay");
Date dateOfBirth = Date.valueOf("2023-06-28");
employee.setDesignationCode(1);
// Call the setDateOfBirth method and pass the dateOfBirth variable as an argument
employee.setDateOfBirth(dateOfBirth);
employee.setGender("M");
employee.setIsIndian(true);
BigDecimal basicSalary = new BigDecimal("1800.50");
employee.setBasicSalary(basicSalary);
employee.setPanNumber("abcdef");
employee.setAadharCardNumber("xyzpqr");
dataManager.begin();
dataManager.update(employee);
dataManager.end();

}catch(Exception exception)
{
exception.printStackTrace();
}
}
}