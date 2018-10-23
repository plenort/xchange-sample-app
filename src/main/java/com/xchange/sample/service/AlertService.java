package com.xchange.sample.service;

import com.xchange.sample.dao.domain.Alert;
import com.xchange.sample.dao.repository.AlertRepository;
import com.xchange.sample.service.dto.AlertDTO;
import com.xchange.sample.web.errors.AlertAlreadyDefinedException;
import com.xchange.sample.web.errors.IncorrectCurrencyUsedException;
import org.apache.logging.log4j.util.Strings;
import org.knowm.xchange.currency.CurrencyPair;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class AlertService {

    private final Logger log = LoggerFactory.getLogger(AlertService.class);
    private final ModelMapper modelMapper;

    private AlertRepository alertRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository){
        this.alertRepository = alertRepository;
        modelMapper = new ModelMapper();
    }

    public AlertDTO addAlert(String pair, Long limit){
        Alert alert = new Alert();

        alert.setPair(pair);
        alert.setLimit(limit);
        alert.setTimestamp(Instant.now());

        validate(alert);

        alertRepository.save(alert);

        return modelMapper.map(alert, AlertDTO.class);
    }

    public AlertDTO removeAlert(String pair, Long limit){

        Optional<Alert> alert = alertRepository.findByPairAndLimit(pair, limit);
        if (alert.isPresent()) {
            alertRepository.delete(alert.get());

            return modelMapper.map(alert, AlertDTO.class);
        }

        return null;
    }

    public List<AlertDTO> listAlerts(){
        Iterable<Alert> alerts = alertRepository.findAll();
        Type listType = new TypeToken<List<AlertDTO>>() {}.getType();

        return modelMapper.map(alerts, listType);
    }

    public void validate(Alert alert) {
        if (Strings.isBlank(alert.getPair()))
            throw new IncorrectCurrencyUsedException();
        try {
            new CurrencyPair(alert.getPair());
        } catch (IllegalArgumentException e) {
            throw new IncorrectCurrencyUsedException(e.getMessage());
        }

        Optional<Alert> existingAlert = alertRepository.findByPairAndLimit(alert.getPair(), alert.getLimit());
        if (existingAlert.isPresent()) {
            throw new AlertAlreadyDefinedException(alert.getPair(), alert.getLimit());
        }
    }
}
