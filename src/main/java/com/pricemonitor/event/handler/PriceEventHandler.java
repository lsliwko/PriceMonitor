package com.pricemonitor.event.handler;

import javafx.application.Platform;

import com.pricemonitor.dialog.domain.ObservablePriceData;
import com.pricemonitor.dialog.domain.Price;
import com.pricemonitor.event.Event;
import com.pricemonitor.event.PriceChangeEvent;

public class PriceEventHandler implements EventHandler {

	final private String instrument;
	final private String source;
	
    final private ObservablePriceData priceData;
    
    public PriceEventHandler(String source, String instrument, ObservablePriceData priceData) {
        if (instrument==null) throw new IllegalArgumentException("instrument is null");
        if (source==null) throw new IllegalArgumentException("source is null");
        if (priceData==null) throw new IllegalArgumentException("priceData is null");
        
    	this.instrument	= instrument;
    	this.source		= source;
        this.priceData  = priceData;
    }

    @Override
    public boolean process(Event event) {
        if (event instanceof PriceChangeEvent) {
            PriceChangeEvent priceChangeEvent   = (PriceChangeEvent)event;
            
            //filter our every non-matching event
            if (
            		(!source.equals(priceChangeEvent.getSource()))||
            		(!instrument.equals(priceChangeEvent.getInstrument()))
            ) return false;
            
            
            //NOTE
            //PriceChangeEvent and Price have the same values
            //it's code duplication, however I prefer to keep separately server and client objects
            final Price price = new Price(
                    priceChangeEvent.getSource(),
                    priceChangeEvent.getInstrument(),
                    priceChangeEvent.getAsk(),
                    priceChangeEvent.getOffer(),
                    priceChangeEvent.getTimestamp()
            );
            
            //dspatch to EDT
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    priceData.addPrice(price);
                }
            });
            
            //event processed
            return true;
        }
        
        return false;
    }
}
