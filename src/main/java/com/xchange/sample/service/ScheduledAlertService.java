package com.xchange.sample.service;

import com.xchange.sample.service.dto.AlertDTO;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
@EnableScheduling
public class ScheduledAlertService {

    public static final String TOPIC_ALERTS = "/topic/alerts";

    private final Logger log = LoggerFactory.getLogger(ScheduledAlertService.class);

    @Autowired
    private AlertService alertService;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Value("${alert.check.interval.in.milliseconds}")
    String interval;
    @Value("${alert.check.interval.in.milliseconds}")
    String initialDelay;

    @Scheduled(fixedRateString = "${alert.check.interval.in.milliseconds}",
            initialDelayString = "${alert.check.initial.delay.in.milliseconds}")
    public void checkAlerts(){
        log.trace("Running scheduled task, timestamp {}, interval: {} [ms], inital delay: {} [ms]",
                Instant.now(), interval, initialDelay);

        List<AlertDTO> alerts = alertService.listAlerts();

        alerts.forEach(alert -> checkAlert(alert));
    }

    private void checkAlert(AlertDTO alert) {
        try {
            log.trace("Checking currency pair {}, limit {}", alert.getPair(), alert.getLimit());
            long lastPrice = getLastPrice(new CurrencyPair(alert.getPair()));
            log.trace("Market price is {}", lastPrice, alert.getLimit());
            long aboveLimit = lastPrice - alert.getLimit().longValue();
            if (aboveLimit > 0) {
                log.info("Alert - market price for {} is {} above limit: {} (limit: {})",
                        aboveLimit, alert.getPair(), lastPrice, alert.getLimit());

                alert.setTimestamp(Instant.now());
                messagingTemplate.convertAndSend(TOPIC_ALERTS, alert);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private long getLastPrice(CurrencyPair pair) throws IOException {
        Exchange bitstamp = ExchangeFactory.INSTANCE.createExchange(BitstampExchange.class.getName());
        MarketDataService marketDataService = bitstamp.getMarketDataService();
        Ticker ticker = marketDataService.getTicker(pair);

        return ticker.getLast().longValue();
    }
}
