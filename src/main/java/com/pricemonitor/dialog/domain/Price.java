package com.pricemonitor.dialog.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//import org.apache.commons.lang3.builder.HashCodeBuilder;
//import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

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

//    @Override
//    public int hashCode() {
//        return HashCodeBuilder.reflectionHashCode(this);
//    }
//
//    @Override
//    public String toString() {
//        return ReflectionToStringBuilder.toString(this);
//    }

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ask == null) ? 0 : ask.hashCode());
        result = prime * result + ((instrument == null) ? 0 : instrument.hashCode());
        result = prime * result + ((offer == null) ? 0 : offer.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Price other = (Price) obj;
        if (ask == null) {
            if (other.ask != null)
                return false;
        } else if (!ask.equals(other.ask))
            return false;
        if (instrument == null) {
            if (other.instrument != null)
                return false;
        } else if (!instrument.equals(other.instrument))
            return false;
        if (offer == null) {
            if (other.offer != null)
                return false;
        } else if (!offer.equals(other.offer))
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        return true;
    }
}
