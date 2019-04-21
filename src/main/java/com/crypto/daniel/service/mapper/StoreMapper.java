package com.crypto.daniel.service.mapper;

import com.crypto.daniel.domain.*;
import com.crypto.daniel.service.dto.StoreDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Store and its DTO StoreDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface StoreMapper extends EntityMapper<StoreDTO, Store> {

    @Mapping(source = "location.id", target = "locationId")
    StoreDTO toDto(Store store);

    @Mapping(target = "storeItemInstances", ignore = true)
    @Mapping(source = "locationId", target = "location")
    Store toEntity(StoreDTO storeDTO);

    default Store fromId(Long id) {
        if (id == null) {
            return null;
        }
        Store store = new Store();
        store.setId(id);
        return store;
    }
}
