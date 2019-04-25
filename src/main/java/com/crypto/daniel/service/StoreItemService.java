package com.crypto.daniel.service;

import com.crypto.daniel.service.dto.StoreItemDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing StoreItem.
 */
public interface StoreItemService {

    /**
     * Save a storeItem.
     *
     * @param storeItemDTO the entity to save
     * @return the persisted entity
     */
    StoreItemDTO save(StoreItemDTO storeItemDTO);

    /**
     * Get all the storeItems.
     *
     * @return the list of entities
     */
    List<StoreItemDTO> findAll();


    /**
     * Get the "id" storeItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StoreItemDTO> findOne(Long id);

    /**
     * Delete the "id" storeItem.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the storeItem corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<StoreItemDTO> search(String query);
}
