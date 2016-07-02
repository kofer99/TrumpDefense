package mygame;

import java.sql.*;

/**
 *
 * @author Lukas
 */
public class DataHelper {
    String printLine = "  __________________________________________________";
    DataControl dataControl;
    Connection conn;
    Statement s;

    public DataHelper(DataControl control)
    {
        dataControl = control;
        conn = control.conn;
        s = control.s;
    }

    public void insertTurm(String name, String desc, int range, int speed, int dmg)
    {
        if (!dataControl.DataLoaded)
            return;

        try {
            // Prepare the insert statement to use
            System.out.println(printLine);
            System.out.println("Inserting....");
            Statement stmt = conn.createStatement();
            stmt.execute("insert into TUERME values('" +
                name + "','" + desc + "'," + range + "," + speed + "," + dmg + ")");
            stmt.close();
            System.out.println(printLine);
        } catch (Throwable e)  {
            System.out.println(" . . . exception thrown:");
            e.printStackTrace(System.out);
        }
    }

    public void insertGegner(String name, int speed, int leben)
    {
        if (!dataControl.DataLoaded)
            return;

        try {
            // Prepare the insert statement to use
            System.out.println(printLine);
            System.out.println("Inserting....");
            Statement stmt = conn.createStatement();
            stmt.execute("insert into GEGNER values('" +
                name + "'," + speed + "," + leben + ")");
            stmt.close();
            System.out.println(printLine);
        } catch (Throwable e)  {
            System.out.println(" . . . exception thrown:");
            e.printStackTrace(System.out);
        }
    }

    public void Update(String s)
    {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(s);
            stmt.close();
        } catch (Throwable e) {
            System.out.println(" . . . exception thrown:");
            e.printStackTrace(System.out);
        }
    }

    public void SelectTuerme()
    {
        if (!dataControl.DataLoaded)
            return;

        try {
            ResultSet myWishes = s.executeQuery("select * from TUERME");
            ResultSetMetaData rsmd = myWishes.getMetaData();

            System.out.println(printLine);
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");

            System.out.println("\n----------------------------------------------------");

            while(myWishes.next())
            {
                System.out.println(myWishes.getString(1) + "\t\t" +  myWishes.getString(2)
                    + "\t\t" + myWishes.getInt(3) + "\t\t" + myWishes.getInt(4) + "\t\t" + myWishes.getInt(5));
            }

            System.out.println(printLine);
            myWishes.close();
        } catch (Throwable e) {
            System.out.println(" . . . exception thrown:");
            e.printStackTrace(System.out);
        }
    }

    public void SelectGegner()
    {
        if (!dataControl.DataLoaded)
            return;

        try {
            ResultSet myWishes = s.executeQuery("select * from GEGNER");
            ResultSetMetaData rsmd = myWishes.getMetaData();

            System.out.println(printLine);
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");

            System.out.println("\n----------------------------------------------------");

            while(myWishes.next())
                System.out.println(myWishes.getString(1) + "\t\t" + myWishes.getInt(2) + "\t\t" + myWishes.getInt(3));

            System.out.println(printLine);
            myWishes.close();
        } catch (Throwable e) {
            System.out.println(" . . . exception thrown:");
            e.printStackTrace(System.out);
        }
    }
}
