package com.thinking.machines.orm.validator;
import com.thinking.machines.orm.exception.*;
import com.thinking.machines.orm.utilities.*;
import com.thinking.machines.orm.wrapper.*;
import com.thinking.machines.orm.annotation.*;
import javafx.util.*;
import java.util.*;
import java.sql.*;
import java.lang.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
public class TableValidator
{
private Object object;
private Connection connection;
private List<String> exceptions;
private boolean exceptionFlag;
private DatabaseMetaData databaseMetaData;
private ResultSetMetaData resultSetMetaData;
private ResultSet resultSet;
public TableValidator(Object object,Connection connection) throws ORMException
{
this.object=object;
this.connection=connection;
this.exceptions=new LinkedList<>();
}
public void validate() throws ORMException
{
Annotation [] annotations=object.getClass().getDeclaredAnnotations();
if(annotations.length==0)
{
exceptionFlag=true;
exceptions.add("Annotation table missing on : "+object.getClass().getSimpleName());
return;
}
String annotatedTableName="";
if(annotations[0] instanceof Table)
{
Table table=(Table) annotations[0];
annotatedTableName=table.name();
}
else
{
exceptionFlag=true;
exceptions.add("Invalid annotation on : "+object.getClass().getSimpleName());
return;
}
boolean isTableNameCorrect=verifyTableName(object,annotatedTableName);
if(isTableNameCorrect==false)
{
exceptionFlag=true;
exceptions.add("Invalid table name : "+annotatedTableName+" on annotation table for : "+object.getClass().getSimpleName());
return;
}
}//end of function
public boolean verifyTableName(Object object,String annotatedTableName) throws ORMException
{
boolean tableNameFound=false;
try
{
databaseMetaData=connection.getMetaData();
String tableNames[]={"TABLE"};
resultSet=databaseMetaData.getTables(null,null,"%",tableNames);
while(resultSet.next())
{
//fetch table information on every iteration
String tableName=resultSet.getString("TABLE_NAME");
if(tableName.equals(annotatedTableName)==false) continue;
else
{
tableNameFound=true;
break;
}
}//end of while
}catch(Exception exception)
{
exception.printStackTrace();
exceptionFlag=true;
exceptions.add(exception.getMessage());
return false;
}
return tableNameFound;
}//end of function
public boolean hasException()
{
return this.exceptionFlag;
}
public String getExceptions()
{
StringBuffer exceptionStream=null;
if(exceptionFlag)
{
exceptionStream=new StringBuffer();
for(String exception:exceptions) exceptionStream.append(exception);
}
return exceptionStream.toString();
}
}