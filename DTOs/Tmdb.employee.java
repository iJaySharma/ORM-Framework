import com.thinking.machines.orm.annotations.*;
@Table(name="tmdb.employee")
public class Tmdb.employee
{
@PrimaryKey(name="id")
@AutoIncrement(name="id")
@Column(name="id")
private Integer id;
@Column(name="name")
private String name;
@ForeignKey(parent="designation",column="code")
@Column(name="designation_code")
private Integer designationCode;
@Column(name="date_of_birth")
private java.sql.Date dateOfBirth;
@Column(name="gender")
private String gender;
@Column(name="is_indian")
private  isIndian;
@Column(name="basic_salary")
private  basicSalary;
@Column(name="pan_number")
private String panNumber;
@Column(name="aadhar_card_number")
private String aadharCardNumber;
public Tmdb.employee()
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
public void  setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
public void  setDesignationCode(Integer designationCode)
{
this.designationCode=designationCode;
}
public Integer getDesignationCode()
{
return this.designationCode;
}
public void  setDateOfBirth(java.sql.Date dateOfBirth)
{
this.dateOfBirth=dateOfBirth;
}
public java.sql.Date getDateOfBirth()
{
return this.dateOfBirth;
}
public void  setGender(String gender)
{
this.gender=gender;
}
public String getGender()
{
return this.gender;
}
public void  setIsIndian( isIndian)
{
this.isIndian=isIndian;
}
public  getIsIndian()
{
return this.isIndian;
}
public void  setBasicSalary( basicSalary)
{
this.basicSalary=basicSalary;
}
public  getBasicSalary()
{
return this.basicSalary;
}
public void  setPanNumber(String panNumber)
{
this.panNumber=panNumber;
}
public String getPanNumber()
{
return this.panNumber;
}
public void  setAadharCardNumber(String aadharCardNumber)
{
this.aadharCardNumber=aadharCardNumber;
}
public String getAadharCardNumber()
{
return this.aadharCardNumber;
}
}