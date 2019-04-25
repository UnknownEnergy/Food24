package com.crypto.daniel.service.mapper;

import com.crypto.daniel.domain.*;
import com.crypto.daniel.service.dto.GroceryListDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GroceryList and its DTO GroceryListDTO.
 */
@Mapper(componentModel = "spring", uses = {StoreItemMapper.class, FamilyGroupMapper.class, FamilyMemberMapper.class})
public interface GroceryListMapper extends EntityMapper<GroceryListDTO, GroceryList> {

    @Mapping(source = "familyGroup.id", target = "familyGroupId")
    @Mapping(source = "familyGroup.name", target = "familyGroupName")
    @Mapping(source = "familyMember.id", target = "familyMemberId")
    GroceryListDTO toDto(GroceryList groceryList);

    @Mapping(source = "familyGroupId", target = "familyGroup")
    @Mapping(source = "familyMemberId", target = "familyMember")
    GroceryList toEntity(GroceryListDTO groceryListDTO);

    default GroceryList fromId(Long id) {
        if (id == null) {
            return null;
        }
        GroceryList groceryList = new GroceryList();
        groceryList.setId(id);
        return groceryList;
    }
}
