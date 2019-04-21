package com.crypto.daniel.web.rest;
import com.crypto.daniel.service.FamilyMemberService;
import com.crypto.daniel.web.rest.errors.BadRequestAlertException;
import com.crypto.daniel.web.rest.util.HeaderUtil;
import com.crypto.daniel.service.dto.FamilyMemberDTO;
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
 * REST controller for managing FamilyMember.
 */
@RestController
@RequestMapping("/api")
public class FamilyMemberResource {

    private final Logger log = LoggerFactory.getLogger(FamilyMemberResource.class);

    private static final String ENTITY_NAME = "familyMember";

    private final FamilyMemberService familyMemberService;

    public FamilyMemberResource(FamilyMemberService familyMemberService) {
        this.familyMemberService = familyMemberService;
    }

    /**
     * POST  /family-members : Create a new familyMember.
     *
     * @param familyMemberDTO the familyMemberDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new familyMemberDTO, or with status 400 (Bad Request) if the familyMember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/family-members")
    public ResponseEntity<FamilyMemberDTO> createFamilyMember(@Valid @RequestBody FamilyMemberDTO familyMemberDTO) throws URISyntaxException {
        log.debug("REST request to save FamilyMember : {}", familyMemberDTO);
        if (familyMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new familyMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FamilyMemberDTO result = familyMemberService.save(familyMemberDTO);
        return ResponseEntity.created(new URI("/api/family-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /family-members : Updates an existing familyMember.
     *
     * @param familyMemberDTO the familyMemberDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated familyMemberDTO,
     * or with status 400 (Bad Request) if the familyMemberDTO is not valid,
     * or with status 500 (Internal Server Error) if the familyMemberDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/family-members")
    public ResponseEntity<FamilyMemberDTO> updateFamilyMember(@Valid @RequestBody FamilyMemberDTO familyMemberDTO) throws URISyntaxException {
        log.debug("REST request to update FamilyMember : {}", familyMemberDTO);
        if (familyMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FamilyMemberDTO result = familyMemberService.save(familyMemberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, familyMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /family-members : get all the familyMembers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of familyMembers in body
     */
    @GetMapping("/family-members")
    public List<FamilyMemberDTO> getAllFamilyMembers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all FamilyMembers");
        return familyMemberService.findAll();
    }

    /**
     * GET  /family-members/:id : get the "id" familyMember.
     *
     * @param id the id of the familyMemberDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the familyMemberDTO, or with status 404 (Not Found)
     */
    @GetMapping("/family-members/{id}")
    public ResponseEntity<FamilyMemberDTO> getFamilyMember(@PathVariable Long id) {
        log.debug("REST request to get FamilyMember : {}", id);
        Optional<FamilyMemberDTO> familyMemberDTO = familyMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(familyMemberDTO);
    }

    /**
     * DELETE  /family-members/:id : delete the "id" familyMember.
     *
     * @param id the id of the familyMemberDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/family-members/{id}")
    public ResponseEntity<Void> deleteFamilyMember(@PathVariable Long id) {
        log.debug("REST request to delete FamilyMember : {}", id);
        familyMemberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/family-members?query=:query : search for the familyMember corresponding
     * to the query.
     *
     * @param query the query of the familyMember search
     * @return the result of the search
     */
    @GetMapping("/_search/family-members")
    public List<FamilyMemberDTO> searchFamilyMembers(@RequestParam String query) {
        log.debug("REST request to search FamilyMembers for query {}", query);
        return familyMemberService.search(query);
    }

}
