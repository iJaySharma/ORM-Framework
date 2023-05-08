package com.thinking.machines.orm.wrapper;
public class ForeignKeyWrapper
{
private String parentTableName;
private String parentTableColumnName;
private String childTableName;
private String childTableColumnName;
public ForeignKeyWrapper ()
{
this.parentTableName=null;
this.parentTableColumnName=null;
this.childTableName=null;
this.childTableColumnName=null;
}
public void setParentTableName(String parentTableName)
{
this.parentTableName=parentTableName;
}
public String getParentTableName()
{
return this.parentTableName;
}
public void setParentTableColumnName(String parentTableColumnName)
{
this.parentTableColumnName=parentTableColumnName;
}
public String getParentTableColumnName()
{
return this.parentTableColumnName;
}
public void setChildTableName(String childTableName)
{
this.childTableName=childTableName;
}
public String getChildTableName()
{
return this.childTableName;
}
public void setChildTableColumnName(String childTableColumnName)
{
this.childTableColumnName=childTableColumnName;
}
public String getChildTableColumnName()
{
return this.childTableColumnName;
}
}
