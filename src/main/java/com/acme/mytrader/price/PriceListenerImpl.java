package com.acme.mytrader.price;

import com.acme.mytrader.execution.ExecutionService;

public class PriceListenerImpl implements PriceListener {

    private final PriceSource priceSource;

    private final ExecutionService executionService;

    private final String stockName;

    private final double threshold;

    private final int volume;

    public PriceListenerImpl(final PriceSource priceSource,
                             final ExecutionService executionService,
                             final String stockName,
                             final double threshold,
                             final int volume) {
        this.priceSource = priceSource;
        this.executionService = executionService;
        this.stockName = stockName;
        this.threshold = threshold;
        this.volume = volume;
        this.priceSource.addPriceListener(this);
    }

    @Override
    public void priceUpdate(String security, double price) {
        if (price < threshold) {
            executionService.buy(security, price, volume);
            priceSource.removePriceListener(this);
        }
    }

    @Override
    public String getStockName() {
        return stockName;
    }
}
