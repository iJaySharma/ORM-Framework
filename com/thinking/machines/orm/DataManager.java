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
}// end of save function

public void update(Object object) throws ORMException
{
entity=entityManager.loadEntity(object,OperationType.UPDATE);
updateWrapper=entity.getUpdateWrapper();
Map<Class,Method> psMap=updateWrapper.getPreparedStatementSetterMethods();
try
{
validator=new Validator(object,updateWrapper.getPOJOGetterMethods());
validator.validate(ValidatorType.TABLE);
if(validator.hasException())
{
throw new ORMException(validator.getExceptions());
}
validator.validate(ValidatorType.PRIMARY);
if(validator.hasException())
{
throw new ORMException(validator.getExceptions());
}
validator.validate(ValidatorType.FOREIGN);
if(validator.hasException())
{
throw new ORMException(validator.getExceptions());
}
validator.validate(ValidatorType.COLUMN);
if(validator.hasException())
{
throw new ORMException(validator.getExceptions());
}
//validations complete....
//check for primary key & foreign key starts here...
validateKeysForUpdate(object);
generateUpdateSQLStatement(object,updateWrapper);

//code to update record in db table starts here...
Object psObject=connection.prepareStatement(updateWrapper.getUpdateSQLStatement());
Class psClass=Class.forName(psObject.getClass().getName());
List<Pair<Field,Method>> primaryKeyPOJOGetterMethods=new LinkedList<>();
int psIndex=1;
for(int index=0;index<updateWrapper.getPOJOGetterMethods().size();index++)
{
Field field=updateWrapper.getPOJOGetterMethods().get(index).getKey();
Method method=updateWrapper.getPOJOGetterMethods().get(index).getValue();

Annotation fieldLevelAnnotations[]=field.getDeclaredAnnotations();
boolean isFieldPrimaryKey=false;
for(Annotation fieldLevelAnnotation: fieldLevelAnnotations)
{
if(fieldLevelAnnotation instanceof PrimaryKey)
{
primaryKeyPOJOGetterMethods.add(updateWrapper.getPOJOGetterMethods().get(index));
isFieldPrimaryKey=true;
break;
}
}//end of for
if(isFieldPrimaryKey) continue;
if(psMap.containsKey(field.getType())==false)
{
throw new ORMException("Invalid field type : "+field.getType()+" for field : "+field.getName());
}
psMap.get(field.getType()).invoke(psObject,psIndex,method.invoke(object));
psIndex++;
}//end of for

for(int index=0;index<primaryKeyPOJOGetterMethods.size();index++)
{
Field field=primaryKeyPOJOGetterMethods.get(index).getKey();
Method method=primaryKeyPOJOGetterMethods.get(index).getValue();
psMap.get(field.getType()).invoke(psObject,psIndex,method.invoke(object));
psIndex++;
}//end of for

Method executeUpdateMethod=psClass.getMethod("executeUpdate");
executeUpdateMethod.invoke(psObject);

//code to update record in db table ends here...
}catch(Exception exception)
{
exception.printStackTrace();
}
}// end of update function

