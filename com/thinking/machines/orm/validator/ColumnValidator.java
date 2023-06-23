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
public class ColumnValidator
{
private Object object;
private List<Pair<Field,Method>> entityMap;
private Connection connection;
private List<String> exceptions;
private boolean exceptionFlag;
private DatabaseMetaData databaseMetaData;
private ResultSetMetaData resultSetMetaData;
private ResultSet resultSet;
public ColumnValidator(Object object,List<Pair<Field,Method>> entityMap,Connection connection) throws ORMException
{
this.object=object;
this.entityMap=entityMap;
this.connection=connection;
this.exceptions=new LinkedList<>();
}
public void validate() throws ORMException
{
Map<String,ColumnWrapper> columnsMap=getColumnsMap(object);
for(Pair<Field,Method> pair:entityMap)
{
Field field=pair.getKey();
Method method=pair.getValue();
Annotation [] fieldLevelAnnotations=field.getDeclaredAnnotations();
boolean isColumnAnnotated=false;
String fieldName=field.getName();
for(Annotation fieldLevelAnnotation: fieldLevelAnnotations)
{
if(fieldLevelAnnotation instanceof Column)
{
isColumnAnnotated=true;
Column column=(Column) fieldLevelAnnotation;
if(columnsMap.containsKey(field.getName())==false)
{
exceptionFlag=true;
exceptions.add("Invalid column annotation name : "+field.getName()+" on : "+field.getName()+" for : "+object.getClass().getSimpleName());
return;
}
if(column.name().equals(columnsMap.get(field.getName()).getName())==false)
{
exceptionFlag=true;
exceptions.add("Invalid column annotation name : "+column.name()+" on : "+field.getName()+" for : "+object.getClass().getSimpleName());
return;
}
}
}//end of loop
if(isColumnAnnotated==false)
{
exceptionFlag=true;
exceptions.add("Column annotation missing on : "+fieldName+" on : "+object.getClass().getSimpleName());
return;
}
}
}//end of function
public Map<String,ColumnWrapper> getColumnsMap(Object object) throws ORMException
{
Map<String,ColumnWrapper> columnsMap=new HashMap<>();
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
columnWrapper=new ColumnWrapper();
columnWrapper.setName(columnName);
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
columnsMap.put(filteredColumnName,columnWrapper);
}//end of if
else columnsMap.put(columnName,columnWrapper);
}//loop to extract columnsInformation ends here...
}//end of while
}catch(Exception exception)
{
exception.printStackTrace();
exceptionFlag=true;
exceptions.add(exception.getMessage());
return null;
}
return columnsMap;
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