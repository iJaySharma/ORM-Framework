package com.thinking.machines.orm.wrapper;
public class ConfigurationWrapper
{
private String jdbcDriver;
private String connectionUrl;
private String username;
private String password;
private String path;
public ConfigurationWrapper()
{
this.jdbcDriver=null;
this.connectionUrl=null;
this.username=null;
this.password=null;
this.path=null;
}
public void setJdbcDriver(String jdbcDriver)
{
this.jdbcDriver=jdbcDriver;
}
public String getJdbcDriver()
{
return this.jdbcDriver;
}
public void setConnectionUrl(String connectionUrl)
{
this.connectionUrl=connectionUrl;
}
public String getConnectionUrl()
{
return this.connectionUrl;
}
public void setUsername(String username)
{
this.username=username;
}
public String getUsername()
{
return this.username;
}
public void setPassword(String password)
{
this.password=password;
}
public String getPassword()
{
return this.password;
}
public void setPath(String path)
{
this.path=path;
}
public String getPath()
{
return this.path;
}
}