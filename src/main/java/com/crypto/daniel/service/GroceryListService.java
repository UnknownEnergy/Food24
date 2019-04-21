package com.crypto.daniel.service;

import com.crypto.daniel.service.dto.GroceryListDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing GroceryList.
 */
public interface GroceryListService {

    /**
     * Save a groceryList.
     *
     * @param groceryListDTO the entity to save
     * @return the persisted entity
     */
    GroceryListDTO save(GroceryListDTO groceryListDTO);

    /**
     * Get all the groceryLists.
     *
     * @return the list of entities
     */
    List<GroceryListDTO> findAll();

    /**
     * Get all the GroceryList with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<GroceryListDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" groceryList.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GroceryListDTO> findOne(Long id);

    /**
     * Delete the "id" groceryList.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the groceryList corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<GroceryListDTO> search(String query);
}
