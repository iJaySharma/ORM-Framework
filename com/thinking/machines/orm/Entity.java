package com.thinking.machines.orm;
import com.thinking.machines.orm.wrapper.*;
import com.thinking.machines.orm.exception.*;
public class Entity
{
private Object entityObject;
private InsertWrapper insertWrapper;
private UpdateWrapper updateWrapper;
private DeleteWrapper deleteWrapper;
private SelectWrapper selectWrapper;
public Entity()
{
}
public void setEntityObject(Object entityObject)
{
this.entityObject=entityObject;
}
public Object getEntityObject()
{
return this.entityObject;
}
public void setInsertWrapper(InsertWrapper insertWrapper)
{
this.insertWrapper=insertWrapper;
}
public InsertWrapper getInsertWrapper()
{
return this.insertWrapper;
}
public void setUpdateWrapper(UpdateWrapper updateWrapper)
{
this.updateWrapper=updateWrapper;
}
public UpdateWrapper getUpdateWrapper()
{
return this.updateWrapper;
}
public void setDeleteWrapper(DeleteWrapper deleteWrapper)
{
this.deleteWrapper=deleteWrapper;
}
public DeleteWrapper getDeleteWrapper()
{
return this.deleteWrapper;
}
public void setSelectWrapper(SelectWrapper selectWrapper)
{
this.selectWrapper=selectWrapper;
}
public SelectWrapper getSelectWrapper()
{
return this.selectWrapper;
}
}