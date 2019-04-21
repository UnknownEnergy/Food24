package com.crypto.daniel.web.rest;
import com.crypto.daniel.service.FamilyGroupService;
import com.crypto.daniel.web.rest.errors.BadRequestAlertException;
import com.crypto.daniel.web.rest.util.HeaderUtil;
import com.crypto.daniel.service.dto.FamilyGroupDTO;
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
 * REST controller for managing FamilyGroup.
 */
@RestController
@RequestMapping("/api")
public class FamilyGroupResource {

    private final Logger log = LoggerFactory.getLogger(FamilyGroupResource.class);

    private static final String ENTITY_NAME = "familyGroup";

    private final FamilyGroupService familyGroupService;

    public FamilyGroupResource(FamilyGroupService familyGroupService) {
        this.familyGroupService = familyGroupService;
    }

    /**
     * POST  /family-groups : Create a new familyGroup.
     *
     * @param familyGroupDTO the familyGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new familyGroupDTO, or with status 400 (Bad Request) if the familyGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/family-groups")
    public ResponseEntity<FamilyGroupDTO> createFamilyGroup(@Valid @RequestBody FamilyGroupDTO familyGroupDTO) throws URISyntaxException {
        log.debug("REST request to save FamilyGroup : {}", familyGroupDTO);
        if (familyGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new familyGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FamilyGroupDTO result = familyGroupService.save(familyGroupDTO);
        return ResponseEntity.created(new URI("/api/family-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /family-groups : Updates an existing familyGroup.
     *
     * @param familyGroupDTO the familyGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated familyGroupDTO,
     * or with status 400 (Bad Request) if the familyGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the familyGroupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/family-groups")
    public ResponseEntity<FamilyGroupDTO> updateFamilyGroup(@Valid @RequestBody FamilyGroupDTO familyGroupDTO) throws URISyntaxException {
        log.debug("REST request to update FamilyGroup : {}", familyGroupDTO);
        if (familyGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FamilyGroupDTO result = familyGroupService.save(familyGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, familyGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /family-groups : get all the familyGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of familyGroups in body
     */
    @GetMapping("/family-groups")
    public List<FamilyGroupDTO> getAllFamilyGroups() {
        log.debug("REST request to get all FamilyGroups");
        return familyGroupService.findAll();
    }

    /**
     * GET  /family-groups/:id : get the "id" familyGroup.
     *
     * @param id the id of the familyGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the familyGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/family-groups/{id}")
    public ResponseEntity<FamilyGroupDTO> getFamilyGroup(@PathVariable Long id) {
        log.debug("REST request to get FamilyGroup : {}", id);
        Optional<FamilyGroupDTO> familyGroupDTO = familyGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(familyGroupDTO);
    }

    /**
     * DELETE  /family-groups/:id : delete the "id" familyGroup.
     *
     * @param id the id of the familyGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/family-groups/{id}")
    public ResponseEntity<Void> deleteFamilyGroup(@PathVariable Long id) {
        log.debug("REST request to delete FamilyGroup : {}", id);
        familyGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/family-groups?query=:query : search for the familyGroup corresponding
     * to the query.
     *
     * @param query the query of the familyGroup search
     * @return the result of the search
     */
    @GetMapping("/_search/family-groups")
    public List<FamilyGroupDTO> searchFamilyGroups(@RequestParam String query) {
        log.debug("REST request to search FamilyGroups for query {}", query);
        return familyGroupService.search(query);
    }

}
