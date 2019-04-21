package com.crypto.daniel.service.impl;

import com.crypto.daniel.service.FamilyGroupService;
import com.crypto.daniel.domain.FamilyGroup;
import com.crypto.daniel.repository.FamilyGroupRepository;
import com.crypto.daniel.repository.search.FamilyGroupSearchRepository;
import com.crypto.daniel.service.dto.FamilyGroupDTO;
import com.crypto.daniel.service.mapper.FamilyGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing FamilyGroup.
 */
@Service
@Transactional
public class FamilyGroupServiceImpl implements FamilyGroupService {

    private final Logger log = LoggerFactory.getLogger(FamilyGroupServiceImpl.class);

    private final FamilyGroupRepository familyGroupRepository;

    private final FamilyGroupMapper familyGroupMapper;

    private final FamilyGroupSearchRepository familyGroupSearchRepository;

    public FamilyGroupServiceImpl(FamilyGroupRepository familyGroupRepository, FamilyGroupMapper familyGroupMapper, FamilyGroupSearchRepository familyGroupSearchRepository) {
        this.familyGroupRepository = familyGroupRepository;
        this.familyGroupMapper = familyGroupMapper;
        this.familyGroupSearchRepository = familyGroupSearchRepository;
    }

    /**
     * Save a familyGroup.
     *
     * @param familyGroupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FamilyGroupDTO save(FamilyGroupDTO familyGroupDTO) {
        log.debug("Request to save FamilyGroup : {}", familyGroupDTO);
        FamilyGroup familyGroup = familyGroupMapper.toEntity(familyGroupDTO);
        familyGroup = familyGroupRepository.save(familyGroup);
        FamilyGroupDTO result = familyGroupMapper.toDto(familyGroup);
        familyGroupSearchRepository.save(familyGroup);
        return result;
    }

    /**
     * Get all the familyGroups.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FamilyGroupDTO> findAll() {
        log.debug("Request to get all FamilyGroups");
        return familyGroupRepository.findAll().stream()
            .map(familyGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one familyGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FamilyGroupDTO> findOne(Long id) {
        log.debug("Request to get FamilyGroup : {}", id);
        return familyGroupRepository.findById(id)
            .map(familyGroupMapper::toDto);
    }

    /**
     * Delete the familyGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FamilyGroup : {}", id);
        familyGroupRepository.deleteById(id);
        familyGroupSearchRepository.deleteById(id);
    }

    /**
     * Search for the familyGroup corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FamilyGroupDTO> search(String query) {
        log.debug("Request to search FamilyGroups for query {}", query);
        return StreamSupport
            .stream(familyGroupSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(familyGroupMapper::toDto)
            .collect(Collectors.toList());
    }
}
