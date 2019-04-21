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
 * A FamilyGroup.
 */
@Entity
@Table(name = "family_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "familygroup")
public class FamilyGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "familyGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GroceryList> groceryLists = new HashSet<>();
    @ManyToMany(mappedBy = "familyGroups")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<FamilyMember> familyMembers = new HashSet<>();

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

    public FamilyGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<GroceryList> getGroceryLists() {
        return groceryLists;
    }

    public FamilyGroup groceryLists(Set<GroceryList> groceryLists) {
        this.groceryLists = groceryLists;
        return this;
    }

    public FamilyGroup addGroceryList(GroceryList groceryList) {
        this.groceryLists.add(groceryList);
        groceryList.setFamilyGroup(this);
        return this;
    }

    public FamilyGroup removeGroceryList(GroceryList groceryList) {
        this.groceryLists.remove(groceryList);
        groceryList.setFamilyGroup(null);
        return this;
    }

    public void setGroceryLists(Set<GroceryList> groceryLists) {
        this.groceryLists = groceryLists;
    }

    public Set<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public FamilyGroup familyMembers(Set<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
        return this;
    }

    public FamilyGroup addFamilyMember(FamilyMember familyMember) {
        this.familyMembers.add(familyMember);
        familyMember.getFamilyGroups().add(this);
        return this;
    }

    public FamilyGroup removeFamilyMember(FamilyMember familyMember) {
        this.familyMembers.remove(familyMember);
        familyMember.getFamilyGroups().remove(this);
        return this;
    }

    public void setFamilyMembers(Set<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
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
        FamilyGroup familyGroup = (FamilyGroup) o;
        if (familyGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), familyGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FamilyGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
