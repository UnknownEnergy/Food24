package com.crypto.daniel.service.mapper;

import com.crypto.daniel.domain.*;
import com.crypto.daniel.service.dto.FamilyGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FamilyGroup and its DTO FamilyGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FamilyGroupMapper extends EntityMapper<FamilyGroupDTO, FamilyGroup> {


    @Mapping(target = "groceryLists", ignore = true)
    @Mapping(target = "familyMembers", ignore = true)
    FamilyGroup toEntity(FamilyGroupDTO familyGroupDTO);

    default FamilyGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        FamilyGroup familyGroup = new FamilyGroup();
        familyGroup.setId(id);
        return familyGroup;
    }
}
