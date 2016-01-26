package com.pricemonitor.dialog.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Price {
    
    //NOTE
    //PriceChangeEvent and Price have the same values
    //it's code duplication, however I prefer to keep separately server and client objects

    final private String source;
    final private String instrument;
    final private BigDecimal ask;
    final private BigDecimal offer;
    final private LocalDateTime timestamp;

    public Price(String source, String instrument, BigDecimal ask, BigDecimal offer, LocalDateTime timestamp) {
        if (source==null) throw new IllegalArgumentException("source is null");
        if (instrument==null) throw new IllegalArgumentException("instrument is null");
        if (ask==null) throw new IllegalArgumentException("ask is null");
        if (offer==null) throw new IllegalArgumentException("offer is null");
        if (timestamp==null) throw new IllegalArgumentException("timestamp is null");
        
        this.source = source;
        this.instrument = instrument;
        this.ask = ask;
        this.offer = offer;
        this.timestamp = timestamp;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String getSource() {
        return source;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public BigDecimal getOffer() {
        return offer;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getInstrument() {
        return instrument;
    }
}