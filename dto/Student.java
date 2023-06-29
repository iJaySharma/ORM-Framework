package dto;
import com.thinking.machines.orm.annotation.*;
@Table(name="student")
public class Student
{
@PrimaryKey(name="rollnumber")
@Column(name="rollnumber")
private Integer rollnumber;
@Column(name="name")
private String name;
@Column(name="age")
private Integer age;
public Student()
{
}
public void  setRollnumber(Integer rollnumber)
{
this.rollnumber=rollnumber;
}
public Integer getRollnumber()
{
return this.rollnumber;
}
public void  setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
public void  setAge(Integer age)
{
this.age=age;
}
public Integer getAge()
{
return this.age;
}
}