package com.pricemonitor.dialog.handler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javafx.application.Application;
import javafx.stage.Stage;

import org.junit.BeforeClass;
import org.junit.Test;

import com.pricemonitor.dialog.domain.ObservablePriceData;
import com.pricemonitor.event.Event;
import com.pricemonitor.event.PriceChangeEvent;
import com.pricemonitor.event.handler.PriceEventHandler;

public class PriceEventHandlerTest {

    public static class AsNonApp extends Application {
        @Override
        public void start(Stage paramStage) throws Exception {
            // Noop
        }
    }

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        // Initialise Java FX
        Thread thread = new Thread() {
            public void run() {
                Application.launch(AsNonApp.class, new String[0]);
            }
        };
        thread.setDaemon(true);
        thread.start();

        // give it 1000 ms to finish
        Thread.sleep(1000);
    }

    @Test
    public void testProcessEventOnlyPriceChangeEvents() {
        ObservablePriceData priceData = new ObservablePriceData(50);

        PriceEventHandler priceEventHandler = new PriceEventHandler("source A", "instrument 1", priceData);

        boolean processed = priceEventHandler.process(new Event() {
            // dummy event
        });

        assertFalse("Should ignore event", processed);
    }

    @Test
    public void testProcessEventOnlySpecifiedInstrumentAndSource() {
        ObservablePriceData priceData = new ObservablePriceData(50);

        PriceEventHandler priceEventHandler = new PriceEventHandler("source A", "instrument 1", priceData);

        PriceChangeEvent priceChangeEvent1 = new PriceChangeEvent("source B", "instrument 1", BigDecimal.ZERO, BigDecimal.ZERO, LocalDateTime.now());

        PriceChangeEvent priceChangeEvent2 = new PriceChangeEvent("source A", "instrument 1", BigDecimal.ZERO, BigDecimal.ZERO, LocalDateTime.now());

        assertFalse("Should process event", priceEventHandler.process(priceChangeEvent1));
        assertTrue("Should process event", priceEventHandler.process(priceChangeEvent2));
    }
}
