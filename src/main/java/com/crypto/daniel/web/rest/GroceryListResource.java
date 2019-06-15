package com.crypto.daniel.web.rest;

import com.crypto.daniel.domain.User;
import com.crypto.daniel.service.FamilyMemberService;
import com.crypto.daniel.service.GroceryListService;
import com.crypto.daniel.service.UserService;
import com.crypto.daniel.service.dto.GroceryListDTO;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing GroceryList.
 */
@RestController
@RequestMapping("/api")
public class GroceryListResource {

    private final Logger log = LoggerFactory.getLogger(GroceryListResource.class);

    private static final String ENTITY_NAME = "groceryList";

    private final GroceryListService groceryListService;

    private final UserService userService;

    private final FamilyMemberService familyMemberService;

    public GroceryListResource(GroceryListService groceryListService, UserService userService, FamilyMemberService familyMemberService) {
        this.groceryListService = groceryListService;
        this.userService = userService;
        this.familyMemberService = familyMemberService;
    }

    /**
     * POST  /grocery-lists : Create a new groceryList.
     *
     * @param groceryListDTO the groceryListDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groceryListDTO, or with status 400 (Bad Request) if the groceryList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/grocery-lists")
    public ResponseEntity<GroceryListDTO> createGroceryList(@Valid @RequestBody GroceryListDTO groceryListDTO) throws URISyntaxException {
        log.debug("REST request to save GroceryList : {}", groceryListDTO);
        if (groceryListDTO.getId() != null) {
            throw new BadRequestAlertException("A new groceryList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GroceryListDTO result = groceryListService.save(groceryListDTO);
        return ResponseEntity.created(new URI("/api/grocery-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /grocery-lists : Updates an existing groceryList.
     *
     * @param groceryListDTO the groceryListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groceryListDTO,
     * or with status 400 (Bad Request) if the groceryListDTO is not valid,
     * or with status 500 (Internal Server Error) if the groceryListDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/grocery-lists")
    public ResponseEntity<GroceryListDTO> updateGroceryList(@Valid @RequestBody GroceryListDTO groceryListDTO) throws URISyntaxException {
        log.debug("REST request to update GroceryList : {}", groceryListDTO);
        if (groceryListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GroceryListDTO result = groceryListService.save(groceryListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, groceryListDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /grocery-lists : get all the groceryLists.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of groceryLists in body
     */
    @GetMapping("/grocery-lists")
    public List<GroceryListDTO> getAllGroceryLists(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all GroceryLists");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserWithAuthoritiesByLogin(username).orElse(null);

        if (username.equals("admin")) {
            return groceryListService.findAll();
        } else {
            return groceryListService.findAll().stream()
                .filter(groceryListDTO -> familyMemberService.findAll().stream()
                    .filter(familyMemberDTO -> familyMemberDTO.getUserId().equals(Objects.requireNonNull(user).getId()))
                    .anyMatch(familyMemberDTO -> familyMemberDTO.getFamilyGroups().stream()
                        .anyMatch(familyGroupDTO -> familyGroupDTO.getId().equals(groceryListDTO.getFamilyGroupId())))).collect(Collectors.toList());
        }
    }

    /**
     * GET  /grocery-lists/:id : get the "id" groceryList.
     *
     * @param id the id of the groceryListDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groceryListDTO, or with status 404 (Not Found)
     */
    @GetMapping("/grocery-lists/{id}")
    public ResponseEntity<GroceryListDTO> getGroceryList(@PathVariable Long id) {
        log.debug("REST request to get GroceryList : {}", id);
        Optional<GroceryListDTO> groceryListDTO = groceryListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(groceryListDTO);
    }

    /**
     * DELETE  /grocery-lists/:id : delete the "id" groceryList.
     *
     * @param id the id of the groceryListDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/grocery-lists/{id}")
    public ResponseEntity<Void> deleteGroceryList(@PathVariable Long id) {
        log.debug("REST request to delete GroceryList : {}", id);
        groceryListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/grocery-lists?query=:query : search for the groceryList corresponding
     * to the query.
     *
     * @param query the query of the groceryList search
     * @return the result of the search
     */
    @GetMapping("/_search/grocery-lists")
    public List<GroceryListDTO> searchGroceryLists(@RequestParam String query) {
        log.debug("REST request to search GroceryLists for query {}", query);
        return groceryListService.search(query);
    }

}
