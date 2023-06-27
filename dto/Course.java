package dto;import com.thinking.machines.orm.annotation.*;
@Table(name="course")
public class Course
{
@PrimaryKey(name="id")
@AutoIncrement(name="id")
@Column(name="id")
private Integer id;
@Column(name="course_name")
private String courseName;
public Course()
{
}
public void  setId(Integer id)
{
this.id=id;
}
public Integer getId()
{
return this.id;
}
public void  setCourseName(String courseName)
{
this.courseName=courseName;
}
public String getCourseName()
{
return this.courseName;
}
}