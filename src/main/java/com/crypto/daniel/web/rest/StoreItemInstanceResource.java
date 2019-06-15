package com.crypto.daniel.web.rest;

import com.crypto.daniel.domain.User;
import com.crypto.daniel.service.FamilyMemberService;
import com.crypto.daniel.service.GroceryListService;
import com.crypto.daniel.service.StoreItemInstanceService;
import com.crypto.daniel.service.UserService;
import com.crypto.daniel.service.dto.StoreItemInstanceDTO;
import com.crypto.daniel.web.rest.errors.BadRequestAlertException;
import com.crypto.daniel.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing StoreItemInstance.
 */
@RestController
@RequestMapping("/api")
public class StoreItemInstanceResource {

    private final Logger log = LoggerFactory.getLogger(StoreItemInstanceResource.class);

    private static final String ENTITY_NAME = "storeItemInstance";

    private final StoreItemInstanceService storeItemInstanceService;

    private final GroceryListService groceryListService;

    private final FamilyMemberService familyMemberService;

    private final UserService userService;

    public StoreItemInstanceResource(StoreItemInstanceService storeItemInstanceService, GroceryListService groceryListService, FamilyMemberService familyMemberService, UserService userService) {
        this.storeItemInstanceService = storeItemInstanceService;
        this.groceryListService = groceryListService;
        this.familyMemberService = familyMemberService;
        this.userService = userService;
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
        List<StoreItemInstanceDTO> storeItemInstanceServiceAll = storeItemInstanceService.findAll();
        List<StoreItemInstanceDTO> filteredResult = new ArrayList<>();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if(username.equals("admin")) {
            return storeItemInstanceServiceAll;
        } else {
            User user = userService.getUserWithAuthoritiesByLogin(username).orElse(null);
            storeItemInstanceServiceAll.forEach(storeItemInstanceDTO -> {
                if (groceryListService.findAll().stream()
                    .filter(groceryListDTO -> familyMemberService.findAll().stream()
                        .filter(familyMemberDTO -> familyMemberDTO.getUserId().equals(Objects.requireNonNull(user).getId()))
                        .anyMatch(familyMemberDTO -> familyMemberDTO.getFamilyGroups().stream()
                            .anyMatch(familyGroupDTO -> groceryListDTO.getFamilyGroupId().equals(familyGroupDTO.getId()))))
                    .flatMap(groceryListDTO -> groceryListDTO.getStoreItems().stream())
                    .anyMatch(storeItemDTO ->
                        storeItemDTO.getId().equals(storeItemInstanceDTO.getStoreItemId()))) {
                    filteredResult.add(storeItemInstanceDTO);
                }
            });
        }

        return filteredResult;
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
