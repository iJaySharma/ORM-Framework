package com.thinking.machines.orm.query;
import java.util.*;
public class Operators
{
private Operators()
{
}
private static Map<Integer,String> operators=new HashMap<>();
static
{
operators.put(1,">");
operators.put(2,"<");
operators.put(3,">=");
operators.put(4,"<=");
operators.put(5,"=");
operators.put(6,"<>");
}
public static final Integer gt=1;	// >
public static final Integer lt=2;	// <
public static final Integer ge=3;	// >=
public static final Integer le=4;	// <=
public static final Integer eq=5;	// =
public static final Integer ne=6;    // <>
public static String getOperator(Integer operator)
{
return operators.get(operator);
}
}