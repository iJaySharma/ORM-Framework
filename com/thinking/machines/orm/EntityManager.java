package com.thinking.machines.orm;
import com.thinking.machines.orm.wrapper.*;
import com.thinking.machines.orm.exception.*;
import java.util.*;
import javafx.util.*;	//for pair
import java.lang.*;
import java.lang.reflect.*;
import java.sql.*;
public class EntityManager
{
private Entity entity;
private Connection connection;
private DatabaseMetaData databaseMetaData;
private ResultSetMetaData resultSetMetaData;
private PreparedStatement preparedStatement;
private ResultSet resultSet;
private ColumnWrapper columnWrapper;
private Object object;
private List<Pair<Field,Method>> pojoGetterMethods;
private Map<Class,Method> preparedStatementSetterMethods;
private List<Pair<Field,Method>> pojoSetterMethods;
private Map<Class,Method> resultSetGetterMethods;
public EntityManager(Connection connection)
{
this.connection=connection;
this.entity=null;
}
public Entity loadEntity(Object object,OperationType operationType) throws ORMException
{
try
{
this.object=object;
entity=new Entity();
entity.setEntityObject(object);
prepareDataStructures(object);
if(operationType.equals(operationType.INSERT))
{
InsertWrapper insertWrapper=getInsertWrapper(object);
entity.setInsertWrapper(insertWrapper);
}
}catch(Exception exception)
{
}
return entity;
}



public InsertWrapper getInsertWrapper(Object object) throws ORMException
{
InsertWrapper insertWrapper=new InsertWrapper();
List<ColumnWrapper> columns=new LinkedList<>();
try
{
databaseMetaData=connection.getMetaData();
String tableNames[]={"TABLE"};
resultSet=databaseMetaData.getTables(null,null,"%",tableNames);
while(resultSet.next())
{
//fetch table information on every iteration
String tableName=resultSet.getString("TABLE_NAME");
if(tableName.equalsIgnoreCase(object.getClass().getSimpleName())==false) continue;
//fetching columns information
ResultSet columnsInformation=databaseMetaData.getColumns(null,null,tableName,null);
while(columnsInformation.next())
{
String columnName=columnsInformation.getString("COLUMN_NAME").toLowerCase();
columnWrapper=new ColumnWrapper();
columnWrapper.setName(columnName);
columns.add(columnWrapper);
}//inner while ends here
}//outer while ends here
}catch(Exception exception)
{
exception.printStackTrace();
}
insertWrapper.setPreparedStatementSetterMethods(preparedStatementSetterMethods);
insertWrapper.setPOJOGetterMethods(pojoGetterMethods);
insertWrapper.setColumns(columns);
return insertWrapper;
}//end of function

private void prepareDataStructures(Object object) throws ORMException
{
try
{
//code to analyze object(class) 
Map<String,Class> typeResolverMap=new HashMap<>();
typeResolverMap.put("int",Class.forName("java.lang.Integer"));
typeResolverMap.put("float",Class.forName("java.lang.Float"));
typeResolverMap.put("long",Class.forName("java.lang.Long"));
typeResolverMap.put("double",Class.forName("java.lang.Double"));
typeResolverMap.put("char",Class.forName("java.lang.String"));
typeResolverMap.put("byte",Class.forName("java.lang.Byte"));
typeResolverMap.put("boolean",Class.forName("java.lang.Boolean"));
typeResolverMap.put("date",Class.forName("java.sql.Date"));

preparedStatementSetterMethods=new HashMap<>();
resultSetGetterMethods=new HashMap<>();
pojoGetterMethods=new LinkedList<>();
pojoSetterMethods=new LinkedList<>();

List<Field> entityFields=new LinkedList<>();
List<Method> entityMethods=new LinkedList<>();
List<Method> entityGetterMethods=new LinkedList<>();
List<Method> entitySetterMethods=new LinkedList<>();

Class c=Class.forName(object.getClass().getName());
Field [] fields=c.getDeclaredFields();
Method [] methods=c.getDeclaredMethods();
for(Field field:fields) entityFields.add(field);

for(Method method:methods)
{
if(method.getName().startsWith("get")) entityMethods.add(method);
}
for(int i=0;i<entityFields.size();i++)
{
String fieldName=entityFields.get(i).getName().toLowerCase();
for(int j=0;j<entityMethods.size();j++)
{
String methodName=entityMethods.get(j).getName().substring(entityMethods.get(j).getName().indexOf("t")+1).toLowerCase();
if(fieldName.equals(methodName)) entityGetterMethods.add(entityMethods.get(j));
}
}
for(int i=0;i<entityFields.size();i++)
{
Pair<Field,Method> pair=new Pair<Field,Method>(entityFields.get(i),entityGetterMethods.get(i));
pojoGetterMethods.add(pair);
}
for(Field field:entityFields)
{
String fieldName=field.getName();
for(Method method:methods)
{
String methodName=method.getName();
if(methodName.startsWith("get")) continue;
if(methodName.substring(methodName.indexOf("t")+1).equalsIgnoreCase(fieldName)==false) continue;
pojoSetterMethods.add(new Pair<Field,Method> (field,method));
}
}
//List -> Pair<Field,Method> is populated

//code to populate resultSetGetterMethods starts here...
Class resultSetClass=Class.forName("java.sql.ResultSet");
Method resultSetMethods[]=resultSetClass.getDeclaredMethods();
for(Method resultSetMethod:resultSetMethods)
{
if(resultSetMethod.getName().startsWith("get")==false) continue;
Class returnTypeClass=null;
if(typeResolverMap.containsKey(resultSetMethod.getReturnType()))
{
returnTypeClass=typeResolverMap.get(resultSetMethod.getReturnType());
}
else returnTypeClass=resultSetMethod.getReturnType();
Class prms[]=resultSetMethod.getParameterTypes();
if(prms.length!=1) continue;
if(prms[0].getSimpleName().equalsIgnoreCase("string")==false) continue;
if(resultSetGetterMethods.containsKey(returnTypeClass)) continue;
resultSetGetterMethods.put(returnTypeClass,resultSetMethod);
}//end of for
//code to populate resultSetGetterMethods ends  here...

//populating for ps list starts here....
Class ps=Class.forName("java.sql.PreparedStatement");
methods=ps.getDeclaredMethods();
for(Method method:methods)
{
if(method.getName().startsWith("set")==false) continue;
if(method.getName().startsWith("setNString")) continue;
Class prms[]=method.getParameterTypes();
int lengthOfPrms=prms.length;
int i=1;
if(lengthOfPrms<=0) continue;
if(typeResolverMap.containsKey(prms[i].toString()))
{
if(preparedStatementSetterMethods.containsKey(typeResolverMap.get(prms[i].toString()))==false)
{
preparedStatementSetterMethods.put(typeResolverMap.get(prms[i].toString()),method);
}
}
else 
{
if(preparedStatementSetterMethods.containsKey(prms[i])==false)
{
preparedStatementSetterMethods.put(prms[i],method);
}
}
}
}catch(Exception exception)
{
exception.printStackTrace();
}
}//end of function
}