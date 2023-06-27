package dto;import com.thinking.machines.orm.annotation.*;
@Table(name="administrator")
public class Administrator
{
@PrimaryKey(name="username")
@Column(name="username")
private String username;
@Column(name="password")
private String password;
public Administrator()
{
}
public void  setUsername(String username)
{
this.username=username;
}
public String getUsername()
{
return this.username;
}
public void  setPassword(String password)
{
this.password=password;
}
public String getPassword()
{
return this.password;
}
}