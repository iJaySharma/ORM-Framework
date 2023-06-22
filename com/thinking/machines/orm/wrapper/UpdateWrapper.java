package com.thinking.machines.orm.wrapper;
import java.util.*;
import javafx.util.*;
import java.lang.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
public class UpdateWrapper
{
private String tableName;
private String updateSQLStatement;
private Map<Class,Method> preparedStatementSetterMethods;
private List<Pair<Field,Method>> pojoGetterMethods;
private List<ColumnWrapper> columns;
private List<ColumnWrapper> primaryKeyColumns;
private Integer primaryKeyColumnsCount;
public UpdateWrapper()
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
public void setUpdateSQLStatement(String updateSQLStatement)
{
this.updateSQLStatement=updateSQLStatement;
}
public String getUpdateSQLStatement()
{
return this.updateSQLStatement;
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
public void setPrimaryKeyColumnsCount(Integer primaryKeyColumnsCount)
{
this.primaryKeyColumnsCount=primaryKeyColumnsCount;
}
public Integer getPrimaryKeyColumnsCount()
{
return this.primaryKeyColumnsCount;
}
public void setPrimaryKeyColumns(List<ColumnWrapper> primaryKeyColumns)
{
this.primaryKeyColumns=primaryKeyColumns;
}
public List<ColumnWrapper> getPrimaryKeyColumns()
{
return this.primaryKeyColumns;
}
}