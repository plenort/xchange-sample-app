package com.xchange.sample.dao.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"_pair", "_limit"})
})
public class Alert extends AbstractPersistable<Long> {

    @Column(name="_pair")
    private String pair;

    @Column(name="_limit")
    private Long limit;

    @Column(name="_timestamp")
    private Instant timestamp;

    public Alert(){
        this(null);
    }

    public Alert(String pair, Long limit) {
        this.pair = pair;
        this.limit = limit;
        this.timestamp = Instant.now();
    }

    public Alert(Long id){
        this.setId(id);
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
        if (!super.equals(o)) return false;
        Alert alert = (Alert) o;
        return Objects.equals(pair, alert.pair) &&
                Objects.equals(limit, alert.limit);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), pair, limit);
    }
}
