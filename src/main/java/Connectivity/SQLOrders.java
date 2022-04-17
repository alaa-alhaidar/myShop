package Connectivity;


import java.sql.*;
import java.text.DecimalFormat;
import java.util.Scanner;

public class SQLOrders {
    static int nextID = 1;
    static private int ref;
    static double price = 0;
    static double total = 0;
    static DecimalFormat dc = new DecimalFormat();
    Scanner in = new Scanner(System.in);
    public void readDataBase(String tableName) throws SQLException, ClassNotFoundException {

        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");
        Statement statement = connect.createStatement();
        String readQuery = "SELECT * FROM " + tableName;

        ResultSet rs = statement.executeQuery(readQuery);
        String stringBuffer = null;
        while (rs.next()) {
            stringBuffer = "P_ID: " + rs.getInt(1) + " P_Name: " + rs.getString
                    (2) + " Price: " + rs.getDouble(3) + " €.";
            System.out.println(stringBuffer);
        }
        System.out.println();
        connect.close();
        statement.close();
    }

    public double readTransaction() throws SQLException, ClassNotFoundException {

        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");
        Statement statement = connect.createStatement();
        String readQuery = "SELECT * FROM transaction_item WHERE transaction_item.tr_ref =  " + ref;

        ResultSet rs = statement.executeQuery(readQuery);
        String stringBuffer = null;
        while (rs.next()) {
            stringBuffer = "P_ID: " + rs.getInt(1) + " P_name: " + rs.getString(2)
                    + " Price: " + rs.getDouble(3);
            total += rs.getDouble(3);
            System.out.println(stringBuffer);
        }
        System.out.println();
        connect.close();
        statement.close();
        return total;
    }

    public static void readFinalStatus() throws SQLException, ClassNotFoundException {

        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");
        Statement statement = connect.createStatement();
        String readQuery = "SELECT * FROM transaction_item WHERE transaction_item.tr_ref =  " + ref;
        ResultSet rs = statement.executeQuery(readQuery);
        String stringBuffer = null;
        while (rs.next()) {
            stringBuffer = "tr_id: " + rs.getInt(1) + " tr_name: " + rs.getString(2)
                    + " Price: " + rs.getDouble(3);

            System.out.println(stringBuffer);
        }

        String readQuery2 = "SELECT * FROM payment WHERE payment.p_ref =  " + ref;
        Statement statement2 = connect.createStatement();
        ResultSet rs2 = statement2.executeQuery(readQuery2);
        String stringBuffer2 = null;
        while (rs2.next()) {
            stringBuffer2 = "Cash : " + rs2.getDouble(3) + "\nPayPal : " + rs2.getDouble(6) +
                    "\nDebit_Credit : " + rs2.getDouble(7) + "\nVouchers  : " +
                    rs2.getInt(5) + "\nTotal  : " + dc.format(rs2.getDouble(2)) +
                    "\nCash back  : " + rs2.getDouble(4);

            System.out.println(stringBuffer2);
        }
        System.out.println();
        connect.close();
        statement.close();
        statement2.close();

    }

    public double insertIntoDataBase(String tableName, int p_id) throws SQLException, ClassNotFoundException {

        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");
        Statement statement = connect.createStatement();
        String readQuery = "SELECT * FROM lebensmittel";
        ResultSet rs = statement.executeQuery(readQuery);

        while (rs.next()) {
            if (rs.getInt(1) == p_id) {
                int id = nextID++;
                String insertQuery = "INSERT INTO transaction_item VALUES  (?,?,?,?)";
                PreparedStatement ps = connect.prepareStatement(insertQuery);
                ps.setInt(1, ref);
                ps.setString(2, rs.getString(2));
                ps.setDouble(3, rs.getDouble(3));
                ps.setInt(4, ref);
                ps.executeUpdate();
                price += rs.getDouble(3);
            }
        }
        connect.close();
        statement.close();
        return price;
    }

