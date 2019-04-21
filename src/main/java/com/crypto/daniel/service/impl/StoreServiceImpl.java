package com.crypto.daniel.service.impl;

import com.crypto.daniel.service.StoreService;
import com.crypto.daniel.domain.Store;
import com.crypto.daniel.repository.StoreRepository;
import com.crypto.daniel.repository.search.StoreSearchRepository;
import com.crypto.daniel.service.dto.StoreDTO;
import com.crypto.daniel.service.mapper.StoreMapper;
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
 * Service Implementation for managing Store.
 */
@Service
@Transactional
public class StoreServiceImpl implements StoreService {

    private final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;

    private final StoreSearchRepository storeSearchRepository;

    public StoreServiceImpl(StoreRepository storeRepository, StoreMapper storeMapper, StoreSearchRepository storeSearchRepository) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
        this.storeSearchRepository = storeSearchRepository;
    }

    /**
     * Save a store.
     *
     * @param storeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StoreDTO save(StoreDTO storeDTO) {
        log.debug("Request to save Store : {}", storeDTO);
        Store store = storeMapper.toEntity(storeDTO);
        store = storeRepository.save(store);
        StoreDTO result = storeMapper.toDto(store);
        storeSearchRepository.save(store);
        return result;
    }

    /**
     * Get all the stores.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreDTO> findAll() {
        log.debug("Request to get all Stores");
        return storeRepository.findAll().stream()
            .map(storeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one store by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StoreDTO> findOne(Long id) {
        log.debug("Request to get Store : {}", id);
        return storeRepository.findById(id)
            .map(storeMapper::toDto);
    }

    /**
     * Delete the store by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Store : {}", id);
        storeRepository.deleteById(id);
        storeSearchRepository.deleteById(id);
    }

    /**
     * Search for the store corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreDTO> search(String query) {
        log.debug("Request to search Stores for query {}", query);
        return StreamSupport
            .stream(storeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(storeMapper::toDto)
            .collect(Collectors.toList());
    }
}
