package com.crypto.daniel.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A StoreItem.
 */
@Entity
@Table(name = "store_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "storeitem")
public class StoreItem implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "storeItems")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<GroceryList> groceryLists = new HashSet<>();

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

    public StoreItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GroceryList> getGroceryLists() {
        return groceryLists;
    }

    public StoreItem groceryLists(Set<GroceryList> groceryLists) {
        this.groceryLists = groceryLists;
        return this;
    }

    public StoreItem addGroceryList(GroceryList groceryList) {
        this.groceryLists.add(groceryList);
        groceryList.getStoreItems().add(this);
        return this;
    }

    public StoreItem removeGroceryList(GroceryList groceryList) {
        this.groceryLists.remove(groceryList);
        groceryList.getStoreItems().remove(this);
        return this;
    }

    public void setGroceryLists(Set<GroceryList> groceryLists) {
        this.groceryLists = groceryLists;
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
        StoreItem storeItem = (StoreItem) o;
        if (storeItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), storeItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StoreItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
