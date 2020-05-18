package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceListenerImpl;
import com.acme.mytrader.price.PriceSource;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */
public class TradingStrategy {

    private final PriceSource priceSource;

    private final ExecutionService executionService;

    private final Map<String, PriceListener> priceListeners;

    public TradingStrategy(final PriceSource priceSource, final ExecutionService executionService) {
        this.priceSource = priceSource;
        this.executionService = executionService;
        this.priceListeners = new HashMap<>();
    }

    public void monitor(final String stockName, final double threshold, final int volume) {
        stopMonitoring(stockName);
        final PriceListener priceListener = new PriceListenerImpl(priceSource, executionService, stockName, threshold, volume);
        priceListeners.put(stockName, priceListener);
    }

    public void stopMonitoring(final String stockName) {
        if (priceListeners.containsKey(stockName)) {
            final PriceListener priceListener = priceListeners.remove(stockName);
            priceSource.removePriceListener(priceListener);
        }
    }
}
