package shopPackage;


import java.util.Scanner;

public class CreditDebit implements Zahlung {
    Scanner in = new Scanner(System.in);
    double d;

    public CreditDebit() {
        System.out.println("Card payment ist gewählt ");

    }

    CustomerAction customerAction = new CustomerAction();
    static double r;

    @Override
    public double getBalance() {
        return r;

    }

    @Override
    public double abrechnen() {
        System.out.println("Bitte geben Sie einen Betrag : ");
        d = in.nextDouble();

        return d;
    }

    public double getD() {
        return d;
    }
}
