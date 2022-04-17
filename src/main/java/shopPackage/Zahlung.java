package shopPackage;

import java.sql.SQLException;

public interface Zahlung {
    double d = 0;

    public double getBalance() throws InterruptedException, SQLException, ClassNotFoundException;

    public double abrechnen() throws SQLException, ClassNotFoundException, InterruptedException;

}
