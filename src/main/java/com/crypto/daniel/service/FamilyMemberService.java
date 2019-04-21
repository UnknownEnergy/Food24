package com.crypto.daniel.service;

import com.crypto.daniel.service.dto.FamilyMemberDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing FamilyMember.
 */
public interface FamilyMemberService {

    /**
     * Save a familyMember.
     *
     * @param familyMemberDTO the entity to save
     * @return the persisted entity
     */
    FamilyMemberDTO save(FamilyMemberDTO familyMemberDTO);

    /**
     * Get all the familyMembers.
     *
     * @return the list of entities
     */
    List<FamilyMemberDTO> findAll();

    /**
     * Get all the FamilyMember with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<FamilyMemberDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" familyMember.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FamilyMemberDTO> findOne(Long id);

    /**
     * Delete the "id" familyMember.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the familyMember corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<FamilyMemberDTO> search(String query);
}
