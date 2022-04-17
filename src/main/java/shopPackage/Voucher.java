package shopPackage;

import Connectivity.SQLOrders;
import Connectivity.myConnectSQL;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Voucher implements Zahlung {
    double d;
    int vArt = 0;
    Scanner in = new Scanner(System.in);
    public Voucher() {
        System.out.println("Voucher payment ist gewählt ");

    }


    @Override
    public double getBalance() {
        return 0;
    }

    @Override
    public double abrechnen() throws InterruptedException, SQLException, ClassNotFoundException {
        int voucherValue = voucherArt();
        setPaymentVoucher();
        return voucherValue;
    }

    public int voucherArt() {

        String[] voucherArr = new String[3];

        voucherArr[0] = " Wähle 0 für Vouchers, 25 €.";
        voucherArr[1] = " Wähle 1 fürVouchers, 50 €.";
        voucherArr[2] = " Wähle 2 fürVouchers, 100 €.";

        for (String s : voucherArr) {
            System.out.println(s);
        }
        System.out.println(" Bitte geben Sie Ihr voucher kategory \n");
        vArt = in.nextInt();

        switch (vArt) {
            case 0: {
                vArt = 25;
                break;
            }
            case 1: {
                vArt = 50;
                break;
            }
            case 2: {
                vArt = 100;
                break;
            }
        }
        return vArt;
    }

    public double getD() {
        return d;
    }

    public void setPaymentVoucher() throws SQLException, ClassNotFoundException, InterruptedException {
        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");
        String setQuery2 =
                "UPDATE `payment` SET `payment`.p_voucher = " + (vArt) + " WHERE `payment`.`p_ref` = " + SQLOrders.getREF();
        Statement statement = connect.createStatement();
        statement.executeUpdate(setQuery2);
        connect.close();
        statement.close();

    }

    public double getTotal() throws SQLException, ClassNotFoundException {
        return SQLOrders.getTotalRechnung();
    }

}
