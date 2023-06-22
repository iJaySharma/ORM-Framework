package com.thinking.machines.orm.wrapper;
import java.util.*;
import java.lang.*;
import java.lang.reflect.*;
import javafx.util.*;
public class DeleteWrapper
{
private String tableName;
private String deleteSQLStatement;
private Map<Class,Method> preparedStatementSetterMethods;
private List<Pair<Field,Method>> pojoGetterMethods;
private List<ForeignKeyWrapper> foreignKeys;
public DeleteWrapper()
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
public void setDeleteSQLStatement(String deleteSQLStatement)
{
this.deleteSQLStatement=deleteSQLStatement;
}
public String getDeleteSQLStatement()
{
return this.deleteSQLStatement;
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
public void setForeignKeys(List<ForeignKeyWrapper> foreignKeys)
{
this.foreignKeys=foreignKeys;
}
public List<ForeignKeyWrapper> getForeignKeys()
{
return this.foreignKeys;
}
}