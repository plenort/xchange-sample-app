package com.xchange.sample.integration.web.rest;

import com.xchange.sample.Application;
import com.xchange.sample.service.dto.AlertDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AlertResourceIntegraitonTest {

    public static final long LIMIT = 5000L;
    private static final AlertDTO TEST_ALERT = new AlertDTO(CurrencyPair.BTC_USD.toString(), LIMIT);
    public static final String ALERT_ENDPOINT = "/alert";
    public static final String PAIR_PARAMETER = "pair";
    public static final String LIMIT_PARAMETER = "limit";

    @Autowired
    private WebTestClient webClient;

    @Test
    public void alertIntegraitonTest() {
        this.webClient.get().uri(ALERT_ENDPOINT)
                .exchange().expectStatus().isOk().expectBodyList(AlertDTO.class).doesNotContain(TEST_ALERT);

        this.webClient.put().uri(uriBuilder -> uriBuilder.path(ALERT_ENDPOINT)
                .queryParam(PAIR_PARAMETER, TEST_ALERT.getPair())
                .queryParam(LIMIT_PARAMETER, TEST_ALERT.getLimit())
                .build())
                .exchange().expectStatus().isOk();

        this.webClient.get().uri(ALERT_ENDPOINT)
                .exchange().expectStatus().isOk().expectBodyList(AlertDTO.class).contains(TEST_ALERT);

        this.webClient.delete().uri(uriBuilder -> uriBuilder.path(ALERT_ENDPOINT)
                .queryParam(PAIR_PARAMETER, TEST_ALERT.getPair())
                .queryParam(LIMIT_PARAMETER, TEST_ALERT.getLimit())
                .build())
                .exchange().expectStatus().isOk();

        this.webClient.get().uri(ALERT_ENDPOINT)
                .exchange().expectStatus().isOk().expectBodyList(AlertDTO.class).doesNotContain(TEST_ALERT);
    }
}