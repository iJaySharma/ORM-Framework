package com.thinking.machines.orm.query;
public class Clause
{
private String leftOperand;
private Object rightOperand;
private Integer operator;
public Clause()
{
this.leftOperand=null;
this.rightOperand=null;
this.operator=null;
}
public void setLeftOperand(String leftOperand)
{
this.leftOperand=leftOperand;
}
public String getLeftOperand()
{
return this.leftOperand;
}
public void setRightOperand(Object rightOperand)
{
this.rightOperand=rightOperand;
}
public Object getRightOperand()
{
return this.rightOperand;
}
public void setOperator(Integer operator)
{
this.operator=operator;
}
public Integer getOperator()
{
return this.operator;
}
}