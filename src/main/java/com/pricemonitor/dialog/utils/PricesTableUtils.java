package com.pricemonitor.dialog.utils;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import com.pricemonitor.dialog.domain.Price;

public class PricesTableUtils {
	
	static final public int SORT_MODE_NONE	= 0;
	static final public int SORT_MODE_INSTRUMENT_SOURCE	= 1;

    @SuppressWarnings("unchecked")
    static public TableView<Price> createPricesTableView(ObservableList<Price> pricesList, int initialSortMode) {
        if (pricesList == null)
            throw new IllegalArgumentException("pricesList is null");

        final TableView<Price> table = new TableView<Price>();

        TableColumn<Price, String> instrumentColumn = new TableColumn<Price, String>("Instrument");
        instrumentColumn.setMinWidth(200);
        instrumentColumn.setCellValueFactory(new PropertyValueFactory<Price, String>("instrument"));

        TableColumn<Price, String> sourceColumn = new TableColumn<Price, String>("Source");
        sourceColumn.setMinWidth(200);
        sourceColumn.setCellValueFactory(new PropertyValueFactory<Price, String>("source"));

        TableColumn<Price, BigDecimal> askColumn = new TableColumn<Price, BigDecimal>("Ask");
        askColumn.setMinWidth(50);
        askColumn.setCellValueFactory(new PropertyValueFactory<Price, BigDecimal>("ask"));

        TableColumn<Price, BigDecimal> offerColumn = new TableColumn<Price, BigDecimal>("Offer");
        offerColumn.setMinWidth(50);
        offerColumn.setCellValueFactory(new PropertyValueFactory<Price, BigDecimal>("offer"));

        TableColumn<Price, String> timestampColumn = new TableColumn<Price, String>("Timestamp");
        timestampColumn.setMinWidth(200);
        //format timedate cell
        timestampColumn.setCellValueFactory(new Callback<CellDataFeatures<Price, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Price, String> param) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY   HH:MM:ss.SSS");
                return new SimpleObjectProperty<String>(param.getValue().getTimestamp().format(formatter));
            }
        });

        // timestampColumn.setCellValueFactory(new PropertyValueFactory<Price,LocalDateTime>("timestamp"));
        
        // for sorting to work with observable list we need to bind table comparator to sorted list comparator
        SortedList<Price> sortedPricesList = new SortedList<Price>(pricesList);
        sortedPricesList.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedPricesList);
        table.getColumns().addAll(instrumentColumn, sourceColumn, askColumn, offerColumn, timestampColumn);
        
        //default sorting
        switch (initialSortMode) {
	        case SORT_MODE_INSTRUMENT_SOURCE:
		        table.getSortOrder().setAll(instrumentColumn, sourceColumn);
		        break;
        }
        
        return table;
    }
}
