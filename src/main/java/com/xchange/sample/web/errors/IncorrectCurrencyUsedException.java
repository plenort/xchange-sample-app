package com.xchange.sample.web.errors;

public class IncorrectCurrencyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;
    private static final String INCORRECT_OR_EMPTY_CURRENCY_PAIR_VALUE = "Incorrect or empty currency pair value.";

    public IncorrectCurrencyUsedException() {
        this(INCORRECT_OR_EMPTY_CURRENCY_PAIR_VALUE);
    }

    public IncorrectCurrencyUsedException(String msg) {
        super(ErrorConstants.INCORRECT_CURRENCY_USED_TYPE, INCORRECT_OR_EMPTY_CURRENCY_PAIR_VALUE + " " + msg,
                "alerts", "incorrectcurrency");
    }
}
