package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TradingStrategyTest {

    private TradingStrategy tradingStrategy;

    @Mock
    private PriceSource priceSource;

    @Mock
    private ExecutionService executionService;

    @Before
    public void init() {
        tradingStrategy = new TradingStrategy(priceSource, executionService);
    }

    @Test
    public void monitorAStockWillAddAListener() {
        tradingStrategy.monitor("IBM", 55.0, 100);
        verify(priceSource, times(1)).addPriceListener(any(PriceListener.class));
    }

    @Test
    public void monitorAStockMultipleTimesWillRemovePreviousListeners() {
        tradingStrategy.monitor("IBM", 55.00, 100);
        tradingStrategy.monitor("IBM", 35.00, 200);
        verify(priceSource, times(2)).addPriceListener(any(PriceListener.class));
        verify(priceSource, times(1)).removePriceListener(any(PriceListener.class));
    }

    @Test
    public void monitorMultipleStocksWillAddAllListeners() {
        tradingStrategy.monitor("IBM", 55.00, 100);
        tradingStrategy.monitor("AVIVA", 75.00, 100);
        verify(priceSource, times(2)).addPriceListener(any(PriceListener.class));
        verify(priceSource, never()).removePriceListener(any(PriceListener.class));
    }

    @Test
    public void stopMonitoringWithoutAnyListenerWillNotRemoveAnyListener() {
        tradingStrategy.stopMonitoring("IBM");
        verify(priceSource, never()).removePriceListener(any(PriceListener.class));
    }

    @Test
    public void stopMonitoringAfterMonitoringAStockWillRemoveTheListener() {
        tradingStrategy.monitor("IBM", 55.0, 100);
        tradingStrategy.stopMonitoring("IBM");
        verify(priceSource, times(1)).addPriceListener(any(PriceListener.class));
        verify(priceSource, times(1)).removePriceListener(any(PriceListener.class));
    }
}
