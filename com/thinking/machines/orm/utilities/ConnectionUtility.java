package com.thinking.machines.orm.utilities;
import com.thinking.machines.orm.exception.*;
import com.thinking.machines.orm.wrapper.*;
import java.sql.*;
public class ConnectionUtility 
{
public static Connection getConnection() throws ORMException
{
Connection connection=null;
ConfigurationWrapper configurationWrapper=null;
try
{
configurationWrapper=ConfigurationReaderUtility.getConfigurations();
Class.forName(configurationWrapper.getJdbcDriver().trim());
connection=DriverManager.getConnection(configurationWrapper.getConnectionUrl().trim(),configurationWrapper.getUsername().trim(),configurationWrapper.getPassword().trim());
if(connection==null) throw new ORMException("Can't establish connection with specified details");
}catch(Exception exception)
{
    exception.printStackTrace();
throw new ORMException("Can't establish connection with specified details");
}
return connection;
}
}