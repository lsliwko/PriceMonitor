package com.pricemonitor.dialog.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ObservablePriceData {
    
	final private ObservableList<Price> latestPrices	= createEmptyObservablePriceList();
	final private ConcurrentHashMap<InstrumentSourceKey, Integer> latestPricesIndexMap = new ConcurrentHashMap<InstrumentSourceKey, Integer>();
	
    final private ConcurrentHashMap<InstrumentSourceKey, ObservableList<Price>> historicalPricesMap  = new ConcurrentHashMap<InstrumentSourceKey, ObservableList<Price>>();
    final private int historicalPricesLimit;
    
    
    static private Comparator<Price> sortByTimestampDesc    = new Comparator<Price>() {

        @Override
        public int compare(Price objA, Price objB) {
            return -objA.getTimestamp().compareTo(objB.getTimestamp());
        }
    };

    public ObservablePriceData(int historicalPricesLimit) {
        this.historicalPricesLimit   = historicalPricesLimit;
    }

    public void addPrice(Price price) {
        if (price==null) throw new IllegalArgumentException("price is null");
        
        InstrumentSourceKey key = new InstrumentSourceKey(price);
        
        //observable lists are not concurrent
        //update latest prices
        //we want to keep it in list to make it observable later  
        Integer index = latestPricesIndexMap.putIfAbsent(key, latestPricesIndexMap.size());
        if (index==null) {
            index   = latestPricesIndexMap.get(key);
            latestPrices.add(index, price);
        } else {
            latestPrices.set(index, price);
        }
        
        //atomically retrieve/add-and-retrieve list of prices
        historicalPricesMap.putIfAbsent(key, createEmptyObservablePriceList());
        ObservableList<Price> prices = historicalPricesMap.get(key);
        
        //add price in order in case some price with previous timestamp appears later
        //it's also faster than Collectins.sort
        addInOrder(prices, price, sortByTimestampDesc);
            
        //remove tail of items past limit
        if (prices.size()>historicalPricesLimit) {
        	prices.subList(historicalPricesLimit, prices.size()).clear();
        }   
    }
    
    
    //from http://www.javacreed.com/sorting-a-copyonwritearraylist/
    private static <T> int addInOrder(final ObservableList<T> list, final T item, Comparator<T> comparator) {
        final int insertAt;
        // The index of the search key, if it is contained in the list;
        // otherwise, (-(insertion point) - 1)
        final int index = Collections.binarySearch(list, item, comparator);
        if (index < 0) {
            insertAt = -(index + 1);
        } else {
            insertAt = index + 1;
        }

        list.add(insertAt, item);
        return insertAt;
    }
    
    public ObservableList<Price> getObservableLatestPrices() {
        return latestPrices;
    }
    
    public ObservableList<Price> getObservableHistoricalPrices(InstrumentSourceKey key) {
        return historicalPricesMap.getOrDefault(key, createEmptyObservablePriceList());
    }

	private static ObservableList<Price> createEmptyObservablePriceList() {
		return FXCollections.observableArrayList(new CopyOnWriteArrayList<Price>());
	}
}
