package shopPackage;

public class Stock {


    static int anInt = 1;
    private int id;
    private String produkt_name;
    private double preis;

    public Stock(int id, String produkt_name, double preis) {
        this.id = id;
        this.produkt_name = produkt_name;
        this.preis = preis;
    }

    public int getId() {
        return id;
    }

    public String getProdukt_name() {
        return produkt_name;
    }

    public double getPreis() {
        return preis;
    }

    @Override
    public String toString() {
        return
                " Artikel_ID " + id +
                        ", produkt_name " + produkt_name + '\'' +
                        ", preis pro Stock " + preis +
                        '}';
    }
}