private void generateUpdateSQLStatement(Object object,UpdateWrapper updateWrapper) throws ORMException
{
List<ColumnWrapper> columns=updateWrapper.getColumns();
List<ColumnWrapper> primaryKeyColumns=new LinkedList<>();
Integer primaryKeyColumnsCount=updateWrapper.getPrimaryKeyColumnsCount();
Integer commaCount=(columns.size()-primaryKeyColumnsCount)-1;
StringBuffer updateSQLStatement=new StringBuffer();
updateSQLStatement.append("update "+updateWrapper.getTableName()+" set ");
for(ColumnWrapper column:columns)
{
if(column.getIsPrimaryKey()!=null)
{
primaryKeyColumns.add(column);
continue;
}
updateSQLStatement.append(column.getName()+"=?");
if(commaCount>0)
{
updateSQLStatement.append(", ");
commaCount--;
}
}//end of for
updateSQLStatement.append(" where ");
Integer andCount=primaryKeyColumnsCount-1;
for(ColumnWrapper column:primaryKeyColumns)
{
updateSQLStatement.append(column.getName()+"=?");
if(andCount>0) updateSQLStatement.append(" and ");
}//end of for
updateWrapper.setUpdateSQLStatement(updateSQLStatement.toString());
}//end of function...
private void validateKeysForUpdate(Object object) throws ORMException
{
try
{
Field primaryKeyField=null;
Object primaryKeyFieldValue=null;
Field foreignKeyField=null;
Object foreignKeyFieldValue=null;
for(Pair<Field,Method> pair: updateWrapper.getPOJOGetterMethods())
{
//check for the primary key field...
Annotation [] fieldLevelAnnotations=pair.getKey().getDeclaredAnnotations();
if(fieldLevelAnnotations.length==0) throw new ORMException("Annotations Missing on : "+pair.getKey().getName());
primaryKeyField=null;
primaryKeyFieldValue=null;
for(Annotation fieldLevelAnnotation:fieldLevelAnnotations)
{
if(fieldLevelAnnotation instanceof PrimaryKey)
{
primaryKeyField=pair.getKey();
primaryKeyFieldValue=pair.getValue().invoke(object);
break;
}
}//end of for...
//got the field which is primary key annotated
//now check if the corrosponding primary key field value exists in db table
boolean isExists;
if(primaryKeyField!=null)
{
isExists=verifyRecordExistenceWithPrimaryKey(object,primaryKeyField,primaryKeyFieldValue);
if(isExists==false) 
{
throw new ORMException("Record not found with primary key field value : "+primaryKeyField.getName()+" for pojo : "+object.getClass().getSimpleName());
}
}
//check for primary key field complete...

//check for foreign key starts here....
fieldLevelAnnotations=pair.getKey().getDeclaredAnnotations();
if(fieldLevelAnnotations.length==0) throw new ORMException("Annotations Missing on : "+pair.getKey().getName());
foreignKeyField=null;
foreignKeyFieldValue=null;

for(Annotation fieldLevelAnnotation:fieldLevelAnnotations)
{
if(fieldLevelAnnotation instanceof ForeignKey)
{
foreignKeyField=pair.getKey();
foreignKeyFieldValue=pair.getValue().invoke(object);
break;
}
}//end of for...
//got the field which is foreign key annotated
//now check if the corrosponding foreign key field value exists in db table
if(foreignKeyField!=null)
{
isExists=verifyRecordExistenceWithForeignKey(object,foreignKeyField,foreignKeyFieldValue);
if(isExists==false) 
{
throw new ORMException("Record not found with foreign key field value : "+foreignKeyField.getName()+" for pojo : "+object.getClass().getSimpleName());
}
}
}//end of for...
}catch(Exception exception)
{
exception.printStackTrace();
}
}//end of function
private boolean verifyRecordExistenceWithForeignKey(Object object,Field foreignKeyField,Object foreignKeyFieldValue) throws ORMException
{
//code to check record existence in table...
boolean found=false;
Annotation classLevelAnnotations[]=object.getClass().getDeclaredAnnotations();
String childTableName="";
for(Annotation classLevelAnnotation : classLevelAnnotations)
{
if(classLevelAnnotation instanceof Table)
{
Table table=(Table) classLevelAnnotation;
childTableName=table.name();
break;
}
}//end of for
Annotation fieldLevelAnnotations[]=foreignKeyField.getDeclaredAnnotations();
String parentTableName="";
String parentTableColumnName="";
for(Annotation fieldLevelAnnotation: fieldLevelAnnotations)
{
if(fieldLevelAnnotation instanceof ForeignKey)
{
ForeignKey foreignKey=(ForeignKey) fieldLevelAnnotation;
parentTableName=foreignKey.parent();
parentTableColumnName=foreignKey.column();
break;
}
}//end of for
//code to fire query starts here....
try
{
preparedStatement=connection.prepareStatement("select "+parentTableColumnName+" from "+parentTableName+" where "+parentTableColumnName+"="+foreignKeyFieldValue);
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false) found=false;
else found=true;
resultSet.close();
}catch(Exception exception)
{
exception.printStackTrace();
}
//code to fire query ends here....
return found;
}//end of function
private boolean verifyRecordExistenceWithPrimaryKey(Object object,Field primaryKeyField,Object primaryKeyFieldValue) throws ORMException
{
//code to check record existence in table...
boolean found=false;
Annotation classLevelAnnotations[]=object.getClass().getDeclaredAnnotations();
String tableName="";
for(Annotation classLevelAnnotation : classLevelAnnotations)
{
if(classLevelAnnotation instanceof Table)
{
Table table=(Table) classLevelAnnotation;
tableName=table.name();
break;
}
}//end of for
Annotation fieldLevelAnnotations[]=primaryKeyField.getDeclaredAnnotations();
String primaryKeyFieldName="";
for(Annotation fieldLevelAnnotation: fieldLevelAnnotations)
{
if(fieldLevelAnnotation instanceof PrimaryKey)
{
PrimaryKey primaryKey=(PrimaryKey) fieldLevelAnnotation;
primaryKeyFieldName=primaryKey.name();
break;
}
}//end of for
//code to fire query starts here....
try
{
preparedStatement=connection.prepareStatement("select "+primaryKeyFieldName+" from "+tableName+" where "+primaryKeyFieldName+"="+primaryKeyFieldValue);
resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false) found=false;
else found=true;
resultSet.close();
}catch(Exception exception)
{
exception.printStackTrace();
}
//code to fire query ends here....
return found;
}//end of function

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