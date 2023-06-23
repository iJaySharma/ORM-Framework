package com.thinking.machines.orm.validator;
import com.thinking.machines.orm.wrapper.*;
import com.thinking.machines.orm.utilities.*;
import com.thinking.machines.orm.annotation.*;
import com.thinking.machines.orm.exception.*;
import java.util.*;
import java.sql.*;
import javafx.util.*;
import java.lang.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
public class ForeignKeyValidator
{
private List<Pair<Field,Method>> entityMap;
private Object object;
private boolean exceptionFlag;
private List<String> exceptions;
private ForeignKeyWrapper foreignKeyWrapper;
private Connection connection;
private DatabaseMetaData databaseMetaData;
private ResultSetMetaData resultSetMetaData;
private PreparedStatement preparedStatement;
private String entityName;
private ResultSet resultSet;
private Map<String,ForeignKeyWrapper> foreignKeysMap;
public ForeignKeyValidator(Object object,List<Pair<Field,Method>> entityMap,Connection connection)
{
this.object=object;
this.entityMap=entityMap;
this.exceptionFlag=false;
this.exceptions=new ArrayList<String>();
this.entityName=object.getClass().getSimpleName();
this.foreignKeysMap=null;
this.connection=connection;
}
public void validate() throws ORMException
{
Map<String,ForeignKeyWrapper> foreignKeysMap=getForeignKeys();
//validation of foreign keys starts here...
Annotation [] fieldLevelAnnotations=null;
for(Pair<Field,Method> pair:entityMap)
{
fieldLevelAnnotations=pair.getKey().getDeclaredAnnotations();
for(Annotation fieldLevelAnnotation:fieldLevelAnnotations)
{
if(fieldLevelAnnotation instanceof ForeignKey)
{
ForeignKey foreignKey=(ForeignKey) fieldLevelAnnotation;
if(foreignKeysMap.containsKey(pair.getKey().getName())==false)
{
//wrong foreign key annotation placed...
exceptionFlag=true;
exceptions.add("Invalid foreign key annotation on field : "+pair.getKey().getName());
}
String parent=foreignKey.parent();
String column=foreignKey.column();
ForeignKeyWrapper foreignKeyWrapper=foreignKeysMap.get(pair.getKey().getName());
if(foreignKeyWrapper.getParentTableName().equals(parent)==false) 
{
//parent mentioned in @ForeignKey is not matching
exceptionFlag=true;
exceptions.add("Invalid parent in foreign key annotation : "+parent);
}
if(foreignKeyWrapper.getParentTableColumnName().equals(column)==false)
{
//column mentioned in @ForeignKey is not matching
exceptionFlag=true;
exceptions.add("Invalid column in foreign key annotation : "+column);
}
}
}
}
}//end of function
public Map<String,ForeignKeyWrapper> getForeignKeys() throws ORMException
{
foreignKeysMap=new HashMap<>();
//code to fetch foreign keys for columns starts here....
try
{
databaseMetaData=connection.getMetaData();
String tableNames[]={"TABLE"};
resultSet=databaseMetaData.getTables(null,null,"%",tableNames);
while(resultSet.next())
{
//fetch table information on every iteration
String tableName=resultSet.getString("TABLE_NAME");
//System.out.println("Entity Name : "+entityName+" , Table Name : "+tableName);
if(tableName.equalsIgnoreCase(entityName)==false) continue;
//table found for entity...

//extracting foreign keys
ResultSet foreignKeysInformation=databaseMetaData.getImportedKeys(null,null,tableName);
String filteredChildTableColumnName="";
for(int index=0;foreignKeysInformation.next();index++)
{
String parentTableName=foreignKeysInformation.getString("PKTABLE_NAME");
String parentTableColumnName=foreignKeysInformation.getString("PKCOLUMN_NAME");
String childTableName=foreignKeysInformation.getString("FKTABLE_NAME");
String childTableColumnName=foreignKeysInformation.getString("FKCOLUMN_NAME");
/*
System.out.println("Parent Table Name : "+parentTableName+" , Parent Table Column Name : "+parentTableColumnName);
System.out.println("Child Table Name : "+childTableName+" , Child Table Column Name : "+childTableColumnName);
*/
foreignKeyWrapper=new ForeignKeyWrapper();
foreignKeyWrapper.setParentTableName(parentTableName);
foreignKeyWrapper.setParentTableColumnName(parentTableColumnName);
foreignKeyWrapper.setChildTableName(childTableName);
foreignKeyWrapper.setChildTableColumnName(childTableColumnName);
int underscoreIndex=childTableColumnName.indexOf("_");
if(underscoreIndex>0)
{
while(true)
{
underscoreIndex=childTableColumnName.indexOf("_");
if(underscoreIndex<0) break;
filteredChildTableColumnName=childTableColumnName.substring(0,underscoreIndex);
filteredChildTableColumnName+=childTableColumnName.substring(underscoreIndex+1,underscoreIndex+2).toUpperCase();
filteredChildTableColumnName+=childTableColumnName.substring(underscoreIndex+2).toLowerCase();
childTableColumnName=filteredChildTableColumnName;
}//end of while loop
foreignKeysMap.put(filteredChildTableColumnName,foreignKeyWrapper);
}//end of if
else foreignKeysMap.put(childTableColumnName,foreignKeyWrapper);
}//loop to extract foreign keys ends here...
foreignKeysInformation.close();
}//end of resultSet loop
resultSet.close();
}catch(Exception exception)
{
exceptionFlag=true;
exceptions.add(exception.getMessage());
return null;
}
return foreignKeysMap;
}//end of function
public boolean hasException()
{
return this.exceptionFlag;
}
public String getExceptions()
{
StringBuffer exceptionStream=null;
if(this.exceptionFlag)
{
exceptionStream=new StringBuffer();
for(String exception:exceptions) exceptionStream.append(exception);
}
else return "";
return exceptionStream.toString();
}
}