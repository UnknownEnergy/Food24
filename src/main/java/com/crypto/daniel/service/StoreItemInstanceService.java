package com.crypto.daniel.service;

import com.crypto.daniel.service.dto.StoreItemInstanceDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing StoreItemInstance.
 */
public interface StoreItemInstanceService {

    /**
     * Save a storeItemInstance.
     *
     * @param storeItemInstanceDTO the entity to save
     * @return the persisted entity
     */
    StoreItemInstanceDTO save(StoreItemInstanceDTO storeItemInstanceDTO);

    /**
     * Get all the storeItemInstances.
     *
     * @return the list of entities
     */
    List<StoreItemInstanceDTO> findAll();


    /**
     * Get the "id" storeItemInstance.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StoreItemInstanceDTO> findOne(Long id);

    /**
     * Delete the "id" storeItemInstance.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the storeItemInstance corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<StoreItemInstanceDTO> search(String query);
}
