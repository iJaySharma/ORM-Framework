package dto;
import com.thinking.machines.orm.annotation.*;
@Table(name="team_new")
public class Teamnew
{
@PrimaryKey(name="code")
@Column(name="code")
private Integer code;
@Column(name="team_lead")
private String teamLead;
@ForeignKey(parent="company_new",column="id")
@Column(name="company_id")
private Integer companyId;
public Teamnew()
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
public void  setTeamLead(String teamLead)
{
this.teamLead=teamLead;
}
public String getTeamLead()
{
return this.teamLead;
}
public void  setCompanyId(Integer companyId)
{
this.companyId=companyId;
}
public Integer getCompanyId()
{
return this.companyId;
}
}