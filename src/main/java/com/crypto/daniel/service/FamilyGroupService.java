package com.crypto.daniel.service;

import com.crypto.daniel.service.dto.FamilyGroupDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing FamilyGroup.
 */
public interface FamilyGroupService {

    /**
     * Save a familyGroup.
     *
     * @param familyGroupDTO the entity to save
     * @return the persisted entity
     */
    FamilyGroupDTO save(FamilyGroupDTO familyGroupDTO);

    /**
     * Get all the familyGroups.
     *
     * @return the list of entities
     */
    List<FamilyGroupDTO> findAll();


    /**
     * Get the "id" familyGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FamilyGroupDTO> findOne(Long id);

    /**
     * Delete the "id" familyGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the familyGroup corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<FamilyGroupDTO> search(String query);
}
