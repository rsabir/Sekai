package sqlite.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.naming.Context;


/**
 * Created by RABOUDI on 18/11/2015.
 */

public class Connexion {
    private String DBName = "Chemin aux base de donnée";
	private Connection connection = null;
	//private Statement statement = null;
	private Context context = null;
	private DataSource datasource = null;


    public Connexion(String dBName) {
        DBName = dBName;
    }

	public boolean connect() {
		try {
			 // Get the context and create a connection
			context = new InitialContext();
			datasource = (DataSource) context.lookup("java:/comp/env/jdbc/"+DBName);
			connection = datasource.getConnection();

			System.out.println("Connexion a la base de donnée de " + DBName
					+ " effectuée avec succés");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erreur de connection");
			return false;
		}
		return true;
		
	}

    public boolean update(String request) {
    	//if (!connect()) return false;
    	
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

    public boolean commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public ResultSet query(String request) {
        ResultSet resultat = null;
       // if (!connect()) return null;
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

    public boolean close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
