package com.thinking.machines.orm.validator;
import com.thinking.machines.orm.*;
import com.thinking.machines.orm.exception.*;
import com.thinking.machines.orm.wrapper.*;
import com.thinking.machines.orm.annotation.*;
import com.thinking.machines.orm.utilities.*;
import javafx.util.*;
import java.util.*;
import java.lang.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.sql.*;
public class Validator
{
private Object object;
private List<Pair<Field,Method>> pojoGetterMethods;
private Connection connection;
private TableValidator tableValidator;
private PrimaryKeyValidator primaryKeyValidator;
private ForeignKeyValidator foreignKeyValidator;
private AutoIncrementValidator autoIncrementValidator;
private ColumnValidator columnValidator;
private DatabaseMetaData databaseMetaData;
private ResultSetMetaData resultSetMetaData;
private ResultSet resultSet;
private boolean exceptionFlag;
private List<String> exceptions;
public Validator(Object object,List<Pair<Field,Method>> pojoGetterMethods)
{
this.object=object;
this.pojoGetterMethods=pojoGetterMethods;
this.exceptions=new LinkedList<>();
try
{
this.connection=ConnectionUtility.getConnection();
}catch(Exception exception)
{
exception.printStackTrace();
}
}
public void validate(ValidatorType validatorType)
{
try
{
if(validatorType.equals(ValidatorType.PRIMARY))
{
//primary key validator
primaryKeyValidator=new PrimaryKeyValidator(object,pojoGetterMethods,connection);
primaryKeyValidator.validate();
if(primaryKeyValidator.hasException())
{
exceptionFlag=true;
exceptions.add(primaryKeyValidator.getExceptions());
return;
}
System.out.println("**********Primary Key Validation Complete**********");
}
else if(validatorType.equals(ValidatorType.FOREIGN))
{
//foreign key validator
foreignKeyValidator=new ForeignKeyValidator(object,pojoGetterMethods,connection);
foreignKeyValidator.validate();
if(foreignKeyValidator.hasException())
{
exceptionFlag=true;
exceptions.add(foreignKeyValidator.getExceptions());
return;
}
System.out.println("**********Foreign Key validation complete.**********");
}
else if(validatorType.equals(ValidatorType.AUTOINCREMENT))
{
//auto increment validator
autoIncrementValidator=new AutoIncrementValidator(object,pojoGetterMethods,connection);
autoIncrementValidator.validate();
if(autoIncrementValidator.hasException())
{
exceptionFlag=true;
exceptions.add(autoIncrementValidator.getExceptions());
return;
}
System.out.println("**********Auto Increment validation complete.**********");
}
else if(validatorType.equals(ValidatorType.COLUMN))
{
columnValidator=new ColumnValidator(object,pojoGetterMethods,connection);
columnValidator.validate();
if(columnValidator.hasException())
{
exceptionFlag=true;
exceptions.add(columnValidator.getExceptions());
return;
}
System.out.println("**********Column validation complete.**********");
}
else if(validatorType.equals(ValidatorType.TABLE))
{
tableValidator=new TableValidator(object,connection);
tableValidator.validate();
if(tableValidator.hasException())
{
exceptionFlag=true;
exceptions.add(tableValidator.getExceptions());
return;
}
System.out.println("**********Table validation complete.**********");
}
}catch(ORMException ormException)
{
ormException.printStackTrace();
}
}
public boolean hasException()
{
return this.exceptionFlag;
}
public String getExceptions()
{
StringBuffer exceptionStream=null;
if(exceptionFlag)
{
exceptionStream=new StringBuffer();
for(String exception:exceptions) exceptionStream.append(exception);
}
return exceptionStream.toString();
}
}