package analyse;


import Connectivity.myConnectSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SalesAnalyse {

    Connectivity.myConnectSQL myConnectSQL = new myConnectSQL();

    public int getTotalSales() throws SQLException, ClassNotFoundException {
        int total_sales = 0;
        Connection connect = myConnectSQL.getConnect("stock");
        String readQuery = "SELECT * FROM `transactions` ";
        Statement statement = connect.createStatement();
        ResultSet rs = statement.executeQuery(readQuery);
        while (rs.next()) {
            total_sales += rs.getDouble(2);
        }
        statement.close();
        rs.close();

        return total_sales;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        SalesAnalyse sa = new SalesAnalyse();
        System.out.println("Total sales: "+sa.getTotalSales());
    }
}
