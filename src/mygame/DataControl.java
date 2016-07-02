package mygame;

import java.sql.*;

/**
 *
 * @author Lukas
 */

public class DataControl {
    public boolean DataLoaded;
    public Connection conn;
    public Statement s;

    String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String connectionURL = "jdbc:derby:Datenbank;create=true";

    String createTuerme = "CREATE TABLE TUERME "
        +  " (NAME VARCHAR(32) PRIMARY KEY, "
        +  " DESCRIPTION VARCHAR(32) NOT NULL, "
        +  " RANGE INTEGER NOT NULL, "
        +  " SPEED INTEGER NOT NULL, "
        +  " DMG INTEGER NOT NULL) ";
    String createGegner = "CREATE TABLE GEGNER "
        +  " (NAME VARCHAR(32) PRIMARY KEY, "
        +  " SPEED INTEGER NOT NULL, "
        +  " LEBEN INTEGER NOT NULL) ";
    String answer;

    public DataControl()
    {
        if (DataLoaded)
            return;

        try {
            conn = DriverManager.getConnection(connectionURL);
            System.out.println("Connected.");
            s = conn.createStatement();

            // Check if table exists
            if (!checkForTable(conn, "TUERME"))
            {
                System.out.println (" . . . . creating table TUERME");
                s.execute(createTuerme);
            }

            // Check if table exists
            if (!checkForTable(conn, "GEGNER"))
            {
                System.out.println (" . . . . creating table GEGNER");
                s.execute(createGegner);
            }

            DataLoaded = true;
        } catch (Throwable e)  {
            System.out.println(" . . . exception thrown:");
            e.printStackTrace(System.out);
            DataLoaded = false;
        }
    }

    public void Close()
    {
        if (!DataLoaded)
            return;

        DataLoaded = false;
        try {
            if (s != null)
                s.close();

            if (conn != null)
                conn.close();

            System.out.println("Closed connection");
        } catch (Throwable e)  {
            System.out.println(" . . . exception thrown:");
            e.printStackTrace(System.out);
        }

        // In embedded mode, an application should shut down Derby.
        // Shutdown throws the XJ015 exception to confirm success.
        if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
            boolean gotSQLExc = false;
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException se) {
                if (se.getSQLState().equals("XJ015")) {
                    gotSQLExc = true;
                }
            }

            if (!gotSQLExc) {
                System.out.println("Database did not shut down normally");
            } else {
                System.out.println("Database shut down normally");
            }
        }
    }

    public static boolean checkForTable(Connection conTst, String name) throws SQLException {
        try {
            Statement s = conTst.createStatement();
            s.execute("SELECT 1 FROM " + name);
        }  catch (SQLException sqle) {
            String theError = (sqle).getSQLState();
            System.out.println("Utils got: " + theError);

            // If table exists will get - WARNING 02000: No row was found
            if (theError.equals("42X05")) {
                System.out.println("Table does not exist");
                return false;
            } else if (theError.equals("42X14") || theError.equals("42821")) {
                System.out.println("WwdChk4Table: Incorrect table definition. Drop table WISH_LIST and rerun this program");
                throw sqle;
            } else {
                System.out.println("WwdChk4Table: Unhandled SQLException" );
                throw sqle;
            }
        }

        // Table exists
        return true;
    }

    public ResultSet SelectTurm(String name)
    {
        if (!DataLoaded)
            throw new IllegalArgumentException("Database not loaded!");

        ResultSet turm = null;
        try {
            turm = s.executeQuery("select * from TUERME WHERE NAME='" + name +"'");
        } catch (Throwable e) {
            System.out.println(" . . . exception thrown:");
            e.printStackTrace(System.out);
        }
        return turm;
    }
}
