package com.crypto.daniel.service.impl;

import com.crypto.daniel.service.StoreItemInstanceService;
import com.crypto.daniel.domain.StoreItemInstance;
import com.crypto.daniel.repository.StoreItemInstanceRepository;
import com.crypto.daniel.repository.search.StoreItemInstanceSearchRepository;
import com.crypto.daniel.service.dto.StoreItemInstanceDTO;
import com.crypto.daniel.service.mapper.StoreItemInstanceMapper;
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
 * Service Implementation for managing StoreItemInstance.
 */
@Service
@Transactional
public class StoreItemInstanceServiceImpl implements StoreItemInstanceService {

    private final Logger log = LoggerFactory.getLogger(StoreItemInstanceServiceImpl.class);

    private final StoreItemInstanceRepository storeItemInstanceRepository;

    private final StoreItemInstanceMapper storeItemInstanceMapper;

    private final StoreItemInstanceSearchRepository storeItemInstanceSearchRepository;

    public StoreItemInstanceServiceImpl(StoreItemInstanceRepository storeItemInstanceRepository, StoreItemInstanceMapper storeItemInstanceMapper, StoreItemInstanceSearchRepository storeItemInstanceSearchRepository) {
        this.storeItemInstanceRepository = storeItemInstanceRepository;
        this.storeItemInstanceMapper = storeItemInstanceMapper;
        this.storeItemInstanceSearchRepository = storeItemInstanceSearchRepository;
    }

    /**
     * Save a storeItemInstance.
     *
     * @param storeItemInstanceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StoreItemInstanceDTO save(StoreItemInstanceDTO storeItemInstanceDTO) {
        log.debug("Request to save StoreItemInstance : {}", storeItemInstanceDTO);
        StoreItemInstance storeItemInstance = storeItemInstanceMapper.toEntity(storeItemInstanceDTO);
        storeItemInstance = storeItemInstanceRepository.save(storeItemInstance);
        StoreItemInstanceDTO result = storeItemInstanceMapper.toDto(storeItemInstance);
        storeItemInstanceSearchRepository.save(storeItemInstance);
        return result;
    }

    /**
     * Get all the storeItemInstances.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreItemInstanceDTO> findAll() {
        log.debug("Request to get all StoreItemInstances");
        return storeItemInstanceRepository.findAll().stream()
            .map(storeItemInstanceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one storeItemInstance by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StoreItemInstanceDTO> findOne(Long id) {
        log.debug("Request to get StoreItemInstance : {}", id);
        return storeItemInstanceRepository.findById(id)
            .map(storeItemInstanceMapper::toDto);
    }

    /**
     * Delete the storeItemInstance by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StoreItemInstance : {}", id);
        storeItemInstanceRepository.deleteById(id);
        storeItemInstanceSearchRepository.deleteById(id);
    }

    /**
     * Search for the storeItemInstance corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreItemInstanceDTO> search(String query) {
        log.debug("Request to search StoreItemInstances for query {}", query);
        return StreamSupport
            .stream(storeItemInstanceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(storeItemInstanceMapper::toDto)
            .collect(Collectors.toList());
    }
}
