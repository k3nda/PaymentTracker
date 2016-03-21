package cz.medel.paymenttracker.constants;

/**
 * Created by medel on 3/21/16.
 */
public enum ExchangeRate {
    HKD("0.128954"), RMB("0.154632"), CZK("0.04168");

    private String value;

    ExchangeRate(String s) {
        value = s;
    }

    public Double getValue() {
        return Double.valueOf(value);
    }
}
