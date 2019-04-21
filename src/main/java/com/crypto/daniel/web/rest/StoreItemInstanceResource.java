package com.crypto.daniel.web.rest;
import com.crypto.daniel.service.StoreItemInstanceService;
import com.crypto.daniel.web.rest.errors.BadRequestAlertException;
import com.crypto.daniel.web.rest.util.HeaderUtil;
import com.crypto.daniel.service.dto.StoreItemInstanceDTO;
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
 * REST controller for managing StoreItemInstance.
 */
@RestController
@RequestMapping("/api")
public class StoreItemInstanceResource {

    private final Logger log = LoggerFactory.getLogger(StoreItemInstanceResource.class);

    private static final String ENTITY_NAME = "storeItemInstance";

    private final StoreItemInstanceService storeItemInstanceService;

    public StoreItemInstanceResource(StoreItemInstanceService storeItemInstanceService) {
        this.storeItemInstanceService = storeItemInstanceService;
    }

    /**
     * POST  /store-item-instances : Create a new storeItemInstance.
     *
     * @param storeItemInstanceDTO the storeItemInstanceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storeItemInstanceDTO, or with status 400 (Bad Request) if the storeItemInstance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/store-item-instances")
    public ResponseEntity<StoreItemInstanceDTO> createStoreItemInstance(@Valid @RequestBody StoreItemInstanceDTO storeItemInstanceDTO) throws URISyntaxException {
        log.debug("REST request to save StoreItemInstance : {}", storeItemInstanceDTO);
        if (storeItemInstanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new storeItemInstance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoreItemInstanceDTO result = storeItemInstanceService.save(storeItemInstanceDTO);
        return ResponseEntity.created(new URI("/api/store-item-instances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /store-item-instances : Updates an existing storeItemInstance.
     *
     * @param storeItemInstanceDTO the storeItemInstanceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storeItemInstanceDTO,
     * or with status 400 (Bad Request) if the storeItemInstanceDTO is not valid,
     * or with status 500 (Internal Server Error) if the storeItemInstanceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/store-item-instances")
    public ResponseEntity<StoreItemInstanceDTO> updateStoreItemInstance(@Valid @RequestBody StoreItemInstanceDTO storeItemInstanceDTO) throws URISyntaxException {
        log.debug("REST request to update StoreItemInstance : {}", storeItemInstanceDTO);
        if (storeItemInstanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StoreItemInstanceDTO result = storeItemInstanceService.save(storeItemInstanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storeItemInstanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /store-item-instances : get all the storeItemInstances.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of storeItemInstances in body
     */
    @GetMapping("/store-item-instances")
    public List<StoreItemInstanceDTO> getAllStoreItemInstances() {
        log.debug("REST request to get all StoreItemInstances");
        return storeItemInstanceService.findAll();
    }

    /**
     * GET  /store-item-instances/:id : get the "id" storeItemInstance.
     *
     * @param id the id of the storeItemInstanceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storeItemInstanceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/store-item-instances/{id}")
    public ResponseEntity<StoreItemInstanceDTO> getStoreItemInstance(@PathVariable Long id) {
        log.debug("REST request to get StoreItemInstance : {}", id);
        Optional<StoreItemInstanceDTO> storeItemInstanceDTO = storeItemInstanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storeItemInstanceDTO);
    }

    /**
     * DELETE  /store-item-instances/:id : delete the "id" storeItemInstance.
     *
     * @param id the id of the storeItemInstanceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/store-item-instances/{id}")
    public ResponseEntity<Void> deleteStoreItemInstance(@PathVariable Long id) {
        log.debug("REST request to delete StoreItemInstance : {}", id);
        storeItemInstanceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/store-item-instances?query=:query : search for the storeItemInstance corresponding
     * to the query.
     *
     * @param query the query of the storeItemInstance search
     * @return the result of the search
     */
    @GetMapping("/_search/store-item-instances")
    public List<StoreItemInstanceDTO> searchStoreItemInstances(@RequestParam String query) {
        log.debug("REST request to search StoreItemInstances for query {}", query);
        return storeItemInstanceService.search(query);
    }

}
