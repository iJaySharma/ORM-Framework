package dto;
import com.thinking.machines.orm.annotation.*;
@Table(name="company_new")
public class Companynew
{
@PrimaryKey(name="id")
@Column(name="id")
private Integer id;
@Column(name="address")
private String address;
@Column(name="company_ceo")
private String companyCeo;
@Column(name="company_name")
private String companyName;
@Column(name="inception_date")
private String inceptionDate;
public Companynew()
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
public void  setAddress(String address)
{
this.address=address;
}
public String getAddress()
{
return this.address;
}
public void  setCompanyCeo(String companyCeo)
{
this.companyCeo=companyCeo;
}
public String getCompanyCeo()
{
return this.companyCeo;
}
public void  setCompanyName(String companyName)
{
this.companyName=companyName;
}
public String getCompanyName()
{
return this.companyName;
}
public void  setInceptionDate(String inceptionDate)
{
this.inceptionDate=inceptionDate;
}
public String getInceptionDate()
{
return this.inceptionDate;
}
}