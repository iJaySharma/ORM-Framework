package com.thinking.machines.orm.query;
import com.thinking.machines.orm.exception.*;
import java.util.*;
public class Expression<T>
{
private QueryImplementor<T> queryImplementor;
private Clause clause;
public Expression(QueryImplementor<T> queryImplementor,Clause clause)
{
this.queryImplementor=queryImplementor;
this.clause=clause;
}
public List<T> fire() throws ORMException
{
queryImplementor.addExpression(this);
return queryImplementor.fire();
}
public Clause getClause()
{
return this.clause;
}
}