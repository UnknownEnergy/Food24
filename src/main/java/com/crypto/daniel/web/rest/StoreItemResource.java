package com.crypto.daniel.web.rest;
import com.crypto.daniel.service.StoreItemService;
import com.crypto.daniel.web.rest.errors.BadRequestAlertException;
import com.crypto.daniel.web.rest.util.HeaderUtil;
import com.crypto.daniel.service.dto.StoreItemDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing StoreItem.
 */
@RestController
@RequestMapping("/api")
public class StoreItemResource {

    private final Logger log = LoggerFactory.getLogger(StoreItemResource.class);

    private static final String ENTITY_NAME = "storeItem";

    private final StoreItemService storeItemService;

    public StoreItemResource(StoreItemService storeItemService) {
        this.storeItemService = storeItemService;
    }

    /**
     * POST  /store-items : Create a new storeItem.
     *
     * @param storeItemDTO the storeItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storeItemDTO, or with status 400 (Bad Request) if the storeItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/store-items")
    public ResponseEntity<StoreItemDTO> createStoreItem(@Valid @RequestBody StoreItemDTO storeItemDTO) throws URISyntaxException {
        log.debug("REST request to save StoreItem : {}", storeItemDTO);
        if (storeItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new storeItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoreItemDTO result = storeItemService.save(storeItemDTO);
        return ResponseEntity.created(new URI("/api/store-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /store-items : Updates an existing storeItem.
     *
     * @param storeItemDTO the storeItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storeItemDTO,
     * or with status 400 (Bad Request) if the storeItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the storeItemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/store-items")
    public ResponseEntity<StoreItemDTO> updateStoreItem(@Valid @RequestBody StoreItemDTO storeItemDTO) throws URISyntaxException {
        log.debug("REST request to update StoreItem : {}", storeItemDTO);
        if (storeItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StoreItemDTO result = storeItemService.save(storeItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storeItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /store-items : get all the storeItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of storeItems in body
     */
    @GetMapping("/store-items")
    public List<StoreItemDTO> getAllStoreItems() {
        log.debug("REST request to get all StoreItems");
        return storeItemService.findAll();
    }

    /**
     * GET  /store-items/:id : get the "id" storeItem.
     *
     * @param id the id of the storeItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storeItemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/store-items/{id}")
    public ResponseEntity<StoreItemDTO> getStoreItem(@PathVariable Long id) {
        log.debug("REST request to get StoreItem : {}", id);
        Optional<StoreItemDTO> storeItemDTO = storeItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storeItemDTO);
    }

    /**
     * DELETE  /store-items/:id : delete the "id" storeItem.
     *
     * @param id the id of the storeItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/store-items/{id}")
    public ResponseEntity<Void> deleteStoreItem(@PathVariable Long id) {
        log.debug("REST request to delete StoreItem : {}", id);
        storeItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/store-items?query=:query : search for the storeItem corresponding
     * to the query.
     *
     * @param query the query of the storeItem search
     * @return the result of the search
     */
    @GetMapping("/_search/store-items")
    public List<StoreItemDTO> searchStoreItems(@RequestParam String query) {
        log.debug("REST request to search StoreItems for query {}", query);
        return storeItemService.search(query);
    }

}
