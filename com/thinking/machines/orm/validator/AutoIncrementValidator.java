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
import java.math.*;
public class AutoIncrementValidator
{
private Object object;
private List<Pair<Field,Method>> entityMap;
private Connection connection;
private List<String> exceptions;
private boolean exceptionFlag;
private DatabaseMetaData databaseMetaData;
private ResultSetMetaData resultSetMetaData;
private ResultSet resultSet;
public AutoIncrementValidator(Object object,List<Pair<Field,Method>> entityMap,Connection connection) throws ORMException
{
this.object=object;
this.entityMap=entityMap;
this.connection=connection;
this.exceptions=new LinkedList<>();
}
public void validate() throws ORMException
{
Map<String,ColumnWrapper> autoIncrementColumnsMap=getAutoIncrementedColumnsMap(object);
for(Pair<Field,Method> pair: entityMap)
{
Field field=pair.getKey();
Method method=pair.getValue();
Annotation fieldLevelAnnotations[]=field.getDeclaredAnnotations();
if(fieldLevelAnnotations.length==0) continue;
for(Annotation fieldLevelAnnotation:fieldLevelAnnotations)
{
if(fieldLevelAnnotation instanceof AutoIncrement)
{
AutoIncrement autoIncrement=(AutoIncrement) fieldLevelAnnotation;
if(autoIncrementColumnsMap.containsKey(field.getName())==false) 
{
exceptionFlag=true;
exceptions.add("Invalid auto increment field : "+field.getName());
return;
}
ColumnWrapper columnWrapper=autoIncrementColumnsMap.get(field.getName());
if(columnWrapper.getName().equals(autoIncrement.name())==false)
{
exceptionFlag=true;
exceptions.add("Invalid auto increment name : "+autoIncrement.name());
return;
}
}
}
}
}//end of function
public Map<String,ColumnWrapper> getAutoIncrementedColumnsMap(Object object) throws ORMException
{
Map<String,ColumnWrapper> autoIncrementColumnsMap=new HashMap<>();
ColumnWrapper columnWrapper=null;
try
{
databaseMetaData=connection.getMetaData();
String tableNames[]={"TABLE"};
resultSet=databaseMetaData.getTables(null,null,"%",tableNames);
String classLevelTableName=object.getClass().getSimpleName();
while(resultSet.next())
{
//fetch table information on every iteration
String tableName=resultSet.getString("TABLE_NAME");
if(tableName.equalsIgnoreCase(classLevelTableName)==false) continue;
//fetching columns information
ResultSet columnsInformation=databaseMetaData.getColumns(null,null,tableName,null);
String filteredColumnName="";
while(columnsInformation.next())
{
String columnName=columnsInformation.getString("COLUMN_NAME").toLowerCase();
String isAutoIncrement=columnsInformation.getString("IS_AUTOINCREMENT");
if(isAutoIncrement.equalsIgnoreCase("yes"))
{
columnWrapper=new ColumnWrapper();
columnWrapper.setName(columnName);
columnWrapper.setIsAutoIncrement(true);
int underscoreIndex=columnName.indexOf("_");
if(underscoreIndex>0)
{
while(true)
{
underscoreIndex=columnName.indexOf("_");
if(underscoreIndex<0) break;
filteredColumnName=columnName.substring(0,underscoreIndex);
filteredColumnName+=columnName.substring(underscoreIndex+1,underscoreIndex+2).toUpperCase();
filteredColumnName+=columnName.substring(underscoreIndex+2).toLowerCase();
columnName=filteredColumnName;
}//end of while loop
autoIncrementColumnsMap.put(filteredColumnName,columnWrapper);
}//end of if
else autoIncrementColumnsMap.put(columnName,columnWrapper);
}
}//loop to extract columnsInformation ends here...
}//end of while
}catch(Exception exception)
{
exception.printStackTrace();
exceptionFlag=true;
exceptions.add(exception.getMessage());
return null;
}
return autoIncrementColumnsMap;
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