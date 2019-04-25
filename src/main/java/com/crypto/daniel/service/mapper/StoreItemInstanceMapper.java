package com.crypto.daniel.service.mapper;

import com.crypto.daniel.domain.*;
import com.crypto.daniel.service.dto.StoreItemInstanceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StoreItemInstance and its DTO StoreItemInstanceDTO.
 */
@Mapper(componentModel = "spring", uses = {StoreItemMapper.class, StoreMapper.class})
public interface StoreItemInstanceMapper extends EntityMapper<StoreItemInstanceDTO, StoreItemInstance> {

    @Mapping(source = "storeItem.id", target = "storeItemId")
    @Mapping(source = "storeItem.name", target = "storeItemName")
    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "store.name", target = "storeName")
    StoreItemInstanceDTO toDto(StoreItemInstance storeItemInstance);

    @Mapping(source = "storeItemId", target = "storeItem")
    @Mapping(source = "storeId", target = "store")
    StoreItemInstance toEntity(StoreItemInstanceDTO storeItemInstanceDTO);

    default StoreItemInstance fromId(Long id) {
        if (id == null) {
            return null;
        }
        StoreItemInstance storeItemInstance = new StoreItemInstance();
        storeItemInstance.setId(id);
        return storeItemInstance;
    }
}
