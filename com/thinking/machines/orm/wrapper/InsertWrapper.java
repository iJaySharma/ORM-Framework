package com.thinking.machines.orm.wrapper;
import java.util.*;
import java.lang.*;
import javafx.util.*;	//for pair
import java.lang.reflect.*;

public class InsertWrapper
{
private String sqlStatement;
private Map<Class,Method> preparedStatementSetterMethods;
private List<Pair<Field,Method>> pojoGetterMethods;
private List<ColumnWrapper> columns;
public InsertWrapper()
{
this.sqlStatement=null;
this.preparedStatementSetterMethods=null;
this.pojoGetterMethods=null;
this.columns=null;
}
public void setSQLStatement(String sqlStatement)
{
this.sqlStatement=sqlStatement;
}
public String getSQLStatement()
{
return this.sqlStatement;
}
public void setPreparedStatementSetterMethods(Map<Class,Method> preparedStatementSetterMethods)
{
this.preparedStatementSetterMethods=preparedStatementSetterMethods;
}
public Map<Class,Method> getPreparedStatementSetterMethods()
{
return this.preparedStatementSetterMethods;
}
public void setPOJOGetterMethods(List<Pair<Field,Method>> pojoGetterMethods)
{
this.pojoGetterMethods=pojoGetterMethods;
}
public List<Pair<Field,Method>> getPOJOGetterMethods()
{
return this.pojoGetterMethods;
}
public void setColumns(List<ColumnWrapper> columns)
{
this.columns=columns;
}
public List<ColumnWrapper> getColumns()
{
return this.columns;
}
}