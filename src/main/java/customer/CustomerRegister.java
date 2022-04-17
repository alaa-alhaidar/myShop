package customer;

import Connectivity.myConnectSQL;

import shopPackage.RegistrationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CustomerRegister {
    static boolean isRegistered = false;
    static boolean loginSuccess = false;
    static myConnectSQL myConnectSQL = new myConnectSQL();
    static Scanner in = new Scanner(System.in);

    public static void register() throws SQLException, ClassNotFoundException {
        System.out.println("name: ");
        String name = in.nextLine();
        System.out.println("vorname: ");
        String vorname = in.nextLine();
        if (vorname.length() < 6) {
            System.out.println("vorname: ");
            vorname = in.nextLine();
        }
        System.out.println("email: ");
        String email = in.nextLine();

        if (email.length() < 13 || !(email.contains("@"))) {
            System.out.println("email: ");
            email = in.nextLine();
        }
        System.out.println("psw: ");
        String password = in.nextLine();

        if (password.length() < 6) {
            System.out.println("Password: ");
            password = in.nextLine();

        }
        System.out.println("kontakt: ");
        String kontakt = in.nextLine();

        Connection connect = myConnectSQL.getConnect("stock");

        String insertQuery =
                "INSERT INTO `customer_reg`(`c_name`,`c_vorname`,`c_email`,`c_psw`,`c_kontakt`) VALUES " +
                        "('" + name + "', '" + vorname + "','" + email + "','" + password + "','" + kontakt + "')";
        Statement statement = connect.createStatement();
        statement.executeUpdate(insertQuery);
        statement.close();
        connect.close();
        isRegistered = true;
        System.out.println(" Danke für deine Anmeldung bei Smart Shop\n");

    }

    public boolean isIsRegistered() {
        return isRegistered;
    }

    public static int login() throws SQLException, ClassNotFoundException, RegistrationException {
        int d = 0;
        System.out.println("Bitte melden Sie sich wenn Sie bereit regestriert sind" +
                "\nemail: ");
        String email = in.nextLine();
        System.out.println("psw: ");
        String password = in.nextLine();

        myConnectSQL myConnectSQL = new myConnectSQL();
        Connection connect = myConnectSQL.getConnect("stock");

        String readQuery = "SELECT * FROM `customer_reg` ";

        Statement statement = connect.createStatement();
        ResultSet rs = statement.executeQuery(readQuery);
        while (rs.next()) {
            if (rs.getString(4).equals(email) &
                    rs.getString(5).equals(password)) {

                d = rs.getInt(1);
                isRegistered = true;
                loginSuccess = true;

            } else {
                while (loginSuccess != true) {
                    login();
                }
            }
        }

        connect.close();
        statement.close();
        System.out.println("login ist erfolgreich\n");
        return d;
    }

}
