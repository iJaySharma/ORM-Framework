# ORM-Framework

## Description 
"Object Relational Mapping-ORM" this is a ORM framework developed in java (having usage of Reflection Api and collection Framework) to automate the backend tasks that is the ease of accessing and manipulating the data from or to database server without writing and managing the data layer logic and lots of sql code a backend developer can consume the facilities of framework in doing the CRUD opearions Automata sql operations by just placing the calls to the functions defined in modules for possesing given activity of user without burden of handling the Pojo entities framework will generate the POJO Classes and manage the structuring of table and fields by analyzing their properties from database and give user the Pojo's with appropriate class level and field level annotations.

## Setup
* Install [Latest JDK](https://www.oracle.com/in/java/technologies/javase-downloads.html)
* install [MYSQL Version 8](https://www.onlinetutorialspoint.com/mysql/install-mysql-on-windows-10-step-by-step.html)
* Download My-Sql-Connector jar and Gson jar file and placed it inside lib folder inside project directory
* include ORM jar file in class path of java command during compilation and execution of java file of working directory
* Create the Conf.json file in current ORM Directory and give Json based information of SQL DataBase along with path where to place generated Pojo Entity classes

 ## Features 
* POJO Generator just Compile and Execute the POJOGenerator class it will generate the Pojo Entities with class and field level annotations in specified path in accordance to the properties of Database tables 
* begin and end method of DataManager class to start and stop the Database Connection of Specified Information of Conf.json file
* save update and delete method of DataManager class for push and pull operation of Data from SQL server which covers the Data Layer logics of specified Pojo Entity
* Sql Select Statement operations using fire method to get desired Records based on particular provided field value to operator function 
* Framework manages the CRUD operations and select query operation by just setting Entities fields according to Standards and calling mentioned method to get Job done.

## Demonstration 
* Save 
```
DataManager dataManager = new DataManager();

Employee employee = new Employee();

employee.setName("Suresh mehta");
employee.setDesignationCode(7);
java.sql.Date date = java.sql.Date.valueOf("2007-05-23");
employee.setDateOfBirth(date);
employee.setGender("M");
employee.setIsIndian(true);
BigDecimal bigDecimal = new BigDecimal(150.00);
employee.setBasicSalary(bigDecimal);
employee.setPanNumber("asdfghj");
employee.setAadharCardNumber("jhgfdsa");
dataManager.begin();
dataManager.save(employee);
dataManager.end();
```
* Update
```
DataManager dataManager = new DataManager();

Employee employee = new Employee();
employee.setId(12);
employee.setName("ramesh Joshi");
employee.setDesignationCode(12);
//java.sql.Date date = java.sql.Date.valueOf("1995-05-23");
//employee.setDateOfBirth(date);
employee.setGender("M");
employee.setIsIndian(true);
BigDecimal bigDecimal = new BigDecimal(250.00);
employee.setBasicSalary(bigDecimal);
employee.setPanNumber("qwert");
employee.setAadharCardNumber("zxcvb");
dataManager.begin();
dataManager.update(employee);
dataManager.end();
```
* Delete
```
DataManager dataManager = new DataManager();

Employee employee = new Employee();
employee.setId(11);
dataManager.begin();
dataManager.delete(employee);
dataManager.end();
```
* Select
```
DataManager dataManager=new DataManager();
dataManager.begin();
List<Object> students=dataManager.select(new Student()).fire();
for(Object object:students)
{
Student student=(Student) object;
System.out.println("Name : "+student.getName());
System.out.println("Type : "+student.getRollnumber());
System.out.println("Age : "+student.getAge());
}
dataManager.end();
```

```
DataManager dataManager=new DataManager();
dataManager.begin();
List<Object> students=dataManager.select(new Student()).where("rollnumber").eq(105).fire();    //eq for equals
dataManager.end();
```
```
DataManager dataManager=new DataManager();
dataManager.begin();
List<Object> students=dataManager.select(new Student()).where("rollnumber").ne(105).fire();    //ne for not equals
dataManager.end();
```
```
DataManager dataManager=new DataManager();
dataManager.begin();
List<Object> students=dataManager.select(new Student()).where("rollnumber").lt(1001).fire();    //lt for lesser than 
dataManager.end();
```
```
DataManager dataManager=new DataManager();
dataManager.begin();
List<Object> students=dataManager.select(new Student()).where("rollnumber").gt(1001).fire();    //gt for greater then
dataManager.end();
```
```
DataManager dataManager=new DataManager();
dataManager.begin();
List<Object> students=dataManager.select(new Student()).where("rollnumber").le(1088).fire();    //le for less than equal
dataManager.end();
```
```
DataManager dataManager=new DataManager();
dataManager.begin();
List<Object> students=dataManager.select(new Student()).where("rollnumber").ge(1088).fire();    //ge for greater than equal 
dataManager.end();
```
