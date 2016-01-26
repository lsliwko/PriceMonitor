package com.pricemonitor.dialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;

import com.pricemonitor.dialog.domain.ObservablePriceData;
import com.pricemonitor.dialog.pricelist.PriceMonitorDialog;
import com.pricemonitor.event.PriceChangeEvent;
import com.pricemonitor.event.bus.EventBus;
import com.pricemonitor.event.handler.PriceEventHandler;

import javafx.application.Application;
import javafx.stage.Stage;

public class PriceMonitor extends Application {
	
    final static String[] sources	= new String[] {
    		"source A",
    		"source B",
    		"source C"
    };
    
    final static String[] instruments = new String[] {
    		"instrument 0",
    		"instrument 1",
    		"instrument 2",
    		"instrument 3",
    		"instrument 4",
    		"instrument 5",
    		"instrument 6",
    		"instrument 7",
    		"instrument 8",
    		"instrument 9"
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        //create shared object for all price data
        ObservablePriceData priceData = new ObservablePriceData(50);
        
        //subscribe 10 instruments to 3 price sources
        for (String source : sources) {
        	for (String instrument : instruments) {
        		EventBus.register(new PriceEventHandler(source, instrument, priceData));
        	}
        }
          
        initFeedThreads();
        
        primaryStage.setTitle("Price monitor");
        primaryStage.setScene(new PriceMonitorDialog(priceData));
        primaryStage.show();
    }

    private void initFeedThreads() {
        //start five thread that will generate random price change events
        for (int i=0;i<5;i++) {
	        Thread feedThread = new Thread() {
	        	private Random random	= new Random();
	        	
	        	@Override
	        	public void run() {
	        		try {
	        			while (true) {
		        			//sleep random number of seconds
		        			Thread.sleep(random.nextInt(100));
		        			
		        			//create random price event
		        			EventBus.post(new PriceChangeEvent(
		        					sources[random.nextInt(sources.length)],
		        					instruments[random.nextInt(instruments.length)],
		        					BigDecimal.valueOf(random.nextDouble()+5d).setScale(2, RoundingMode.HALF_UP),
		        					BigDecimal.valueOf(random.nextDouble()+6d).setScale(2, RoundingMode.HALF_UP),
		        					LocalDateTime.now()
		        			));
	        			}
	        		} catch (InterruptedException e) {}
	        	}
	        };
	        feedThread.setDaemon(true);
	        feedThread.start();
        }
    }
}