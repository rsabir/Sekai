package sqlite.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by RABOUDI on 18/11/2015.
 */

public class Connexion {
    private String DBName = "Chemin aux base de donnée SQLite";
    private Connection connection = null;
    private Statement statement = null;

    public Connexion(String dBName) {
        DBName = dBName;
    }

	public void connect() {
		try {
			  Class.forName("org.sqlite.JDBC");
		      connection = DriverManager.getConnection("jdbc:sqlite:" + DBName
		  + ".db");
			// Context ctx = new InitialContext();
	          //DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/server");
	          //Connection connection = ds.getConnection();
			statement = connection.createStatement();
			System.out.println("Connexion a la base de donnée de " + DBName
					+ " effectuée avec succès");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erreur de connection");
		}
	}

    public void update(String request) {
        try {
            statement.executeUpdate(request);
        } catch (SQLException e) {
           // e.printStackTrace();
            System.out.println("Erreur dans la requete : " + request);
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ResultSet query(String request) {
        ResultSet resultat = null;
        try {
            resultat = statement.executeQuery(request);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur dans la requet : " + request);
        }
        return resultat;

    }

    public void close() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
