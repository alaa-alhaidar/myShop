package shopPackage;

import Connectivity.SQLOrders;
import Connectivity.myConnectSQL;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Cash implements Zahlung {
    double d;
    CustomerAction customerAction = new CustomerAction();
    static DecimalFormat dc = new DecimalFormat("####.####");
    Scanner in = new Scanner(System.in);
    public Cash() {
        System.out.println("Cash payment ist gewählt ");

    }

    public double getBalance() {
        return 0;
    }

    @Override
    public double abrechnen() throws InterruptedException, SQLException, ClassNotFoundException {
        System.out.println("Bitte geben Sie einen Betrag : ");
        d = in.nextDouble();
        setPaymentCash();
        return d;
    }

    public double getD() {
        return d;
    }

    public void setPaymentCash() throws SQLException, ClassNotFoundException, InterruptedException {
        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");
        String setQuery2 =
                "UPDATE `payment` SET `payment`.p_cash = " + (d) + " WHERE `payment`.`p_ref` = " + SQLOrders.getREF();
        Statement statement = connect.createStatement();
        statement.executeUpdate(setQuery2);
        connect.close();
        statement.close();

    }

    public static void setPaymentCashBack(double cashBack) throws SQLException, ClassNotFoundException, InterruptedException {
        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");
        String setQuery2 =
                "UPDATE `payment` SET `payment`.p_cash_back = " + (dc.format(cashBack)) + " WHERE `payment`.`p_ref` = " + SQLOrders.getREF();
        Statement statement = connect.createStatement();
        statement.executeUpdate(setQuery2);
        connect.close();
        statement.close();

    }
}
