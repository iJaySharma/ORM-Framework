package com.thinking.machines.orm.wrapper;
public class ColumnWrapper
{
private String name;
private Long size;
private String dataType;
private Boolean isAutoIncrement;
private Boolean isPrimaryKey;
private Boolean isForeignKey;
private Boolean isNull;
private ForeignKeyWrapper foreignKeyWrapper;
public ColumnWrapper()
{
this.name=null;
this.size=null;
this.dataType=null;
this.isAutoIncrement=null;
this.isNull=null;
this.isPrimaryKey=null;
this.isForeignKey=null;
this.foreignKeyWrapper=null;
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
public void setSize(Long size)
{
this.size=size;
}
public Long getSize()
{
return this.size;
}
public void setDataType(String dataType)
{
this.dataType=dataType;
}
public String getDataType()
{
return this.dataType;
}
public void setIsAutoIncrement(Boolean isAutoIncrement)
{
this.isAutoIncrement=isAutoIncrement;
}
public Boolean getIsAutoIncrement()
{
return this.isAutoIncrement;
}
public void setIsPrimaryKey(Boolean isPrimaryKey)
{
this.isPrimaryKey=isPrimaryKey;
}
public Boolean getIsPrimaryKey()
{
return this.isPrimaryKey;
}
public void setIsForeignKey(Boolean isForeignKey)
{
this.isForeignKey=isForeignKey;
}
public Boolean getIsForeignKey()
{
return this.isForeignKey;
}
public void setIsNull(Boolean isNull)
{
this.isNull=isNull;
}
public Boolean getIsNull()
{
return this.isNull;
}
public void setForeignKeyWrapper(ForeignKeyWrapper foreignKeyWrapper)
{
this.foreignKeyWrapper=foreignKeyWrapper;
}
public ForeignKeyWrapper getForeignKeyWrapper()
{
return this.foreignKeyWrapper;
}
}