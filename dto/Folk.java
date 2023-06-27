package dto;import com.thinking.machines.orm.annotation.*;
@Table(name="folk")
public class Folk
{
@Column(name="name")
private String name;
@Column(name="age")
private Integer age;
@ForeignKey(parent="course",column="id")
@Column(name="course_id")
private Integer courseId;
public Folk()
{
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
public void  setCourseId(Integer courseId)
{
this.courseId=courseId;
}
public Integer getCourseId()
{
return this.courseId;
}
}