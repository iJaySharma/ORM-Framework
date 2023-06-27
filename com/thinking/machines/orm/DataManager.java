package com.thinking.machines.orm;
import com.thinking.machines.orm.exception.*;
import com.thinking.machines.orm.wrapper.*;
import com.thinking.machines.orm.utilities.*;
import com.thinking.machines.orm.generator.*;
import com.thinking.machines.orm.annotation.*;
import com.thinking.machines.orm.validator.*;
import java.sql.*;
import java.util.*;
import javafx.util.*;	//for pair
import java.lang.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
public class DataManager
{
private Connection connection;
private PreparedStatement preparedStatement;
private ResultSet resultSet;
private EntityManager entityManager;
private Entity entity;
private InsertWrapper insertWrapper;
private UpdateWrapper updateWrapper;
private DeleteWrapper deleteWrapper;
private StringBuffer stringBuffer;
private Validator validator;
private Map<String,Field> ignoredFields;
public DataManager()
{
this.connection=null;
this.preparedStatement=null;
this.ignoredFields=new HashMap<>();
}
public void begin() throws ORMException
{
connection=ConnectionUtility.getConnection();
if(connection==null) throw new ORMException("Can't establish connection with specified details");
this.entityManager=new EntityManager(connection);
try
{
connection.setAutoCommit(false);
}catch(SQLException sqlException)
{
throw new ORMException(sqlException.getMessage());
}
}
public Integer save(Object object) throws ORMException
{
entity=entityManager.loadEntity(object,OperationType.INSERT);
insertWrapper=entity.getInsertWrapper();
generateInsertSQLStatement(object,insertWrapper);
List<Pair<Field,Method>> pojoGetterMethods=insertWrapper.getPOJOGetterMethods();
Map<Class,Method> psMap=insertWrapper.getPreparedStatementSetterMethods();
PreparedStatement preparedStatement;
try
{
validator=new Validator(object,pojoGetterMethods);
validator.validate(ValidatorType.TABLE);
if(validator.hasException())
{
throw new ORMException(validator.getExceptions());
}
validator.validate(ValidatorType.PRIMARY);
if(validator.hasException())
{
//some serious issue while validating primary key is encountered...
throw new ORMException(validator.getExceptions());
}
validator.validate(ValidatorType.FOREIGN);
if(validator.hasException())
{
throw new ORMException(validator.getExceptions());
}
validator.validate(ValidatorType.AUTOINCREMENT);
if(validator.hasException())
{
throw new ORMException(validator.getExceptions());
}
validator.validate(ValidatorType.COLUMN);
if(validator.hasException())
{
throw new ORMException(validator.getExceptions());
}
Object psObject=connection.prepareStatement(insertWrapper.getSQLStatement());
Class psClass=Class.forName(psObject.getClass().getName());
int psIndex=0;
for(int i=0;i<pojoGetterMethods.size();i++)
{
Field field=pojoGetterMethods.get(i).getKey();
if(ignoredFields.containsKey(field.getName())) continue;
Method pojoGetterMethod=pojoGetterMethods.get(i).getValue();
Method psSetterMethod=psMap.get(field.getType());
psSetterMethod.invoke(psObject,psIndex+1,pojoGetterMethod.invoke(object));
psIndex++;
}
Method executeUpdateMethod=psClass.getMethod("executeUpdate");
executeUpdateMethod.invoke(psObject);
}catch(Exception exception)
{
exception.printStackTrace();
}
return 0;
}
public void end() throws ORMException
{
if(connection!=null)
{
try
{
connection.commit();
connection.close();
connection=null;
}catch(SQLException sqlException)
{
throw new ORMException(sqlException.getMessage());
}
}
}//end of function
private void generateInsertSQLStatement(Object object,InsertWrapper insertWrapper) throws ORMException
{
try
{
List<ColumnWrapper> columns=insertWrapper.getColumns();
List<Pair<Field,Method>> pojoGetterMethods=insertWrapper.getPOJOGetterMethods();
Class c=object.getClass();
Annotation classLevelAnnotations[]=c.getAnnotations();
String tableName="";
for(Annotation classLevelAnnotation:classLevelAnnotations)
{
if(classLevelAnnotation instanceof Table)
{
Table table=(Table) classLevelAnnotation;
tableName=table.name();
}
}
stringBuffer=new StringBuffer();
stringBuffer.append("insert into "+tableName+" (");
int fieldsLength=columns.size();
int comma=0;
int questionMark=0;
int index=0;
for(ColumnWrapper column:columns)
{
boolean isAutoIncrementField=false;
boolean includeInStatement=false;
Field field=pojoGetterMethods.get(index).getKey();
Annotation fieldLevelAnnotations[]=field.getDeclaredAnnotations();
for(Annotation fieldLevelAnnotation : fieldLevelAnnotations)
{
if(fieldLevelAnnotation instanceof AutoIncrement)
{
isAutoIncrementField=true;
AutoIncrement autoIncrement=(AutoIncrement) fieldLevelAnnotation;
Method getterMethodForAutoIncrementedField=pojoGetterMethods.get(index).getValue();
Object autoIncrementedFieldValue=getterMethodForAutoIncrementedField.invoke(object);
if(autoIncrementedFieldValue!=null) 
{
includeInStatement=true;
break;
}
}
}
if(isAutoIncrementField && includeInStatement==false)
{
ignoredFields.put(field.getName(),field);
index++;
comma++;
continue;
}
index++;
stringBuffer.append(column.getName().trim());
if(comma==fieldsLength-1) break;
stringBuffer.append(",");
comma++;
}
if(ignoredFields.size()==0) questionMark=comma;
else questionMark=comma-1;
comma=0;
stringBuffer.append(") values (");
while(comma<=questionMark)
{
stringBuffer.append("?");
if(comma==questionMark) break;
stringBuffer.append(",");
comma++;
}
stringBuffer.append(")");
insertWrapper.setSQLStatement(stringBuffer.toString());
}catch(Exception exception)
{
exception.printStackTrace();
throw new ORMException(exception.getMessage());
}
}
}