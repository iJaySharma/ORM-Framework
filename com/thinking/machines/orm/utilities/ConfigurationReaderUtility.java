package com.thinking.machines.orm.utilities;
import com.thinking.machines.orm.exception.*;
import com.thinking.machines.orm.wrapper.*;
import com.google.gson.*;
import java.io.*;
public class ConfigurationReaderUtility
{
private ConfigurationReaderUtility()
{
}
public static ConfigurationWrapper getConfigurations() throws ORMException
{
Gson gson=null;
File file=null;
ConfigurationWrapper configurationWrapper=null;
String pathToConfiguration="";
try
{
pathToConfiguration="c:\\ormFramework\\conf.json";
gson=new Gson();
file=new File(pathToConfiguration);
if(file.exists()==false) throw new ORMException("Conf.json missing or is been replaced");
gson=new Gson();
configurationWrapper=gson.fromJson(new FileReader(file.getAbsolutePath()),ConfigurationWrapper.class);
}catch(Exception exception)
{
}
return configurationWrapper;
}
}