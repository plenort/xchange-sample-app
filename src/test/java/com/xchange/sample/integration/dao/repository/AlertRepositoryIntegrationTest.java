package com.xchange.sample.integration.dao.repository;


import com.xchange.sample.dao.domain.Alert;
import com.xchange.sample.dao.repository.AlertRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AlertRepositoryIntegrationTest {

    public static final long LIMIT = 5000L;
    private static final Alert TEST_ALERT = new Alert(CurrencyPair.BTC_USD.toString(), LIMIT);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlertRepository alertRepository;

    @Test
    public void alertDaoTest(){
        alertRepository.save(TEST_ALERT);

        Optional<Alert> alertFound = alertRepository.findByPairAndLimit(TEST_ALERT.getPair(), TEST_ALERT.getLimit());
        assertThat(alertFound.isPresent()).isTrue();
        assertThat(alertFound.get()).isEqualTo(TEST_ALERT);

        alertRepository.delete(TEST_ALERT);

        alertFound = alertRepository.findByPairAndLimit(TEST_ALERT.getPair(), TEST_ALERT.getLimit());
        assertThat(alertFound.isPresent()).isFalse();
    }
}
