package net.ketone.jmsmsgconv.stub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.*;

@Component
@Slf4j
public class Sender {

    private List<String> stocks = new ArrayList<String>();
    private Map<String, Double> lastPrice = new HashMap<String, Double>();

    {
        stocks.add("AAPL");
        stocks.add("GD");
        stocks.add("BRK.B");

        lastPrice.put("AAPL", 494.64);
        lastPrice.put("GD", 86.74);
        lastPrice.put("BRK.B", 113.59);
    }

    @Autowired
    JmsTemplate jmsTemplate;


    @Scheduled(fixedRate = 5000L) // every 5 seconds
    public void publishQuote() {
// Pick a random stock symbol
        Collections.shuffle(stocks);
        final String symbol = stocks.get(0);

// Toss a coin and decide if the price goes...
        if ((Math.random() * 10000) % 2 == 0) {
// ...up by a random 0-10%
            lastPrice.put(symbol, new Double(Math.round(lastPrice.get(symbol) * (1 + Math.random()) * 100) / 100));
        } else {
// ...or down by a similar random amount
            lastPrice.put(symbol, new Double(Math.round(lastPrice.get(symbol) * (1 - Math.random())/100.0) * 100) / 100);
        }

// Log new price locally
        log.info("Quote..." + symbol + " is now " + lastPrice.get(symbol));

        MessageCreator messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage("Quote..." + symbol + " is now " + lastPrice.get(symbol));
            }
        };

        jmsTemplate.send("rabbit-trader-channel", messageCreator);
    }

}
