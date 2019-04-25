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

    private String storeItemName;

    private Long storeId;

    private String storeName;

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

    public String getStoreItemName() {
        return storeItemName;
    }

    public void setStoreItemName(String storeItemName) {
        this.storeItemName = storeItemName;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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
            ", storeItem='" + getStoreItemName() + "'" +
            ", store=" + getStoreId() +
            ", store='" + getStoreName() + "'" +
            "}";
    }
}
