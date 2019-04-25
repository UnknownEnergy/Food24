package com.crypto.daniel.service.mapper;

import com.crypto.daniel.domain.*;
import com.crypto.daniel.service.dto.StoreItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StoreItem and its DTO StoreItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StoreItemMapper extends EntityMapper<StoreItemDTO, StoreItem> {


    @Mapping(target = "groceryLists", ignore = true)
    StoreItem toEntity(StoreItemDTO storeItemDTO);

    default StoreItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        StoreItem storeItem = new StoreItem();
        storeItem.setId(id);
        return storeItem;
    }
}
