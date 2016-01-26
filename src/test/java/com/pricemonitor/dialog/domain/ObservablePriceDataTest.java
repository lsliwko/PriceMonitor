package com.pricemonitor.dialog.domain;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Test;

import com.pricemonitor.dialog.domain.InstrumentSourceKey;
import com.pricemonitor.dialog.domain.ObservablePriceData;
import com.pricemonitor.dialog.domain.Price;

public class ObservablePriceDataTest {

    private LocalDateTime date    = LocalDateTime.now();
    
    private Price createDummyPrice(String instrument) {
        //increase date, so prices generated are sequential
        date    = date.plusSeconds(1);
        return new Price(
                "source",
                instrument,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                date);
    }
    
    @Test
    public void testGetObservableLatestPricesSingle() {
        ObservablePriceData priceData   = new ObservablePriceData(50);
        
        Price priceA    = createDummyPrice("a");
        
        priceData.addPrice(priceA);
        
        assertEquals("Should return price", Arrays.asList(priceA), priceData.getObservableLatestPrices());
     }
    
    @Test
    public void testGetObservableLatestPricesMulti() {
        ObservablePriceData priceData   = new ObservablePriceData(50);
        
        Price priceA1    = createDummyPrice("a");
        Price priceA2    = createDummyPrice("a");
        Price priceB    = createDummyPrice("b");
        Price priceC    = createDummyPrice("c");
        
        priceData.addPrice(priceA1);
        priceData.addPrice(priceA2);
        priceData.addPrice(priceB);
        priceData.addPrice(priceC);
        
        assertEquals("Should return only latest prices", Arrays.asList(priceA2,priceB,priceC), priceData.getObservableLatestPrices());
    }
    
    @Test
    public void testGetObservableHistoricalPrices() {
        ObservablePriceData priceData   = new ObservablePriceData(50);
        
        Price priceA1    = createDummyPrice("a");
        Price priceA2    = createDummyPrice("a");
        Price priceA3    = createDummyPrice("a");
        Price priceB    = createDummyPrice("b");
        Price priceC    = createDummyPrice("c");
        
        priceData.addPrice(priceA1);
        priceData.addPrice(priceA2);
        priceData.addPrice(priceA3);
        priceData.addPrice(priceB);
        priceData.addPrice(priceC);
        
        assertEquals("Should return historical prices", Arrays.asList(priceA3,priceA2,priceA1), priceData.getObservableHistoricalPrices(new InstrumentSourceKey(priceA1)));
    }
    
    @Test
    public void testGetObservableHistoricalPricesSorted() {
        ObservablePriceData priceData   = new ObservablePriceData(50);
        
        Price priceA1    = createDummyPrice("a");
        Price priceA2    = createDummyPrice("a");
        Price priceA3    = createDummyPrice("a");
        
        priceData.addPrice(priceA2);
        priceData.addPrice(priceA3);
        
        priceData.addPrice(priceA1);
        
        assertEquals("Should return historical prices in correct order", Arrays.asList(priceA3,priceA2,priceA1), priceData.getObservableHistoricalPrices(new InstrumentSourceKey(priceA1)));
    }
    
    @Test
    public void testGetObservableHistoricalPricesOverLimit() {
        ObservablePriceData priceData   = new ObservablePriceData(3);
        
        Price priceA1    = createDummyPrice("a");
        Price priceA2    = createDummyPrice("a");
        Price priceA3    = createDummyPrice("a");
        Price priceA4    = createDummyPrice("a");
        Price priceA5    = createDummyPrice("a");
        
        priceData.addPrice(priceA1);
        priceData.addPrice(priceA2);
        priceData.addPrice(priceA3);       
        priceData.addPrice(priceA4);        
        priceData.addPrice(priceA5);
        
        assertEquals("Should return only three top historical prices", Arrays.asList(priceA5,priceA4,priceA3), priceData.getObservableHistoricalPrices(new InstrumentSourceKey(priceA1)));
    }
}
