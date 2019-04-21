package com.crypto.daniel.service.mapper;

import com.crypto.daniel.domain.*;
import com.crypto.daniel.service.dto.FamilyMemberDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FamilyMember and its DTO FamilyMemberDTO.
 */
@Mapper(componentModel = "spring", uses = {FamilyGroupMapper.class, LocationMapper.class})
public interface FamilyMemberMapper extends EntityMapper<FamilyMemberDTO, FamilyMember> {

    @Mapping(source = "location.id", target = "locationId")
    FamilyMemberDTO toDto(FamilyMember familyMember);

    @Mapping(target = "groceryLists", ignore = true)
    @Mapping(source = "locationId", target = "location")
    FamilyMember toEntity(FamilyMemberDTO familyMemberDTO);

    default FamilyMember fromId(Long id) {
        if (id == null) {
            return null;
        }
        FamilyMember familyMember = new FamilyMember();
        familyMember.setId(id);
        return familyMember;
    }
}
