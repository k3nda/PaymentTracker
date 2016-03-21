package cz.medel.paymenttracker;

import cz.medel.paymenttracker.base.Payment;
import cz.medel.paymenttracker.base.BalanceCounter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by medel on 3/17/16.
 */
public class PaymentTracker {

    public PaymentTracker() {
    }

    public void start() {
        List<Payment> input = loadInputPayments("payments.txt");

        BalanceCounter balanceCounter = new BalanceCounter(input);

        Thread t = new Thread(balanceCounter);
        t.start();

        Scanner scanner = new Scanner(System.in);
        String consoleInput;

        while (true) {
            consoleInput = scanner.nextLine();

            if (consoleInput.equals("quit"))
                break;

            if (testInputFormat(consoleInput))
                input.add(createPaymentFromInput(consoleInput));
            else
                System.out.println("Wrong input format! Correct format is: \"XXX amount\", where XXX is currency e.g. CZK");
        }

        balanceCounter.terminate();
        t.interrupt();
        System.out.println("Payment tracker finished.");
    }

    private List<Payment> loadInputPayments(String fileName) {
        List<Payment> result = new ArrayList<>();

        try {
            URI uri = getClass().getResource(fileName).toURI();
            Map<String, String> env = new HashMap<>();
            FileSystem fs = FileSystems.newFileSystem(uri, env);
            Path path = Paths.get(uri);

            List<String> input = Files.readAllLines(path);

            for (String line : input) {
                if (testInputFormat(line)) {
                    Payment payment = createPaymentFromInput(line);
                    result.add(payment);
                } else
                    System.out.println("This line does not match format: " + line);
            }

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private boolean testInputFormat(String input) {
        Pattern p = Pattern.compile("[A-Z]{3} -??\\d+");

        Matcher m = p.matcher(input);

        return m.matches();
    }

    private Payment createPaymentFromInput(String line) {
        Payment result;

        String[] dividedString = line.split(" ");
        result = new Payment(dividedString[0], Integer.parseInt(dividedString[1]));

        return result;
    }
}
