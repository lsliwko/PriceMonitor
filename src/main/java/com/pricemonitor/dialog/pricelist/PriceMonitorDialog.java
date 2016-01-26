package com.pricemonitor.dialog.pricelist;

import com.pricemonitor.dialog.domain.InstrumentSourceKey;
import com.pricemonitor.dialog.domain.ObservablePriceData;
import com.pricemonitor.dialog.domain.Price;
import com.pricemonitor.dialog.pricehistory.PriceHistoryDialog;
import com.pricemonitor.dialog.utils.PricesTableUtils;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class PriceMonitorDialog extends Scene {

    public PriceMonitorDialog(final ObservablePriceData priceData) {  
        super(new StackPane(), 900, 600);
        
        if (priceData==null) throw new IllegalArgumentException("priceData is null");

        final TableView<Price> table = PricesTableUtils.createPricesTableView(priceData.getObservableLatestPrices(), PricesTableUtils.SORT_MODE_INSTRUMENT_SOURCE);
        
        // set click listener
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if ((event.isPrimaryButtonDown()) && (event.getClickCount() == 2)) {
                    
                    Price price = table.getSelectionModel().getSelectedItem();
                    InstrumentSourceKey key = new InstrumentSourceKey(price);
                    
                    PriceHistoryDialog.openPriceHistoryWindow(key, priceData);
                    System.out.println();
                }
            }
        });

        ((StackPane) getRoot()).getChildren().add(table);
    }
}