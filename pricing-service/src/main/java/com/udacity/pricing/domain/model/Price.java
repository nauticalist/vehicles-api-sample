package com.udacity.pricing.domain.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Represents the price of a given vehicle, including currency.
 */
@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String currency;

    private BigDecimal price;

    private Long vehicleId;

    public Price() {
    }

    public Price(String currency, BigDecimal price, Long vehicleId) {
        this.currency = currency;
        this.price = price;
        this.vehicleId = vehicleId;
    }

    public Price(Long id, String currency, BigDecimal price, Long vehicleId) {
        this.id = id;
        this.currency = currency;
        this.price = price;
        this.vehicleId = vehicleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Price)) return false;

        Price price1 = (Price) o;

        return new EqualsBuilder()
                .append(getId(), price1.getId())
                .append(getCurrency(), price1.getCurrency())
                .append(getPrice(), price1.getPrice())
                .append(getVehicleId(), price1.getVehicleId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .append(getCurrency())
                .append(getPrice())
                .append(getVehicleId())
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", currency='" + currency + '\'' +
                ", price=" + price +
                ", vehicleId=" + vehicleId +
                '}';
    }
}
