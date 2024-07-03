package com.leadsIq.stockPriceAnalytics.domain.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String UNEXPECTED_ERROR_MESSAGE = "Sorry, an unexpected error occurred.";
    public static final String ENTITY_NOT_FOUND_ERROR_MESSAGE = "Entity not found.";
    public static final String STOCK_PRICE_NOT_FOUND_ERROR_MESSAGE =
        "Stock data not found for symbol %s and date %s";
    public static final String INVALID_WEEK_PARAMETER_ERROR_MESSAGE =
        "Invalid week parameter format %s";
}
