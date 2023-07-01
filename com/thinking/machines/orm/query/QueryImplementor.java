package com.thinking.machines.orm.query;
import com.thinking.machines.orm.exception.*;
import java.util.*;
public interface QueryImplementor<T>
{
public List<T> fire() throws ORMException;
public void addExpression(Expression<T> expression);
}