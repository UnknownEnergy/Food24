package com.crypto.daniel.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the StoreItemInstance entity.
 */
public class StoreItemInstanceDTO implements Serializable {

    private Long id;

    @NotNull
    private Double price;


    private Long storeItemId;

    private Long storeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getStoreItemId() {
        return storeItemId;
    }

    public void setStoreItemId(Long storeItemId) {
        this.storeItemId = storeItemId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StoreItemInstanceDTO storeItemInstanceDTO = (StoreItemInstanceDTO) o;
        if (storeItemInstanceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), storeItemInstanceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StoreItemInstanceDTO{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", storeItem=" + getStoreItemId() +
            ", store=" + getStoreId() +
            "}";
    }
}
