package dto;import com.thinking.machines.orm.annotation.*;
@Table(name="sys_config")
public class Sysconfig
{
@PrimaryKey(name="variable")
@Column(name="variable")
private Integer variable;
@PrimaryKey(name="value")
@Column(name="value")
private String value;
@Column(name="set_time")
private String setTime;
@Column(name="set_by")
private null setBy;
@AutoIncrement(name="id")
@Column(name="id")
private null id;
@Column(name="name")
private null name;
@Column(name="email")
private null email;
public Sysconfig()
{
}
public void  setVariable(Integer variable)
{
this.variable=variable;
}
public Integer getVariable()
{
return this.variable;
}
public void  setValue(String value)
{
this.value=value;
}
public String getValue()
{
return this.value;
}
public void  setSetTime(String setTime)
{
this.setTime=setTime;
}
public String getSetTime()
{
return this.setTime;
}
public void  setSetBy(null setBy)
{
this.setBy=setBy;
}
public null getSetBy()
{
return this.setBy;
}
public void  setId(null id)
{
this.id=id;
}
public null getId()
{
return this.id;
}
public void  setName(null name)
{
this.name=name;
}
public null getName()
{
return this.name;
}
public void  setEmail(null email)
{
this.email=email;
}
public null getEmail()
{
return this.email;
}
}