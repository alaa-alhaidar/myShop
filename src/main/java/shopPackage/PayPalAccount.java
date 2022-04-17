package shopPackage;

public class PayPalAccount {

    private double accountBalance;
    private String accountHolder;
    private String passWort;

    public PayPalAccount(double accountBalance, String accountHolder, String passWort) {
        this.accountBalance = accountBalance;
        this.accountHolder = accountHolder;
        this.passWort = passWort;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double d) {

        this.accountBalance -= d;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public String getPassWort() {
        return passWort;
    }

    @Override
    public String toString() {
        return "PayPalAccount{" +
                "accountBalance=" + accountBalance +
                ", accountHolder='" + accountHolder + '\'' +
                ", passWort='" + passWort + '\'' +
                '}';
    }
}
