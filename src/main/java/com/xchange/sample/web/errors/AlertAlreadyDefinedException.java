package com.xchange.sample.web.errors;

public class AlertAlreadyDefinedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public AlertAlreadyDefinedException(String currencyPair, Long limit) {
        super(ErrorConstants.ALERT_ALREADY_DEFINED_TYPE,
                "Alert for currency pair: " + currencyPair + " and limit: " + limit + " is already defined!",
                "alerts", "alertxists");
    }
}
