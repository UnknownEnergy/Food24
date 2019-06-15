package com.crypto.daniel.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A FamilyMember.
 */
@Entity
@Table(name = "family_member")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "familymember")
public class FamilyMember implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToMany(mappedBy = "familyMember")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GroceryList> groceryLists = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("familyMembers")
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "family_member_family_group",
               joinColumns = @JoinColumn(name = "family_member_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "family_group_id", referencedColumnName = "id"))
    private Set<FamilyGroup> familyGroups = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("familyMembers")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<GroceryList> getGroceryLists() {
        return groceryLists;
    }

    public FamilyMember groceryLists(Set<GroceryList> groceryLists) {
        this.groceryLists = groceryLists;
        return this;
    }

    public FamilyMember addGroceryList(GroceryList groceryList) {
        this.groceryLists.add(groceryList);
        groceryList.setFamilyMember(this);
        return this;
    }

    public FamilyMember removeGroceryList(GroceryList groceryList) {
        this.groceryLists.remove(groceryList);
        groceryList.setFamilyMember(null);
        return this;
    }

    public void setGroceryLists(Set<GroceryList> groceryLists) {
        this.groceryLists = groceryLists;
    }

    public User getUser() {
        return user;
    }

    public FamilyMember user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<FamilyGroup> getFamilyGroups() {
        return familyGroups;
    }

    public FamilyMember familyGroups(Set<FamilyGroup> familyGroups) {
        this.familyGroups = familyGroups;
        return this;
    }

    public FamilyMember addFamilyGroup(FamilyGroup familyGroup) {
        this.familyGroups.add(familyGroup);
        return this;
    }

    public FamilyMember removeFamilyGroup(FamilyGroup familyGroup) {
        this.familyGroups.remove(familyGroup);
        return this;
    }

    public void setFamilyGroups(Set<FamilyGroup> familyGroups) {
        this.familyGroups = familyGroups;
    }

    public Location getLocation() {
        return location;
    }

    public FamilyMember location(Location location) {
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
        FamilyMember familyMember = (FamilyMember) o;
        if (familyMember.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), familyMember.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FamilyMember{" +
            "id=" + getId() +
            "}";
    }
}
