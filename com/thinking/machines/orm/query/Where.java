package com.thinking.machines.orm.query;
import java.util.*;
public class Where<T> 
{
private Clause clause;
private QueryImplementor<T> queryImplementor;
public Where(QueryImplementor<T> queryImplementor,Clause clause)
{
this.queryImplementor=queryImplementor;
this.clause=clause;
}
public Expression<T> eq(Object rightOperand)
{
clause.setOperator(Operators.eq);
clause.setRightOperand(rightOperand);
Expression<T> expression=new Expression<T>(queryImplementor,clause);
return expression;
}
public Expression<T> ne(Object rightOperand)
{
clause.setOperator(Operators.ne);
clause.setRightOperand(rightOperand);
Expression<T> expression=new Expression<T>(queryImplementor,clause);
return expression;
}
public Expression<T> gt(Object rightOperand)
{
clause.setOperator(Operators.gt);
clause.setRightOperand(rightOperand);
Expression<T> expression=new Expression<T>(queryImplementor,clause);
return expression;
}
public Expression<T> lt(Object rightOperand)
{
clause.setOperator(Operators.lt);
clause.setRightOperand(rightOperand);
Expression<T> expression=new Expression<T>(queryImplementor,clause);
return expression;
}
public Expression<T> ge(Object rightOperand)
{
clause.setOperator(Operators.ge);
clause.setRightOperand(rightOperand);
Expression<T> expression=new Expression<T>(queryImplementor,clause);
return expression;
}
public Expression<T> le(Object rightOperand)
{
clause.setOperator(Operators.le);
clause.setRightOperand(rightOperand);
Expression<T> expression=new Expression<T>(queryImplementor,clause);
return expression;
}
}