import com.thinking.machines.orm.annotations.*;
@Table(name="designation")
public class Designation
{
@PrimaryKey(name="code")
@AutoIncrement(name="code")
@Column(name="code")
private Integer code;
@Column(name="title")
private String title;
public Designation()
{
}
public void  setCode(Integer code)
{
this.code=code;
}
public Integer getCode()
{
return this.code;
}
public void  setTitle(String title)
{
this.title=title;
}
public String getTitle()
{
return this.title;
}
}