    public double insertIntoDataBaseG(int p_id) throws SQLException, ClassNotFoundException {

        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");
        Statement statement = connect.createStatement();
        String readQuery = "SELECT * FROM getraenke";
        ResultSet rs = statement.executeQuery(readQuery);

        while (rs.next()) {
            if (rs.getInt(1) == p_id) {
                int id = nextID++;
                String insertQuery = "INSERT INTO transaction_item VALUES  (?,?,?,?)";
                PreparedStatement ps = connect.prepareStatement(insertQuery);
                ps.setInt(1, id);
                ps.setString(2, rs.getString(2));
                ps.setDouble(3, rs.getDouble(3));
                ps.setInt(4, ref);
                ps.executeUpdate();
                price += rs.getDouble(3);
            }
        }
        connect.close();
        statement.close();
        return price;
    }

    public static double getTotalRechnung() throws SQLException, ClassNotFoundException {
        double t = 0;
        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");
        String query = "SELECT * FROM transaction_item WHERE transaction_item.tr_ref = " + ref;
        Statement statement = connect.createStatement();
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {

            t += rs.getDouble(3);

        }

        return t;
    }

    /**
     * @param customer_id
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int createTransaction(int customer_id) throws SQLException, ClassNotFoundException {
        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");

        String insertQuery = "INSERT INTO `transactions`(`t_total`,`customer_id`) VALUES (" + 0 + "," + customer_id + ")";
        Statement statement = connect.createStatement();
        statement.executeUpdate(insertQuery);

        String readQuery = "SELECT * FROM `transactions` ";
        Statement statement2 = connect.createStatement();
        ResultSet rs = statement2.executeQuery(readQuery);
        while (rs.next()) {
            ref = rs.getInt(1);
        }
        statement.close();
        statement2.close();
        rs.close();
        return ref;
    }


    /**
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void updateRef() throws SQLException, ClassNotFoundException {

        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");

        String insertQuery = "INSERT INTO `payment`(`p_ref`) VALUES (" + ref + ")";
        Statement statement = connect.createStatement();
        statement.executeUpdate(insertQuery);


        statement.close();
        connect.close();


    }

    public void deleteTransaction() throws SQLException, ClassNotFoundException {
        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");

        String insertQuery = "DELETE  FROM `transactions` WHERE `transactions`.`t_id` = " + ref;
        Statement statement = connect.createStatement();
        statement.executeUpdate(insertQuery);

        connect.close();
        statement.close();
    }

    public void deleteAll() throws SQLException, ClassNotFoundException {
        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");

        String deleteQuery = "DELETE  FROM `transaction_item` WHERE `transaction_item`.`tr_ref` = " + ref;
        Statement statement = connect.createStatement();
        statement.executeUpdate(deleteQuery);

        connect.close();
        statement.close();
    }

    public void deleteItem() throws SQLException, ClassNotFoundException {
        System.out.println(" P_ID zu entfernen: ");
        int item_id = in.nextInt();

        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");

        String deletetQuery =
                "DELETE FROM `transaction_item` WHERE `transaction_item`.`tr_id` = " + item_id + " AND "
                        + "`transaction_item`.`tr_ref` = " + ref;
        Statement statement = connect.createStatement();
        statement.executeUpdate(deletetQuery);

        getDeletedValue(item_id);

    }

    public void getDeletedValue(double p_id) throws SQLException, ClassNotFoundException {
        System.out.println(" called ");
        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");
        String readQuery = "SELECT * FROM `transaction_item` ";

        Statement statement = connect.createStatement();
        ResultSet rs = statement.executeQuery(readQuery);
        while (rs.next()) {
            if (rs.getInt(1) == p_id) {
                double d = rs.getDouble(3);
                System.out.println(d);
            }
        }

        connect.close();
        statement.close();

    }

    public void setValue() throws SQLException, ClassNotFoundException {
        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");
        String setQuery = "UPDATE `transactions` SET `t_total` = " + price + " WHERE `transactions`.`t_id` = " + ref;
        Statement statement = connect.createStatement();
        statement.executeUpdate(setQuery);
        String setQuery2 = "UPDATE `payment` SET `p_total_receipt` = " + price + " WHERE `payment`.`p_ref` = " + ref;
        statement.executeUpdate(setQuery2);
        connect.close();
        statement.close();

    }

    public static int getREF() {
        return ref;
    }

}
//64rwe97t