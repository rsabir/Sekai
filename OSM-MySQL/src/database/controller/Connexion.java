package database.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Connexion {
	 private static volatile Connexion instance = null;
	 private static Connection connection = null;
		//private Statement statement = null;
		private static Context context = null;
		private static DataSource datasource = null;

	 private Connexion(){
		 super();
		 try {
			 // Get the context and create a connection
			context = new InitialContext();
			datasource = (DataSource) context.lookup("java:/comp/env/jdbc/server");
			connection = datasource.getConnection();
			System.out.println("Connexion a la base de donnée de "
					+ " effectuée avec succés");
		} catch (Exception e) {
			System.out.println("Erreur de connection");
		}
	 }

public final static Connexion getInstance() {
	 if (Connexion.instance == null) {
		 synchronized(Connexion.class) {
             if (Connexion.instance == null) {
               Connexion.instance = new Connexion();
             }
           }
	 }
	return Connexion.instance;
	
}

public ResultSet query(String request) {
	 ResultSet resultat = null;
      try {
      	Statement statement = connection.createStatement();
          resultat = statement.executeQuery(request);
      } catch (SQLException e) {
          e.printStackTrace();
          System.out.println("Erreur dans la requete : " + request);
          return null;
      }
     // close();
      return resultat;
}

public boolean update(String request) {

	 try {
    	Statement statement = connection.createStatement();
        statement.executeUpdate(request);
    } catch (SQLException e) {
       // e.printStackTrace();
        System.out.println("Erreur dans la requete : " + request);
        return false;
    }
    
    //close();
    return true;
}
}

