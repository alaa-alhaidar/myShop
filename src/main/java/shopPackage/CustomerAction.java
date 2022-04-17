package shopPackage;

import Connectivity.SQLOrders;

import customer.CustomerRegister;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

public class CustomerAction {
    Scanner in = new Scanner(System.in);

    int stkToRemove, zArt, produktID, aktion;

    static double steuer = 0;


    SQLOrders sqlOrders = new SQLOrders();
    Map<String, PayPalAccount> pp;
    static boolean bestellt = false;
    Date date = new Date();
    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DecimalFormat dc = new DecimalFormat("###.##");
    int customer_id;

    public void aktion() throws SQLException, ClassNotFoundException, InterruptedException, RegistrationException {

        System.out.println(" Willkommen beim Smart shopping ");
        System.out.println(  " gebe 1 wenn du Account hast, 0 wenn sie regestrieren müssen: ");
        int checkRegister = in.nextInt();

        switch (checkRegister) {
            case 1: {
                customer_id = CustomerRegister.login();

                break;
            }

            case 0: {
                CustomerRegister.register();
                customer_id = CustomerRegister.login();
                break;
            }

        }


        sqlOrders.createTransaction(customer_id);
        sqlOrders.updateRef();
        menu();
        while (aktion != 6) {
            System.out.println(" wähle eine Aktion : \n");
            aktion = in.nextInt();

            switch (aktion) {
                //menu zeigen
                case 0: {
                    sqlOrders.readDataBase("lebensmittel");
                    menu();
                    break;
                }
                case 1: {
                    sqlOrders.readDataBase("getraenke");
                    menu();
                    break;
                }
                //bestellen
                case 2: {
                    System.out.println(" wähle 1 um lebensmittel zu bestellen, 2 zu Getraenke");
                    int select = in.nextInt();

                    if (select == 1) {
                        sqlOrders.readDataBase("lebensmittel");
                        bestell("lebensmittel");
                    }
                    if (select == 2) {
                        sqlOrders.readDataBase("getraenke");
                        bestell("getraenke");
                    }

                    break;
                }
                case 3: {
                    printRechnung();
                    menu();
                    break;
                }
                case 4: {
                    removeArtikel();
                    menu();
                    break;
                }
                // pay
                case 6: {
                    try {
                        zahlungProcess();
                    } catch (NullPointerException e) {
                        zahlungProcess();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                }
                //exit
                case 7: {
                    sqlOrders.deleteTransaction();
                    sqlOrders.deleteAll();
                    System.out.println(" Vielen Dan für Ihren Besuch bei Smart Shop");
                    return;
                }
                case 8: {
                    CustomerRegister.register();
                }
            }
        }
    }


    public boolean bestell(String tableName) throws SQLException, ClassNotFoundException {
        System.out.println("wähle die Produkte bei ID, und 111 zu beenden");
        produktID = in.nextInt();

        if (produktID != 111) {
            sqlOrders.insertIntoDataBase(tableName, produktID);
            bestellt = true;
            bestell(tableName);
        } else {
            System.out.println();
            menu();
        }
        return bestellt;

    }


    public void removeArtikel() throws SQLException, ClassNotFoundException {
        System.out.println("Bitte geben Sie die Nummer des Produktes zu entfernen ");
        stkToRemove = in.nextInt();
        sqlOrders.deleteItem();
    }


    public void printRechnung() throws SQLException, ClassNotFoundException {
        double d = sqlOrders.readTransaction();
        steuer = d * 0.19;

        System.out.println(d);
        System.out.println(" \nIhr gesamter anzuzahlender Betrag ist " + dc.format(d) +
                " €.\n inklusive mwst i.h.v 19% :" + dc.format(steuer) +
                " €. \n Bestelldatum " + sd.format(date) + ".\n ");
    }

    public double getSumme() throws SQLException, ClassNotFoundException {
        return SQLOrders.getTotalRechnung();
    }


    public void menu() {
        String[] arr = new String[9];

        arr[0] = ">> Wähle 0, um Lebensmittelsliste  anzuzeigen ";
        arr[1] = ">> Wähle 1, um Getränksliste  anzuzeigen ";
        arr[2] = ">> Wähle 2, um zu bestellen ";
        arr[3] = ">> Wähle 3, um gesamte Rechnung zu zeigen";
        arr[4] = ">> Wähle 4, um Artikel aus Einkauflist zu entfernen";
        arr[5] = ">> Wähle 5, um Menu zu zeigen";
        arr[6] = ">> Wähle 6, um Menu zu bezahlen";
        arr[7] = ">> Wähle 7, zu beenden (CANCEL)";
        arr[8] = ">> Wähle 8, als neuer Kunde zu regestrieren";
        for (String s : arr) {
            System.out.println(s);
        }
    }

    public void zArtWählen() {

        String[] zArtArr = new String[4];
        zArtArr[0] = ">> Wähle 0, für Cash ";
        zArtArr[1] = ">> Wähle 1, für Credit oder Debit ";
        zArtArr[2] = ">> Wähle 2, für PayBal ";
        zArtArr[3] = ">> Wähle 3, für Voucher auflösen ";
        for (int i = 0; i < zArtArr.length; i++) {
            System.out.println(zArtArr[i]);
        }
    }

    public Zahlung getZahlungArt() {
        zArtWählen();
        try {
            System.out.println(" Bitte geben Sie die Art der Zahlung : ");
            zArt = in.nextInt();

            switch (zArt) {
                case 0: {
                    return new Cash();
                }
                case 1: {
                    return new CreditDebit();
                }
                case 2: {
                    return new PayBal();
                }
                case 3: {
                    return new Voucher();
                }
            }
        } catch (NullPointerException e) {
            System.out.println(" Bitte geben Sie eine Art der Zalung ");
        }
        return null;
    }

    public void zahlungProcess() throws SQLException, ClassNotFoundException, InterruptedException {
        double d = getSumme();
        while (d > 0) {
            double amount = getZahlungArt().abrechnen();
            d -= amount;

            if (d >= 0) {
                System.out.println(" Der zuzahlender Betrag ist i.H.v. " + dc.format(d) + " €. " + "\n");

            }

            if (d < 0) {
                System.out.println(" Zurückbetrag ist . " + dc.format((d)) + " €.\n " +
                        "Vielen Dank für Ihren Einkauf beim Smart Shop.\n");
                Cash.setPaymentCashBack(d);

            }

        }

        sqlOrders.setValue();
        SQLOrders.readFinalStatus();
        System.out.println(
                "Vielen Dank für Ihren Einkauf beim Smart Shop.\n");
    }
}