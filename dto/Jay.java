package dto;
import com.thinking.machines.orm.annotation.*;
@Table(name="jay")
public class Jay
{
@PrimaryKey(name="aid")
@Column(name="aid")
private Integer aid;
@Column(name="fname")
private String fname;
@Column(name="lname")
private String lname;
@Column(name="mname")
private String mname;
@Column(name="color")
private String color;
public Jay()
{
}
public void  setAid(Integer aid)
{
this.aid=aid;
}
public Integer getAid()
{
return this.aid;
}
public void  setFname(String fname)
{
this.fname=fname;
}
public String getFname()
{
return this.fname;
}
public void  setLname(String lname)
{
this.lname=lname;
}
public String getLname()
{
return this.lname;
}
public void  setMname(String mname)
{
this.mname=mname;
}
public String getMname()
{
return this.mname;
}
public void  setColor(String color)
{
this.color=color;
}
public String getColor()
{
return this.color;
}
}