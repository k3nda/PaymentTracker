package cz.medel.paymenttracker.base;

import cz.medel.paymenttracker.constants.ExchangeRate;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by medel on 3/21/16.
 */
public class BalanceCounter implements Runnable {

    private List<Payment> payments;
    private Map<String, Integer> balance;

    private boolean running;

    public BalanceCounter(List<Payment> payments) {
        this.payments = payments;
        this.balance = new HashMap<>();
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            try {
                doPayments();
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                System.out.println("Thread was Interrupted.");
            }
        }
    }

    public void terminate() {
        running = false;
    }

    private void doPayments() {
        if (!payments.isEmpty()) {
            for (Payment payment : payments) {
                String currency = payment.getCurrency();
                int newAmount = payment.getAmount();

                refreshBalance(currency, newAmount);
            }
            payments.clear();
        }
        System.out.println("Balance check: " + printBalance());
    }

    private void refreshBalance(String currency, int amount) {
        if (balance.containsKey(currency)) {
            int sum = balance.get(currency) + amount;
            balance.put(currency, sum);
        } else
            balance.put(currency, amount);
    }

    private String printBalance() {
        String result = "";

        for (Map.Entry<String, Integer> entry : balance.entrySet()) {
            if (entry.getValue() != 0)
                result += entry + getUSDValue(entry) + " ";
        }

        return result;
    }

    private String getUSDValue(Map.Entry<String, Integer> entry) {
        String result = "";
        DecimalFormat decimalFormat = new DecimalFormat(".##");
        double rate;

        if (entry.getKey().equals("USD"))
            return result;

        if (containsRate(entry.getKey())) {
            rate = ExchangeRate.valueOf(entry.getKey()).getValue();
            double usdValue = entry.getValue() * rate;

            result = "(USD " + String.valueOf(decimalFormat.format(usdValue)) + ")";
        } else
            result = "(USD rate is not defined)";

        return result;
    }

    private boolean containsRate(String currency) {
        for (ExchangeRate exchangeRate : ExchangeRate.values()) {
            if (exchangeRate.name().equals(currency))
                return true;
        }

        return false;
    }
}
