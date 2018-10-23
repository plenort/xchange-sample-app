package com.xchange.sample.service.dto;

import java.time.Instant;
import java.util.Objects;

public class AlertDTO {

    private String pair;
    private Long limit;
    private Instant timestamp;

    public AlertDTO(){
        this(null, null);
    }

    public AlertDTO(String pair, Long limit) {
        this.pair = pair;
        this.limit = limit;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertDTO alertDTO = (AlertDTO) o;
        return Objects.equals(pair, alertDTO.pair) &&
                Objects.equals(limit, alertDTO.limit);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pair, limit);
    }
}
