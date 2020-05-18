package com.acme.mytrader.price;

public interface PriceListener {

    void priceUpdate(String security, double price);

    /**
     * Used by the server to notify by monitoring stock name
     *
     * @return Monitoring stock name
     */
    String getStockName();
}
