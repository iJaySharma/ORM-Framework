package com.thinking.machines.orm.query;
import com.thinking.machines.orm.*;
import com.thinking.machines.orm.exception.*;
import com.thinking.machines.orm.annotation.*;
import com.thinking.machines.orm.wrapper.*;
import java.util.*;
import javafx.util.*;
import java.lang.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.sql.*;
import java.math.*;
public class Select<T> implements QueryImplementor<T>
{
private Connection connection;
private Entity entity;
private boolean whereFlag=true;
private List<Expression<T>> whereExpressions=new LinkedList<>();
private SelectWrapper selectWrapper;
public Select(Connection connection,Entity entity) throws ORMException
{
this.connection=connection;
this.entity=entity;
selectWrapper=entity.getSelectWrapper();
}
public Where<T> where(String propertyName)
{
Clause clause=new Clause();
clause.setLeftOperand(propertyName);
Where<T> where=new Where<T>(this,clause);
return where;
}
public void addExpression(Expression<T> expression)
{
if(whereFlag) whereExpressions.add(expression);
}
public List<T> fire() throws ORMException
{
if(whereExpressions.size()==0) return statementQuery();
else return preparedStatementQuery();
}
public List<T> preparedStatementQuery()
{
List<Pair<Field,Method>> pojoSetterMethods=selectWrapper.getPOJOSetterMethods();
Map<Class,Method> resultSetGetterMethods=selectWrapper.getResultSetGetterMethods();
List<T> lists=new LinkedList<T>();
T entityClass;
PreparedStatement preparedStatement=null;
ResultSet resultSet=null;
StringBuffer selectSQLStatement=null;
for(Expression<T> expression:whereExpressions)
{
selectSQLStatement=getSelectSQLStatementForPreparedStatement(pojoSetterMethods,resultSetGetterMethods,expression);
System.out.println(selectSQLStatement.toString());
Method psMethod=null;
try
{
preparedStatement=connection.prepareStatement(selectSQLStatement.toString());
if(expression.getClause().getRightOperand() instanceof Integer)
{
psMethod=preparedStatement.getClass().getMethod("setInt",int.class,int.class);
}
else if(expression.getClause().getRightOperand() instanceof String)
{
psMethod=preparedStatement.getClass().getMethod("setString",int.class,String.class);
}
else if(expression.getClause().getRightOperand() instanceof Float)
{
psMethod=preparedStatement.getClass().getMethod("setFloat",int.class,float.class);
}
else if(expression.getClause().getRightOperand() instanceof Double)
{
psMethod=preparedStatement.getClass().getMethod("setDouble",int.class,double.class);
}
else if(expression.getClause().getRightOperand() instanceof Boolean)
{
psMethod=preparedStatement.getClass().getMethod("setBoolean",int.class,boolean.class);
}
else if(expression.getClause().getRightOperand() instanceof Short)
{
psMethod=preparedStatement.getClass().getMethod("setShort",int.class,short.class);
}
else if(expression.getClause().getRightOperand() instanceof Byte)
{
psMethod=preparedStatement.getClass().getMethod("setByte",int.class,byte.class);
}
else if(expression.getClause().getRightOperand() instanceof Long)
{
psMethod=preparedStatement.getClass().getMethod("setLong",int.class,long.class);
}
else if(expression.getClause().getRightOperand() instanceof BigDecimal)
{
psMethod=preparedStatement.getClass().getMethod("setBigDecimal",int.class,BigDecimal.class);
}
else if(expression.getClause().getRightOperand() instanceof java.sql.Date)
{
psMethod=preparedStatement.getClass().getMethod("setDate",int.class,java.sql.Date.class);
}
psMethod.invoke(preparedStatement,1,expression.getClause().getRightOperand());
resultSet=preparedStatement.executeQuery();

while(resultSet.next())
{
entityClass=(T) entity.getEntityObject().getClass().newInstance();
for(int index=0;index<pojoSetterMethods.size();index++)
{
Field pojoField=pojoSetterMethods.get(index).getKey();
Method pojoSetterMethod=pojoSetterMethods.get(index).getValue();
Annotation annotation=pojoField.getAnnotation(Column.class);
Column column=(Column) annotation;
String fieldName=column.name();
Method resultSetGetterMethod=null;
if(resultSetGetterMethods.containsKey(pojoField.getType())==false)
{
if(pojoField.getType().getSimpleName().equalsIgnoreCase("integer"))
{
resultSetGetterMethod=resultSetGetterMethods.get(int.class);
}
else if(pojoField.getType().getSimpleName().equalsIgnoreCase("float"))
{
resultSetGetterMethod=resultSetGetterMethods.get(float.class);
}
else if(pojoField.getType().getSimpleName().equalsIgnoreCase("long"))
{
resultSetGetterMethod=resultSetGetterMethods.get(long.class);
}
else if(pojoField.getType().getSimpleName().equalsIgnoreCase("double"))
{
resultSetGetterMethod=resultSetGetterMethods.get(double.class);
}
else if(pojoField.getType().getSimpleName().equalsIgnoreCase("boolean"))
{
resultSetGetterMethod=resultSetGetterMethods.get(boolean.class);
}
else if(pojoField.getType().getSimpleName().equalsIgnoreCase("short"))
{
resultSetGetterMethod=resultSetGetterMethods.get(short.class);
}
else if(pojoField.getType().getSimpleName().equalsIgnoreCase("float"))
{
resultSetGetterMethod=resultSetGetterMethods.get(float.class);
}
}
else resultSetGetterMethod=resultSetGetterMethods.get(pojoField.getType());
Class prms[]=resultSetGetterMethod.getParameterTypes();
pojoSetterMethod.invoke(entityClass,resultSetGetterMethod.invoke(resultSet,fieldName));
}//end of for...
lists.add(entityClass);
}//end of while...
}catch(Exception exception)
{
exception.printStackTrace();
}
}
return lists;
}
public StringBuffer getSelectSQLStatementForPreparedStatement(List<Pair<Field,Method>> pojoSetterMethods,Map<Class,Method> resultSetGetterMethods,Expression<T> expression)
{
StringBuffer selectSQLStatement=new StringBuffer();
selectSQLStatement.append("select * from ");
selectSQLStatement.append(selectWrapper.getTableName());
Clause clause=expression.getClause();
selectSQLStatement.append(" where ");
selectSQLStatement.append(clause.getLeftOperand());
selectSQLStatement.append(Operators.getOperator(clause.getOperator()));
selectSQLStatement.append("?");
return selectSQLStatement;
}//end of function
public List<T> statementQuery()
{
List<Pair<Field,Method>> pojoSetterMethods=selectWrapper.getPOJOSetterMethods();
Map<Class,Method> resultSetGetterMethods=selectWrapper.getResultSetGetterMethods();
Statement statement;
ResultSet resultSet;
List<T> lists=new LinkedList<T>();
try
{
statement=connection.createStatement();
resultSet=statement.executeQuery("select * from "+selectWrapper.getTableName());
Object entityObject=entity.getEntityObject();
T entityClass;
while(resultSet.next())
{
entityClass=(T) entityObject.getClass().newInstance();
for(int index=0;index<pojoSetterMethods.size();index++)
{
Field pojoField=pojoSetterMethods.get(index).getKey();
Method pojoSetterMethod=pojoSetterMethods.get(index).getValue();
Annotation annotation=pojoField.getAnnotation(Column.class);
Column column=(Column) annotation;
String fieldName=column.name();
Method resultSetGetterMethod=null;
if(resultSetGetterMethods.containsKey(pojoField.getType())==false)
{
if(pojoField.getType().getSimpleName().equalsIgnoreCase("integer"))
{
resultSetGetterMethod=resultSetGetterMethods.get(int.class);
}
else if(pojoField.getType().getSimpleName().equalsIgnoreCase("float"))
{
resultSetGetterMethod=resultSetGetterMethods.get(float.class);
}
else if(pojoField.getType().getSimpleName().equalsIgnoreCase("long"))
{
resultSetGetterMethod=resultSetGetterMethods.get(long.class);
}
else if(pojoField.getType().getSimpleName().equalsIgnoreCase("double"))
{
resultSetGetterMethod=resultSetGetterMethods.get(double.class);
}
else if(pojoField.getType().getSimpleName().equalsIgnoreCase("boolean"))
{
resultSetGetterMethod=resultSetGetterMethods.get(boolean.class);
}
else if(pojoField.getType().getSimpleName().equalsIgnoreCase("short"))
{
resultSetGetterMethod=resultSetGetterMethods.get(short.class);
}
else if(pojoField.getType().getSimpleName().equalsIgnoreCase("float"))
{
resultSetGetterMethod=resultSetGetterMethods.get(float.class);
}
}
else resultSetGetterMethod=resultSetGetterMethods.get(pojoField.getType());
Class prms[]=resultSetGetterMethod.getParameterTypes();
pojoSetterMethod.invoke(entityClass,resultSetGetterMethod.invoke(resultSet,fieldName));
}//end of for...
lists.add(entityClass);
}//end of while...
}catch(Exception exception)
{
exception.printStackTrace();
}
return lists;
}
}