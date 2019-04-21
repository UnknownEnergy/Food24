package com.crypto.daniel.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Store.
 */
@Entity
@Table(name = "store")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "store")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "store")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<StoreItemInstance> storeItemInstances = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("stores")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Store name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<StoreItemInstance> getStoreItemInstances() {
        return storeItemInstances;
    }

    public Store storeItemInstances(Set<StoreItemInstance> storeItemInstances) {
        this.storeItemInstances = storeItemInstances;
        return this;
    }

    public Store addStoreItemInstance(StoreItemInstance storeItemInstance) {
        this.storeItemInstances.add(storeItemInstance);
        storeItemInstance.setStore(this);
        return this;
    }

    public Store removeStoreItemInstance(StoreItemInstance storeItemInstance) {
        this.storeItemInstances.remove(storeItemInstance);
        storeItemInstance.setStore(null);
        return this;
    }

    public void setStoreItemInstances(Set<StoreItemInstance> storeItemInstances) {
        this.storeItemInstances = storeItemInstances;
    }

    public Location getLocation() {
        return location;
    }

    public Store location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
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
        Store store = (Store) o;
        if (store.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), store.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Store{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
