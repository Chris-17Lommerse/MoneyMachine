package MoneyMachine.factories;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class IbanGenerator {
    public String generateIBAN()
    {
        Random random = new Random();
        String cardPrefix = "NL";

        int r1 = random.nextInt(10);
        int r2 = random.nextInt(10);

        cardPrefix += Integer.toString(r1) + Integer.toString(r2);
        String card = "INHO0";
        cardPrefix += card;
        int n = 0;
        for(int i =0; i < 9; i++)
        {
            n = random.nextInt(10);
            cardPrefix += Integer.toString(n);
        }

        return cardPrefix;
    }
}
