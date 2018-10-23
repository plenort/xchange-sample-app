package com.xchange.sample.unit.web.rest;

import com.xchange.sample.dao.domain.Alert;
import com.xchange.sample.dao.repository.AlertRepository;
import com.xchange.sample.service.AlertService;
import com.xchange.sample.web.rest.AlertResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowm.xchange.currency.CurrencyPair;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {AlertRepository.class, AlertService.class, AlertResource.class})
public class AlertResourceTest {

    private static final String INCORRECT_CURRENCY_PAIR = "incorrect_currency_pair";
    private static final String INCORRECT_LIMIT = "incorrect_limit";
    public static final long LIMIT = 50000L;
    private static final Alert CORRECT_ALERT = new Alert(CurrencyPair.BTC_USD.toString(), LIMIT);
    private static final Alert INCORRECT_ALERT = new Alert(INCORRECT_CURRENCY_PAIR, LIMIT);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertRepository alertRepository;

    @Test
    public void testGET() throws Exception {
        BDDMockito.given(alertRepository.findAll()).willReturn(Collections.singleton(CORRECT_ALERT));
        mockMvc.perform(get("/alert"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(CurrencyPair.BTC_USD.toString())));
    }

    @Test
    public void testPUTIncorrectCurrencyPair() throws Exception {
        mockMvc.perform(put("/alert?pair=" + INCORRECT_ALERT.getPair() + "&limit=" + CORRECT_ALERT.getLimit()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPUTIncorrectLimit() throws Exception {
        mockMvc.perform(put("/alert?pair=" + CORRECT_ALERT.getPair() + "&limit=" + INCORRECT_LIMIT))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testPUTCorrect() throws Exception {
        mockMvc.perform(put("/alert?pair=" + CORRECT_ALERT.getPair() + "&limit=" + CORRECT_ALERT.getLimit()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDELCorrect() throws Exception {
        mockMvc.perform(delete("/alert?pair=" + CORRECT_ALERT.getPair() + "&limit=" + CORRECT_ALERT.getLimit()))
                .andExpect(status().isOk());
    }

}
