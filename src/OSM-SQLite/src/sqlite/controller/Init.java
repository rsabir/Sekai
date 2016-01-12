package sqlite.controller;

import java.sql.ResultSet;

/**
 * Created by RABOUDI on 22/11/2015.
 */
public class Init {
    private static Connexion DBcnx = null;

    public static void main(String[] args) {
        assert args.length >= 1 : "Veuillez indiquer le nom du serveur";
        DBcnx = new Connexion(args[0]);
        DBcnx.connect();
        String createStatement =
                "DROP TABLE Marquer;" +
                        "DROP TABLE Node;" +
                        "DROP TABLE User;" +
                        "CREATE TABLE User\n" +
                        "(ID            INTEGER   PRIMARY KEY AUTOINCREMENT NOT NULL ,\n" +
                        " NAME          TEXT,\n" +
                        " MACADDR       TEXT  UNIQUE NOT NULL);\n" +
                        "CREATE TABLE Node\n" +
                        "(ID            INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL ,\n" +
                        " LONGITUDE     LONG  NOT NULL,\n" +
                        " LATITUDE      LONG  NOT NULL);\n" +
                        "CREATE TABLE Marquer\n" +
                        "(USERID        INT  NOT NULL REFERENCES User(ID),\n" +
                        " NODEID         INT  NOT NULL REFERENCES Node(ID),\n" +
                        " DATEOFADD     DATE NOT NULL,\n" +
                        "  PRIMARY KEY(USERID, NODEID, DATEOFADD)\n" +
                        "\n" +
                        ");";
        DBcnx.update(createStatement);
      //  DBcnx.commit();
        String insertStatement =
                "INSERT INTO User(NAME,MACADDR)\n" +
                        "VALUES (\"Jacque\",\"AE:0F:00:34:DE:BC\");\n" +
                        "INSERT INTO User(NAME,MACADDR)\n" +
                        "VALUES (\"Jeanne\",\"BD:E5:F7:43:21:11\");\n" +
                        "INSERT INTO User(NAME,MACADDR)\n" +
                        "VALUES (\"Julien\",\"26:4E:96:55:DD:AB\");\n" +
                        "INSERT INTO User(NAME,MACADDR)\n" +
                        "VALUES (\"Paul\",\"FE:32:45:67:88:90\");\n" +
                        "INSERT INTO User(NAME,MACADDR)\n" +
                        "VALUES (\"Rosalie\",\"ED:33:22:56:F8:00\");\n"+
                        "INSERT INTO Node(LONGITUDE,LATITUDE)\n" +
                        "VALUES (-0.5777,44.838);\n" +
                        "INSERT INTO Node(LONGITUDE,LATITUDE)\n" +
                        "VALUES (-0.5791799999999512,44.837789);\n" +
                        "INSERT INTO Node(LONGITUDE,LATITUDE)\n" +
                        "VALUES (-0.5771620000000439,44.837317);\n" +
                        "INSERT INTO Node(LONGITUDE,LATITUDE)\n" +
                        "VALUES (-0.5704630000000179,44.849101);\n" +
                        "INSERT INTO Node(LONGITUDE,LATITUDE)\n" +
                        "VALUES (-0.5691448000000037,44.8543239);\n" +
                        "INSERT INTO Node(LONGITUDE,LATITUDE)\n" +
                        "VALUES (-0.5730469999999741,44.831173);\n" +
                        "INSERT INTO Node(LONGITUDE,LATITUDE)\n" +
                        "VALUES (-0.5597199999999702,44.840316);\n" +
                        "INSERT INTO Node(LONGITUDE,LATITUDE)\n" +
                        "VALUES (-0.5909619999999904,44.811956);\n"+
                        "INSERT INTO Marquer(USERID,NODEID,DATEOFADD)\n" +
                        "VALUES (1,1,\"2015-11-23 10:54:10.000\");\n"+
                        "INSERT INTO Marquer(USERID,NODEID,DATEOFADD)\n" +
                        "VALUES (2,2,\"2015-11-23 10:54:00.000\");\n"+
                        "INSERT INTO Marquer(USERID,NODEID,DATEOFADD)\n" +
                        "VALUES (3,1,\"2015-11-23 10:54:10.000\");\n"+
                        "INSERT INTO Marquer(USERID,NODEID,DATEOFADD)\n" +
                        "VALUES (4,4,\"2015-11-23 10:54:10.000\");\n"+
                        "INSERT INTO Marquer(USERID,NODEID,DATEOFADD)\n" +
                        "VALUES (4,1,\"2015-11-23 10:54:20.000\");\n"
                      ;
        DBcnx.update(insertStatement);
    }
}
