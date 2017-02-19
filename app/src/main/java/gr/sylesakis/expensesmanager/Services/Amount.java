package gr.sylesakis.expensesmanager.Services;

import java.util.Currency;

/**
 * Created by SakaroS on 10/12/2016.
 */

public class Amount {
    static final private Currency currency = Currency.getInstance("EUR");
    private String price;

    public Amount() { }

    public Amount(String price) {
        this.price = new String(price);
    }

    public void setPrice(String price) {
        this.price = new String(price);
    }

    public String getPrice(){
        return price;
    }

    public static String getSymbol(){ return currency.getSymbol();
    }

    @Override
    public String toString() {
        return this.getPrice() +Amount.getSymbol();
    }
}
