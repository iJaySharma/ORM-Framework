package com.thinking.machines.orm.generator;
import com.thinking.machines.orm.wrapper.*;
import com.thinking.machines.orm.utilities.*;
import com.thinking.machines.orm.exceptions.*;
import com.google.gson.*;
import java.util.*;
import java.io.*;
import java.sql.*;
import java.lang.reflect.*;
import java.lang.*;
public class POJOGenerator
{
private Connection connection;
private ConfigurationWrapper configuration;
private DatabaseMetaData databaseMetaData;
private ResultSetMetaData resultSetMetaData;
private PreparedStatement preparedStatement;
private ResultSet resultSet;
private ResultSet resultSetForTable;
private TableWrapper tableWrapper;
private ColumnWrapper columnWrapper;
private ForeignKeyWrapper foreignKeyWrapper;
private List<TableWrapper> tables;
private List<ColumnWrapper> columns;
private RandomAccessFile raf;
private File file;
private List<TableWrapper> filteredTables;
private StringBuffer sb;
public POJOGenerator()
{
this.tables=new ArrayList<>();
}
public void populate() throws ORMException
{
try
{
connection=ConnectionUtility.getConnection();
}catch(Exception exception)
{
throw new ORMException("Problem while establishing connection");
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
columns=new ArrayList<>();

//fetching columns information
ResultSet columnsInformation=databaseMetaData.getColumns(null,null,tableName,null);
while(columnsInformation.next())
{
String columnName=columnsInformation.getString("COLUMN_NAME").toLowerCase();
//String columnDataType=columnsInformation.getString("DATA_TYPE").toLowerCase();
String columnSize=columnsInformation.getString("COLUMN_SIZE").toLowerCase();
String isNullable=columnsInformation.getString("IS_NULLABLE");
String isAutoIncrement=columnsInformation.getString("IS_AUTOINCREMENT");
columnWrapper=new ColumnWrapper();
columnWrapper.setName(columnName);
columnWrapper.setSize(Long.parseLong(columnSize));
if(isNullable.equalsIgnoreCase("yes"))  columnWrapper.setIsNull(true);
else columnWrapper.setIsNull(false);
if(isAutoIncrement.equalsIgnoreCase("yes")) columnWrapper.setIsAutoIncrement(true);
else columnWrapper.setIsAutoIncrement(false);
columns.add(columnWrapper);
//System.out.println("Column name : "+columnName+" , Column Size : "+columnSize+" , Is Nullable : "+isNullable+" , IsAutoIncrement : "+isAutoIncrement);
}//loop to extract columnsInformation ends here...


//extracting columnDataTypes...
preparedStatement=connection.prepareStatement("select * from "+tableName);
resultSetForTable=preparedStatement.executeQuery();
resultSetMetaData=resultSetForTable.getMetaData();
for(int index=1;index<=resultSetMetaData.getColumnCount();index++)
{
String columnDataType=resultSetMetaData.getColumnTypeName(index).trim().toLowerCase();
columns.get(index-1).setDataType(columnDataType);
}//loop to extract columnDataTypesInformation ends here...


//extracting primary keys
ResultSet primaryKeysInformation=databaseMetaData.getPrimaryKeys(null,null,tableName);
for(ColumnWrapper column: columns)
{
column.setIsForeignKey(false);
column.setIsPrimaryKey(false);
if(column.getIsAutoIncrement()==null) column.setIsAutoIncrement(false);
}
for(int index=0;primaryKeysInformation.next();index++)
{
String columnName=primaryKeysInformation.getString("COLUMN_NAME");
String primaryKeyName=primaryKeysInformation.getString("PK_NAME");
if(primaryKeyName.equalsIgnoreCase("primary")) columns.get(index).setIsPrimaryKey(true);
}//loop to extract primary keys ends here...
primaryKeysInformation.close();

//extracting foreign keys
ResultSet foreignKeysInformation=databaseMetaData.getImportedKeys(null,null,tableName);
for(int index=0;foreignKeysInformation.next();index++)
{
String parentTableName=foreignKeysInformation.getString("PKTABLE_NAME");
String parentTableColumnName=foreignKeysInformation.getString("PKCOLUMN_NAME");
String childTableName=foreignKeysInformation.getString("FKTABLE_NAME");
String childTableColumnName=foreignKeysInformation.getString("FKCOLUMN_NAME");
foreignKeyWrapper=new ForeignKeyWrapper();
foreignKeyWrapper.setParentTableName(parentTableName);
foreignKeyWrapper.setParentTableColumnName(parentTableColumnName);
foreignKeyWrapper.setChildTableName(childTableName);
foreignKeyWrapper.setChildTableColumnName(childTableColumnName);
//System.out.println("Child Table Column Name : "+childTableColumnName);
if(tableName.equals(childTableName))
{
int x=0;
for(ColumnWrapper cw: columns)
{
if(cw.getName().equals(childTableColumnName))
{
columns.get(x).setIsForeignKey(true);
columns.get(x).setForeignKeyWrapper(foreignKeyWrapper);
}
else columns.get(x).setIsForeignKey(false);
x++;
}
}
}//loop to extract foreign keys ends here...
tableWrapper=new TableWrapper();
tableWrapper.setName(tableName);
tableWrapper.setColumnWrappers(columns);
tables.add(tableWrapper);
}//resultSet loop ends here...
}catch(Exception exception)
{
exception.printStackTrace();
throw new ORMException("Problem while fetching database meta data");
}
populateFilteredData();
}//end of function
public void populateFilteredData() throws ORMException
{
filteredTables=new ArrayList<>();
List<ColumnWrapper> filteredColumns;
TableWrapper filteredTable;
ColumnWrapper filteredColumn;
int underscoreIndex;
for(TableWrapper table: tables)
{
filteredTable=new TableWrapper();
String tableName=table.getName();
String filteredTableName="";
underscoreIndex=tableName.indexOf("_");
if(underscoreIndex>0)
{
while(true)
{
underscoreIndex=tableName.indexOf("_");
if(underscoreIndex<0) break;
filteredTableName=tableName.substring(0,underscoreIndex);
filteredTableName+=tableName.substring(underscoreIndex+1,underscoreIndex+2).toUpperCase();
filteredTableName+=tableName.substring(underscoreIndex+2).toLowerCase();
tableName=filteredTableName;
}//end of while loop
}//end of if
else
{
filteredTableName=tableName.toLowerCase();
}
filteredTableName=filteredTableName.substring(0,1).toUpperCase()+filteredTableName.substring(1).toLowerCase();
filteredColumns=new ArrayList<>();
for(ColumnWrapper column: table.getColumnWrappers())
{
filteredColumn=new ColumnWrapper();
String columnName=column.getName();
String filteredColumnName="";
underscoreIndex=columnName.indexOf("_");
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
}//end of if
else
{
filteredColumnName=columnName.toLowerCase();
}
String filteredColumnDataType="";
String columnDataType=column.getDataType();
if(columnDataType.equalsIgnoreCase("int")) filteredColumnDataType="Integer";
else if(columnDataType.equalsIgnoreCase("bigdecimal")) filteredColumnDataType="java.math.BigDecimal";
else if(columnDataType.equalsIgnoreCase("char") || columnDataType.equalsIgnoreCase("varchar")) filteredColumnDataType="String";
else if(columnDataType.equalsIgnoreCase("long")) filteredColumnDataType="Long";
else if(columnDataType.equalsIgnoreCase("date")) filteredColumnDataType="java.sql.Date";

filteredColumn.setName(filteredColumnName);
filteredColumn.setSize(column.getSize());
filteredColumn.setDataType(filteredColumnDataType);
filteredColumn.setIsNull(column.getIsNull());
filteredColumn.setIsAutoIncrement(column.getIsAutoIncrement());
filteredColumn.setIsPrimaryKey(column.getIsPrimaryKey());
filteredColumn.setIsForeignKey(column.getIsForeignKey());
filteredColumn.setForeignKeyWrapper(column.getForeignKeyWrapper());
filteredColumns.add(filteredColumn);
}//end of column wrapper for
filteredTable.setName(filteredTableName);
filteredTable.setColumnWrappers(filteredColumns);
filteredTables.add(filteredTable);
}//end of table wrapper for
//display();
generatePOJO();
}//end of function
public void display()
{
for(TableWrapper table:tables)
{
System.out.println("----------Table-------------");
System.out.println(table.getName());
System.out.println("-----------Columns---------");
for(ColumnWrapper column:table.getColumnWrappers())
{
System.out.println("Name : "+column.getName());
System.out.println("Size : "+column.getSize());
System.out.println("Data Type : "+column.getDataType());
System.out.println("IsPrimaryKey : "+column.getIsPrimaryKey());
System.out.println("IsAutoIncrement : "+column.getIsAutoIncrement());
System.out.println("IsForeignKey : "+column.getIsForeignKey());

}
}
}
public void generatePOJO() throws ORMException
{
String filteredClassName="";
String className="";
TableWrapper table;
TableWrapper filteredTable;
ColumnWrapper column;
ColumnWrapper filteredColumn;
File file=null;
RandomAccessFile raf=null;
sb=new StringBuffer();
for(int index=0;index<filteredTables.size();index++)
{
table=tables.get(index);
filteredTable=filteredTables.get(index);
className='"'+table.getName()+'"';
filteredClassName=filteredTable.getName();
try
{
configuration=ConfigurationReaderUtility.getConfigurations();
file=new File(configuration.getPath()+File.separator+filteredClassName+".java");
if(file.exists()) file.delete();
raf=new RandomAccessFile(file,"rw");
sb.append("import com.thinking.machines.orm.annotations.*;");
sb.append("\r\n");
sb.append("@Table(name="+className+")");
sb.append("\r\n");
sb.append("public class "+filteredClassName+"\r\n");
sb.append("{\r\n");
//declare fields
for(int j=0;j<filteredTable.getColumnWrappers().size();j++)
{
//check for primary key & auto increment
column=table.getColumnWrappers().get(j);
filteredColumn=filteredTable.getColumnWrappers().get(j);
String filteredColumnName=filteredColumn.getName();
String columnName=column.getName();
//if the fetched column is having primary key constraint
if(filteredColumn.getIsPrimaryKey()) 
{
//is primary key
String primaryKeyAnnotationColumn='"'+columnName+'"';
sb.append("@PrimaryKey(name="+primaryKeyAnnotationColumn+")");
sb.append("\r\n");
}
if(filteredColumn.getIsAutoIncrement())
{
//is auto increment
String autoIncrementAnnotationColumn='"'+columnName+'"';
sb.append("@AutoIncrement(name="+autoIncrementAnnotationColumn+")");
sb.append("\r\n");
}
//checking if the columnName is foreign key
if(filteredColumn.getIsForeignKey())
{
ForeignKeyWrapper foreignKeyWrapper=filteredColumn.getForeignKeyWrapper();
String foreignKeyParentTableName='"'+foreignKeyWrapper.getParentTableName()+'"';
String foreignKeyParentTableColumnName='"'+foreignKeyWrapper.getParentTableColumnName()+'"';
String foreignKeyAnnotationColumn="@ForeignKey(parent="+foreignKeyParentTableName+",column="+foreignKeyParentTableColumnName+")";
sb.append(foreignKeyAnnotationColumn);
sb.append("\r\n");
}
//appending fields...
String columnAnnotation='"'+columnName+'"';
sb.append("@Column(name="+columnAnnotation+")\r\n");
sb.append("private "+filteredColumn.getDataType()+" "+filteredColumn.getName()+";");
sb.append("\r\n");
}
//appending constructor...
sb.append("public "+filteredClassName+"()\r\n{\r\n}\r\n");
for(int j=0;j<filteredTable.getColumnWrappers().size();j++)
{
filteredColumn=filteredTable.getColumnWrappers().get(j);
String filteredColumnName=filteredColumn.getName();
String filteredColumnDataType=filteredColumn.getDataType();
//generating setters & getters
sb.append("public void  set"+filteredColumnName.substring(0,1).toUpperCase()+filteredColumnName.substring(1)+"("+filteredColumnDataType+" "+filteredColumnName+")\r\n{\r\n");
sb.append("this."+filteredColumnName+"="+filteredColumnName+";\r\n}\r\n");
sb.append("public "+filteredColumnDataType+" get"+filteredColumnName.substring(0,1).toUpperCase()+filteredColumnName.substring(1)+"()\r\n{\r\n");
sb.append("return this."+filteredColumnName+";\r\n}\r\n");
}
sb.append("}");
raf.writeBytes(sb.toString());
sb.delete(0,sb.length());
raf.close();
}catch(Exception exception)
{
exception.printStackTrace();
throw new ORMException("Problem while creating POJO file");
}
}//end of for
}//end of function
}//end of class