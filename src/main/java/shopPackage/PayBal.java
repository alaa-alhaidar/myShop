package shopPackage;

import Connectivity.SQLOrders;
import Connectivity.myConnectSQL;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class PayBal implements Zahlung {
    DecimalFormat dc = new DecimalFormat("####.####");
    myConnectSQL myConnectSQL = new myConnectSQL();
    private static Map<Integer, PayPalAccount> payPalList = new TreeMap<>();
    int id = nextID++;
    static int nextID = 0;
    static String psw = "";
    static double accountBalance;
    double balance;
    CustomerAction customerAction = new CustomerAction();
    Scanner in = new Scanner(System.in);

    public PayBal() {
        System.out.println("PayPal payment ist gewählt ");
    }

    @Override
    public double getBalance() throws InterruptedException, SQLException, ClassNotFoundException {
        balance = SQLOrders.getTotalRechnung();
        System.out.println(SQLOrders.getTotalRechnung());
        return balance;
    }

    @Override
    public double abrechnen() throws SQLException, ClassNotFoundException, InterruptedException {
        System.out.println("paypal abrechnen called ");
        double d = checkPasswort();
        System.out.println("PayPal konto Balance " + dc.format(d));
        return d;
    }

    private double checkPasswort() throws SQLException, ClassNotFoundException, InterruptedException {
        double d = readPayPalDatenBank();
        setValue();
        setPaymentPayPal();
        return d;
    }

    private double readPayPalDatenBank() throws SQLException, ClassNotFoundException, InterruptedException {

        Connection connection = myConnectSQL.getConnect("stock");
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM  paypal";
        ResultSet rs = statement.executeQuery(query);
        System.out.println("Bitte Geben Sie Ihre PayPal GeheimZahl ein: ");
        psw = in.nextLine();

        while (rs.next()) {
            if (rs.getString(3).equals(psw) &
                    rs.getDouble(4) >= customerAction.getSumme()) {
                Thread.sleep(2000);
                System.out.println(" check is successful " + rs.getString(2) + "  " + rs.getDouble(4));
                accountBalance = rs.getDouble(4);

            }
        }
        connection.close();
        statement.close();

        return accountBalance;
    }

    public void setValue() throws SQLException, ClassNotFoundException, InterruptedException {
        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");
        String setQuery =
                "UPDATE `paypal` SET `paypal`.`pp_balance` = " + dc.format(accountBalance - SQLOrders.getTotalRechnung()) + " WHERE `paypal`.`pp_psw` = '" + psw + "'";

        Statement statement = connect.createStatement();
        statement.executeUpdate(setQuery);
        connect.close();
        statement.close();

    }


    public void setPaymentPayPal() throws SQLException, ClassNotFoundException, InterruptedException {
        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");


        String setQuery2 =
                "UPDATE `payment` SET `payment`.p_paypal = " + (SQLOrders.getTotalRechnung()) + " WHERE `payment`.`p_ref` = " + SQLOrders.getREF();
        Statement statement = connect.createStatement();
        statement.executeUpdate(setQuery2);
        connect.close();
        statement.close();

    }


}
