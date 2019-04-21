package com.crypto.daniel.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the GroceryList entity.
 */
public class GroceryListDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;


    private Set<StoreItemDTO> storeItems = new HashSet<>();

    private Long familyMemberId;

    private Long familyGroupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<StoreItemDTO> getStoreItems() {
        return storeItems;
    }

    public void setStoreItems(Set<StoreItemDTO> storeItems) {
        this.storeItems = storeItems;
    }

    public Long getFamilyMemberId() {
        return familyMemberId;
    }

    public void setFamilyMemberId(Long familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    public Long getFamilyGroupId() {
        return familyGroupId;
    }

    public void setFamilyGroupId(Long familyGroupId) {
        this.familyGroupId = familyGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GroceryListDTO groceryListDTO = (GroceryListDTO) o;
        if (groceryListDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), groceryListDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GroceryListDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", familyMember=" + getFamilyMemberId() +
            ", familyGroup=" + getFamilyGroupId() +
            "}";
    }
}
