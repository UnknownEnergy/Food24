package com.crypto.daniel.service.impl;

import com.crypto.daniel.service.StoreItemService;
import com.crypto.daniel.domain.StoreItem;
import com.crypto.daniel.repository.StoreItemRepository;
import com.crypto.daniel.repository.search.StoreItemSearchRepository;
import com.crypto.daniel.service.dto.StoreItemDTO;
import com.crypto.daniel.service.mapper.StoreItemMapper;
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
 * Service Implementation for managing StoreItem.
 */
@Service
@Transactional
public class StoreItemServiceImpl implements StoreItemService {

    private final Logger log = LoggerFactory.getLogger(StoreItemServiceImpl.class);

    private final StoreItemRepository storeItemRepository;

    private final StoreItemMapper storeItemMapper;

    private final StoreItemSearchRepository storeItemSearchRepository;

    public StoreItemServiceImpl(StoreItemRepository storeItemRepository, StoreItemMapper storeItemMapper, StoreItemSearchRepository storeItemSearchRepository) {
        this.storeItemRepository = storeItemRepository;
        this.storeItemMapper = storeItemMapper;
        this.storeItemSearchRepository = storeItemSearchRepository;
    }

    /**
     * Save a storeItem.
     *
     * @param storeItemDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StoreItemDTO save(StoreItemDTO storeItemDTO) {
        log.debug("Request to save StoreItem : {}", storeItemDTO);
        StoreItem storeItem = storeItemMapper.toEntity(storeItemDTO);
        storeItem = storeItemRepository.save(storeItem);
        StoreItemDTO result = storeItemMapper.toDto(storeItem);
        storeItemSearchRepository.save(storeItem);
        return result;
    }

    /**
     * Get all the storeItems.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreItemDTO> findAll() {
        log.debug("Request to get all StoreItems");
        return storeItemRepository.findAll().stream()
            .map(storeItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
     *  get all the storeItems where StoreItemInstance is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<StoreItemDTO> findAllWhereStoreItemInstanceIsNull() {
        log.debug("Request to get all storeItems where StoreItemInstance is null");
        return StreamSupport
            .stream(storeItemRepository.findAll().spliterator(), false)
            .filter(storeItem -> storeItem.getStoreItemInstance() == null)
            .map(storeItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one storeItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StoreItemDTO> findOne(Long id) {
        log.debug("Request to get StoreItem : {}", id);
        return storeItemRepository.findById(id)
            .map(storeItemMapper::toDto);
    }

    /**
     * Delete the storeItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StoreItem : {}", id);
        storeItemRepository.deleteById(id);
        storeItemSearchRepository.deleteById(id);
    }

    /**
     * Search for the storeItem corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreItemDTO> search(String query) {
        log.debug("Request to search StoreItems for query {}", query);
        return StreamSupport
            .stream(storeItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(storeItemMapper::toDto)
            .collect(Collectors.toList());
    }
}
