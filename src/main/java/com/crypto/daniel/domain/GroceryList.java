package com.crypto.daniel.domain;


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
 * A GroceryList.
 */
@Entity
@Table(name = "grocery_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "grocerylist")
public class GroceryList implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "grocery_list_store_item",
               joinColumns = @JoinColumn(name = "grocery_list_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "store_item_id", referencedColumnName = "id"))
    private Set<StoreItem> storeItems = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("groceryLists")
    private FamilyMember familyMember;

    @ManyToOne
    @JsonIgnoreProperties("groceryLists")
    private FamilyGroup familyGroup;

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

    public GroceryList name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<StoreItem> getStoreItems() {
        return storeItems;
    }

    public GroceryList storeItems(Set<StoreItem> storeItems) {
        this.storeItems = storeItems;
        return this;
    }

    public GroceryList addStoreItem(StoreItem storeItem) {
        this.storeItems.add(storeItem);
        storeItem.getGroceryLists().add(this);
        return this;
    }

    public GroceryList removeStoreItem(StoreItem storeItem) {
        this.storeItems.remove(storeItem);
        storeItem.getGroceryLists().remove(this);
        return this;
    }

    public void setStoreItems(Set<StoreItem> storeItems) {
        this.storeItems = storeItems;
    }

    public FamilyMember getFamilyMember() {
        return familyMember;
    }

    public GroceryList familyMember(FamilyMember familyMember) {
        this.familyMember = familyMember;
        return this;
    }

    public void setFamilyMember(FamilyMember familyMember) {
        this.familyMember = familyMember;
    }

    public FamilyGroup getFamilyGroup() {
        return familyGroup;
    }

    public GroceryList familyGroup(FamilyGroup familyGroup) {
        this.familyGroup = familyGroup;
        return this;
    }

    public void setFamilyGroup(FamilyGroup familyGroup) {
        this.familyGroup = familyGroup;
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
        GroceryList groceryList = (GroceryList) o;
        if (groceryList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groceryList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GroceryList{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
