package com.acme.mytrader.price;

import com.acme.mytrader.execution.ExecutionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class PriceListenerTest {

    private static final String STOCK_NAME = "IBM";

    @Mock
    private PriceSource priceSource;

    @Mock
    private ExecutionService executionService;

    private PriceListener priceListener;

    @Before
    public void init() {
        priceListener = new PriceListenerImpl(priceSource, executionService, STOCK_NAME, 55.00, 100);
    }

    @Test
    public void getNameReturnsStockName() {
        assertThat(priceListener.getStockName(), equalTo(STOCK_NAME));
    }

    @Test
    public void priceUpdateWithHighValueDoesNotCauseBuyingAndNotRemovingTheListener() {
        priceListener.priceUpdate("testSecurity", 100.00);
        verifyNoMoreInteractions(executionService);
        verify(priceSource, never()).removePriceListener(priceListener);
    }

    @Test
    public void priceUpdateWithLowValueCausesBuyingAndRemovingTheListener() {
        priceListener.priceUpdate("testSecurity", 50.00);
        verify(executionService).buy("testSecurity", 50.00, 100);
        verify(priceSource).removePriceListener(priceListener);
    }
}
