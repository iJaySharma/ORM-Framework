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
* Sql Select Statement operations using fire method to get desired Records based on particular dependent Entity fields
* Framework manages the CRUD operations and select query with where clause by just calling mentioned method and get Job done.
