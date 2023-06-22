package com.thinking.machines.orm.wrapper;
import javafx.util.*;
import java.util.*;
import java.lang.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
public class SelectWrapper
{
private String tableName;
private List<Pair<Field,Method>> pojoGetterMethods;
private Map<Class,Method> preparedStatementSetterMethods;
private List<Pair<Field,Method>> pojoSetterMethods;
private Map<Class,Method> resultSetGetterMethods;
public SelectWrapper()
{
}
public void setTableName(String tableName)
{
this.tableName=tableName;
}
public String getTableName()
{
return this.tableName;
}
public void setPOJOGetterMethods(List<Pair<Field,Method>> pojoGetterMethods)
{
this.pojoGetterMethods=pojoGetterMethods;
}
public List<Pair<Field,Method>> getPOJOGetterMethods()
{
return this.pojoGetterMethods;
}
public void setPreparedStatementSetterMethods(Map<Class,Method> preparedStatementSetterMethods)
{
this.preparedStatementSetterMethods=preparedStatementSetterMethods;
}
public Map<Class,Method> getPreparedStatementSetterMethods()
{
return this.preparedStatementSetterMethods;
}
public void setPOJOSetterMethods(List<Pair<Field,Method>> pojoSetterMethods)
{
this.pojoSetterMethods=pojoSetterMethods;
}
public List<Pair<Field,Method>> getPOJOSetterMethods()
{
return this.pojoSetterMethods;
}
public void setResultSetGetterMethods(Map<Class,Method> resultSetGetterMethods)
{
this.resultSetGetterMethods=resultSetGetterMethods;
}
public Map<Class,Method> getResultSetGetterMethods()
{
return this.resultSetGetterMethods;
}
}