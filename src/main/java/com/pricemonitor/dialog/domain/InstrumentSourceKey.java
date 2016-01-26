package com.pricemonitor.dialog.domain;

//import org.apache.commons.lang3.builder.HashCodeBuilder;
//import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class InstrumentSourceKey {

    final private String instrument;
    final private String source;
    
    public InstrumentSourceKey(String instrument, String source) {
        if (instrument==null) throw new IllegalArgumentException("instrument is null");
        if (source==null) throw new IllegalArgumentException("source is null");
        
        this.instrument = instrument;
        this.source = source;
    }
    
    public InstrumentSourceKey(Price price) {
        if (price==null) throw new IllegalArgumentException("price is null");
        
        this.instrument = price.getInstrument();
        this.source = price.getSource();
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

    public String getInstrument() {
        return instrument;
    }

    public String getSource() {
        return source;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((instrument == null) ? 0 : instrument.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
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
        InstrumentSourceKey other = (InstrumentSourceKey) obj;
        if (instrument == null) {
            if (other.instrument != null)
                return false;
        } else if (!instrument.equals(other.instrument))
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        return true;
    }
}
