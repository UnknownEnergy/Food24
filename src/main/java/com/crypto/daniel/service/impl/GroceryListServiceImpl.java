package com.crypto.daniel.service.impl;

import com.crypto.daniel.service.GroceryListService;
import com.crypto.daniel.domain.GroceryList;
import com.crypto.daniel.repository.GroceryListRepository;
import com.crypto.daniel.repository.search.GroceryListSearchRepository;
import com.crypto.daniel.service.dto.GroceryListDTO;
import com.crypto.daniel.service.mapper.GroceryListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing GroceryList.
 */
@Service
@Transactional
public class GroceryListServiceImpl implements GroceryListService {

    private final Logger log = LoggerFactory.getLogger(GroceryListServiceImpl.class);

    private final GroceryListRepository groceryListRepository;

    private final GroceryListMapper groceryListMapper;

    private final GroceryListSearchRepository groceryListSearchRepository;

    public GroceryListServiceImpl(GroceryListRepository groceryListRepository, GroceryListMapper groceryListMapper, GroceryListSearchRepository groceryListSearchRepository) {
        this.groceryListRepository = groceryListRepository;
        this.groceryListMapper = groceryListMapper;
        this.groceryListSearchRepository = groceryListSearchRepository;
    }

    /**
     * Save a groceryList.
     *
     * @param groceryListDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GroceryListDTO save(GroceryListDTO groceryListDTO) {
        log.debug("Request to save GroceryList : {}", groceryListDTO);
        GroceryList groceryList = groceryListMapper.toEntity(groceryListDTO);
        groceryList = groceryListRepository.save(groceryList);
        GroceryListDTO result = groceryListMapper.toDto(groceryList);
        groceryListSearchRepository.save(groceryList);
        return result;
    }

    /**
     * Get all the groceryLists.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GroceryListDTO> findAll() {
        log.debug("Request to get all GroceryLists");
        return groceryListRepository.findAllWithEagerRelationships().stream()
            .map(groceryListMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the GroceryList with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<GroceryListDTO> findAllWithEagerRelationships(Pageable pageable) {
        return groceryListRepository.findAllWithEagerRelationships(pageable).map(groceryListMapper::toDto);
    }
    

    /**
     * Get one groceryList by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GroceryListDTO> findOne(Long id) {
        log.debug("Request to get GroceryList : {}", id);
        return groceryListRepository.findOneWithEagerRelationships(id)
            .map(groceryListMapper::toDto);
    }

    /**
     * Delete the groceryList by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GroceryList : {}", id);
        groceryListRepository.deleteById(id);
        groceryListSearchRepository.deleteById(id);
    }

    /**
     * Search for the groceryList corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GroceryListDTO> search(String query) {
        log.debug("Request to search GroceryLists for query {}", query);
        return StreamSupport
            .stream(groceryListSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(groceryListMapper::toDto)
            .collect(Collectors.toList());
    }
}
