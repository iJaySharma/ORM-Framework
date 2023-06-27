package dto;import com.thinking.machines.orm.annotation.*;
@Table(name="users")
public class Users
{
@PrimaryKey(name="user")
@Column(name="user")
private Integer user;
@PrimaryKey(name="current_connections")
@Column(name="current_connections")
private String currentConnections;
@Column(name="total_connections")
private String totalConnections;
@Column(name="user_id")
private null userId;
@Column(name="username")
private null username;
@Column(name="password")
private null password;
@Column(name="firstname")
private null firstname;
@Column(name="lastname")
private null lastname;
@AutoIncrement(name="id")
@Column(name="id")
private null id;
@Column(name="name")
private null name;
@Column(name="email")
private null email;
public Users()
{
}
public void  setUser(Integer user)
{
this.user=user;
}
public Integer getUser()
{
return this.user;
}
public void  setCurrentConnections(String currentConnections)
{
this.currentConnections=currentConnections;
}
public String getCurrentConnections()
{
return this.currentConnections;
}
public void  setTotalConnections(String totalConnections)
{
this.totalConnections=totalConnections;
}
public String getTotalConnections()
{
return this.totalConnections;
}
public void  setUserId(null userId)
{
this.userId=userId;
}
public null getUserId()
{
return this.userId;
}
public void  setUsername(null username)
{
this.username=username;
}
public null getUsername()
{
return this.username;
}
public void  setPassword(null password)
{
this.password=password;
}
public null getPassword()
{
return this.password;
}
public void  setFirstname(null firstname)
{
this.firstname=firstname;
}
public null getFirstname()
{
return this.firstname;
}
public void  setLastname(null lastname)
{
this.lastname=lastname;
}
public null getLastname()
{
return this.lastname;
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