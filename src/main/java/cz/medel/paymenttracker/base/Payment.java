package cz.medel.paymenttracker.base;

/**
 * Created by medel on 3/17/16.
 */
public class Payment {

    private String currency;
    private int amount;

    public Payment(String currency, int amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
