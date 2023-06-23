package com.thinking.machines.orm.validator;
import com.thinking.machines.orm.exception.*;
import com.thinking.machines.orm.wrapper.*;
import com.thinking.machines.orm.annotation.*;
import com.thinking.machines.orm.utilities.*;
import java.util.*;
import javafx.util.*;
import java.lang.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.sql.*;
public class PrimaryKeyValidator
{
private Connection connection;
private ConfigurationWrapper configuration;
private DatabaseMetaData databaseMetaData;
private ResultSetMetaData resultSetMetaData;
private ResultSet resultSet;
private boolean exceptionFlag;
private List<String> exceptions;
private List<Pair<Field,Method>> entityMap;		//pojoGetterMethods
private Map<String,ColumnWrapper> tableColumnsMap;
private Object object;
public PrimaryKeyValidator(Object object,List<Pair<Field,Method>> entityMap,Connection connection)
{
this.object=object;
this.entityMap=entityMap;
this.exceptions=new ArrayList<String>();
this.connection=connection;
}
public void validate() throws ORMException
{
List<ColumnWrapper> primaryKeyColumns=getPrimaryKeyColumns(object);
if(exceptionFlag) return;
if(primaryKeyColumns.size()==0) return;
Map<String,ColumnWrapper> primaryKeyColumnsMap=new HashMap<>();
for(ColumnWrapper columnWrapper:primaryKeyColumns) primaryKeyColumnsMap.put(columnWrapper.getName(),columnWrapper);
//primaryKey fetching done here...
for(Pair<Field,Method> pairOfField:entityMap)
{
Field field=pairOfField.getKey();
Annotation [] fieldLevelAnnotations=field.getDeclaredAnnotations();
for(Annotation fieldLevelAnnotation:fieldLevelAnnotations)
{
if(fieldLevelAnnotation instanceof PrimaryKey)
{
PrimaryKey primaryKey=(PrimaryKey) fieldLevelAnnotation;
String givenPrimaryKeyName=primaryKey.name();
if(primaryKeyColumnsMap.containsKey(field.getName())==false)
{
exceptionFlag=true;
exceptions.add("Invalid Primary Key Annotation on : "+field.getName()+"\n");
break;
}
else
{
if(tableColumnsMap.containsKey(givenPrimaryKeyName)==false)
{
exceptionFlag=true;
exceptions.add("Invalid Primary Key Name : "+givenPrimaryKeyName+" on "+field.getName());
break;
}
}
}
}
}
}//end of function
public List<ColumnWrapper> getPrimaryKeyColumns(Object object) throws ORMException
{
List<ColumnWrapper> columns=new ArrayList<>();
tableColumnsMap=new HashMap<>();
Class c=object.getClass();
Annotation annotations[]=c.getDeclaredAnnotations();
String classLevelTableName="";
//extracting table name from @Table annotation (class level annotation)
for(Annotation annotation:annotations)
{
if(annotation instanceof Table)
{
Table tableAnnotation=(Table) annotation;
classLevelTableName=tableAnnotation.name();
break;
}
}
try
{
databaseMetaData=connection.getMetaData();
String tableNames[]={"TABLE"};
resultSet=databaseMetaData.getTables(null,null,"%",tableNames);
while(resultSet.next())
{
//fetch table information on every iteration
String tableName=resultSet.getString("TABLE_NAME");
if(tableName.equalsIgnoreCase(classLevelTableName)==false) continue;
//fetching columns information
ResultSet primaryKeysInformation=databaseMetaData.getPrimaryKeys(null,null,tableName);
ColumnWrapper columnWrapper;
for(int index=0;primaryKeysInformation.next();index++)
{
String columnName=primaryKeysInformation.getString("COLUMN_NAME");
String primaryKeyName=primaryKeysInformation.getString("PK_NAME");
if(primaryKeyName.equalsIgnoreCase("primary"))
{
columnWrapper=new ColumnWrapper();
columnWrapper.setIsPrimaryKey(true);
columnWrapper.setName(columnName);
columns.add(columnWrapper);
tableColumnsMap.put(columnName,columnWrapper);
}
}//loop to extract primary keys ends here...
primaryKeysInformation.close();
}//end of loop
if(tableColumnsMap.size()==0)
{
exceptionFlag=true;
exceptions.add("Invalid table name annotated\n");
return null;
}
//since attribute names in table are containing '_' so we have to filter out that also...
for(ColumnWrapper column: columns)
{
String columnName=column.getName();
String filteredColumnName="";
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
}//end of while
column.setName(filteredColumnName);
}//end of if
}//end of for
}catch(Exception exception)
{
exceptionFlag=true;
exceptions.add(exception.getMessage());
exceptions.add("\n");
return null;
}
return columns;
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