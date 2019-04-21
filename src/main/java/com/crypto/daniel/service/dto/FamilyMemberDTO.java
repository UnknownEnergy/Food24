package com.crypto.daniel.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the FamilyMember entity.
 */
public class FamilyMemberDTO implements Serializable {

    private Long id;

    @NotNull
    private String userName;

    private String firstName;

    private String lastName;


    private Set<FamilyGroupDTO> familyGroups = new HashSet<>();

    private Long locationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<FamilyGroupDTO> getFamilyGroups() {
        return familyGroups;
    }

    public void setFamilyGroups(Set<FamilyGroupDTO> familyGroups) {
        this.familyGroups = familyGroups;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FamilyMemberDTO familyMemberDTO = (FamilyMemberDTO) o;
        if (familyMemberDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), familyMemberDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FamilyMemberDTO{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", location=" + getLocationId() +
            "}";
    }
}
