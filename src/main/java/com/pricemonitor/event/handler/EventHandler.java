package com.pricemonitor.event.handler;

import com.pricemonitor.event.Event;

public interface EventHandler {

    boolean process(Event event);
}
