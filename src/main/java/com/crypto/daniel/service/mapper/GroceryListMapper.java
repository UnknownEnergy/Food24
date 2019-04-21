package com.crypto.daniel.service.mapper;

import com.crypto.daniel.domain.*;
import com.crypto.daniel.service.dto.GroceryListDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GroceryList and its DTO GroceryListDTO.
 */
@Mapper(componentModel = "spring", uses = {StoreItemMapper.class, FamilyMemberMapper.class, FamilyGroupMapper.class})
public interface GroceryListMapper extends EntityMapper<GroceryListDTO, GroceryList> {

    @Mapping(source = "familyMember.id", target = "familyMemberId")
    @Mapping(source = "familyGroup.id", target = "familyGroupId")
    GroceryListDTO toDto(GroceryList groceryList);

    @Mapping(source = "familyMemberId", target = "familyMember")
    @Mapping(source = "familyGroupId", target = "familyGroup")
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
