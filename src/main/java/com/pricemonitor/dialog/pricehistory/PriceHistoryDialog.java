package com.pricemonitor.dialog.pricehistory;

import java.util.concurrent.ConcurrentHashMap;

import com.pricemonitor.dialog.domain.InstrumentSourceKey;
import com.pricemonitor.dialog.domain.ObservablePriceData;
import com.pricemonitor.dialog.domain.Price;
import com.pricemonitor.dialog.utils.PricesTableUtils;

import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PriceHistoryDialog extends Scene {

    // we keep a cache of all opened historical prices windows
    // it could be done is factory, but let's not over-engineer it
    final private static ConcurrentHashMap<InstrumentSourceKey, Stage> priceHistoryWindowsMap  = new ConcurrentHashMap<InstrumentSourceKey, Stage>();

    public PriceHistoryDialog(InstrumentSourceKey key, ObservablePriceData priceData) {
        super(new StackPane(), 700, 500);
        
        if (key==null) throw new IllegalArgumentException("key is null");
        if (priceData==null) throw new IllegalArgumentException("priceData is null");

        TableView<Price> table = PricesTableUtils.createPricesTableView(priceData.getObservableHistoricalPrices(key));
         
        ((StackPane) getRoot()).getChildren().add(table);
    }
    
    static private Stage createPriceHistoryWindow(InstrumentSourceKey key, ObservablePriceData priceData) {
        Stage stage = new Stage();
        stage.setTitle(key.getInstrument() + " @ " + key.getSource());
        stage.setScene(new PriceHistoryDialog(key, priceData));
        
        return stage;
    }
    
    static public void openPriceHistoryWindow(InstrumentSourceKey key, ObservablePriceData priceData) {
        Stage priceHistoryWindow   = priceHistoryWindowsMap.putIfAbsent(key, createPriceHistoryWindow(key, priceData));
        if (priceHistoryWindow==null) {
            priceHistoryWindow  = priceHistoryWindowsMap.get(key);
        }
        
        priceHistoryWindow.show();
        priceHistoryWindow.toFront();
    }
}
