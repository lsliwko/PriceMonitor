package com.pricemonitor.event.bus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.pricemonitor.event.Event;
import com.pricemonitor.event.handler.EventHandler;

public class EventBus {
    
    final private static List<EventHandler> eventHandlers    = new CopyOnWriteArrayList<EventHandler>();

    public static void register(EventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }
    
    public static boolean unregister(EventHandler eventHandler) {
        return eventHandlers.remove(eventHandler);
    }
    
    public static void post(Event event) {
    	//System.out.println("Event:" + event);
    	
        for (EventHandler eventHandler : eventHandlers) {
            eventHandler.process(event);
        }
        
        //for Java 8
        //eventHandlers.forEach(eventHandler -> eventHandler.process(event));
    }
}
