package org.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.entity.Users;

public class App{
	
	public static void main(String[] args) {
		
		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Users.class)
				.buildSessionFactory();
		
				Session session = factory.getCurrentSession();
		
				try {
					// create object of entity class type
					//1.1) Create Users user = new Users("username1","password1","firstName1","lastName1");
				Users user = new Users();
					//start transaction
					session.beginTransaction();
					//perform operation
					//1.1) session.save(user);
					//1.2) Retrieve user = session.get(Users.class,1);
					user = session.get(Users.class,1);
					//commit the transaction
					//Deleting a Record 
					session.delete(user);
					// updating object
					//1.3)Updating user.setUsername("Sagar");
					session.getTransaction().commit();
					System.out.println(user);
					//1.1) System.out.println("Row added");
					
				} finally {
					session.close();
					factory.close();
				}
				
	}
}