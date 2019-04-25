package com.crypto.daniel.service.dto;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the FamilyMember entity.
 */
public class FamilyMemberDTO implements Serializable {

    private Long id;


    private Long userId;

    private String userLogin;

    private Set<FamilyGroupDTO> familyGroups = new HashSet<>();

    private Long locationId;

    private String locationDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
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

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
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
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", location=" + getLocationId() +
            ", location='" + getLocationDescription() + "'" +
            "}";
    }
}
