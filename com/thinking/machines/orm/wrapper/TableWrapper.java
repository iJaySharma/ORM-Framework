package com.thinking.machines.orm.wrapper;
import java.util.*;
public class TableWrapper
{
private String name;
private List<ColumnWrapper> columnWrappers;
public TableWrapper()
{
this.name=null;
this.columnWrappers=null;
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
public void setColumnWrappers(List<ColumnWrapper> columnWrappers)
{
this.columnWrappers=columnWrappers;
}
public List<ColumnWrapper> getColumnWrappers()
{
return this.columnWrappers;
}
}