package com.thinking.machines.orm.utilities;
import com.thinking.machines.orm.wrapper.*;
import com.thinking.machines.orm.exception.*;
import java.util.*;
public class EntityUtility
{
private List<TableWrapper> tableWrappers;
public EntityUtility()
{
this.tableWrappers=null;
}
public void setTableWrappers(List<TableWrapper> tableWrappers)
{
this.tableWrappers=tableWrappers;
}
public List<TableWrapper> getTableWrappers()
{
return this.tableWrappers;
}
}