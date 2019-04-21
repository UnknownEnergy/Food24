package com.crypto.daniel.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StoreItemInstance.
 */
@Entity
@Table(name = "store_item_instance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "storeiteminstance")
public class StoreItemInstance implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @OneToOne
    @JoinColumn(unique = true)
    private StoreItem storeItem;

    @ManyToOne
    @JsonIgnoreProperties("storeItemInstances")
    private Store store;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public StoreItemInstance price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public StoreItem getStoreItem() {
        return storeItem;
    }

    public StoreItemInstance storeItem(StoreItem storeItem) {
        this.storeItem = storeItem;
        return this;
    }

    public void setStoreItem(StoreItem storeItem) {
        this.storeItem = storeItem;
    }

    public Store getStore() {
        return store;
    }

    public StoreItemInstance store(Store store) {
        this.store = store;
        return this;
    }

    public void setStore(Store store) {
        this.store = store;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StoreItemInstance storeItemInstance = (StoreItemInstance) o;
        if (storeItemInstance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), storeItemInstance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StoreItemInstance{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            "}";
    }
}
