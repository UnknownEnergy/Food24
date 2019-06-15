package com.crypto.daniel.web.rest;

import com.crypto.daniel.Food24App;

import com.crypto.daniel.domain.GroceryList;
import com.crypto.daniel.repository.GroceryListRepository;
import com.crypto.daniel.repository.search.GroceryListSearchRepository;
import com.crypto.daniel.service.FamilyMemberService;
import com.crypto.daniel.service.GroceryListService;
import com.crypto.daniel.service.UserService;
import com.crypto.daniel.service.dto.GroceryListDTO;
import com.crypto.daniel.service.mapper.GroceryListMapper;
import com.crypto.daniel.web.rest.errors.ExceptionTranslator;

import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.crypto.daniel.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GroceryListResource REST controller.
 *
 * @see GroceryListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Food24App.class)
public class GroceryListResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private GroceryListRepository groceryListRepository;

    @Mock
    private GroceryListRepository groceryListRepositoryMock;

    @Autowired
    private GroceryListMapper groceryListMapper;

    @Mock
    private GroceryListService groceryListServiceMock;

    @Autowired
    private GroceryListService groceryListService;

    /**
     * This repository is mocked in the com.crypto.daniel.repository.search test package.
     *
     * @see com.crypto.daniel.repository.search.GroceryListSearchRepositoryMockConfiguration
     */
    @Autowired
    private GroceryListSearchRepository mockGroceryListSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restGroceryListMockMvc;

    private GroceryList groceryList;

    @Autowired
    private UserService userService;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GroceryListResource groceryListResource = new GroceryListResource(groceryListService, userService, familyMemberService);
        this.restGroceryListMockMvc = MockMvcBuilders.standaloneSetup(groceryListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroceryList createEntity(EntityManager em) {
        GroceryList groceryList = new GroceryList()
            .name(DEFAULT_NAME);
        return groceryList;
    }

    @Before
    public void initTest() {
        groceryList = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroceryList() throws Exception {
        int databaseSizeBeforeCreate = groceryListRepository.findAll().size();

        // Create the GroceryList
        GroceryListDTO groceryListDTO = groceryListMapper.toDto(groceryList);
        restGroceryListMockMvc.perform(post("/api/grocery-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groceryListDTO)))
            .andExpect(status().isCreated());

        // Validate the GroceryList in the database
        List<GroceryList> groceryListList = groceryListRepository.findAll();
        assertThat(groceryListList).hasSize(databaseSizeBeforeCreate + 1);
        GroceryList testGroceryList = groceryListList.get(groceryListList.size() - 1);
        assertThat(testGroceryList.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the GroceryList in Elasticsearch
        verify(mockGroceryListSearchRepository, times(1)).save(testGroceryList);
    }

    @Test
    @Transactional
    public void createGroceryListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groceryListRepository.findAll().size();

        // Create the GroceryList with an existing ID
        groceryList.setId(1L);
        GroceryListDTO groceryListDTO = groceryListMapper.toDto(groceryList);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroceryListMockMvc.perform(post("/api/grocery-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groceryListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GroceryList in the database
        List<GroceryList> groceryListList = groceryListRepository.findAll();
        assertThat(groceryListList).hasSize(databaseSizeBeforeCreate);

        // Validate the GroceryList in Elasticsearch
        verify(mockGroceryListSearchRepository, times(0)).save(groceryList);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = groceryListRepository.findAll().size();
        // set the field null
        groceryList.setName(null);

        // Create the GroceryList, which fails.
        GroceryListDTO groceryListDTO = groceryListMapper.toDto(groceryList);

        restGroceryListMockMvc.perform(post("/api/grocery-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groceryListDTO)))
            .andExpect(status().isBadRequest());

        List<GroceryList> groceryListList = groceryListRepository.findAll();
        assertThat(groceryListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGroceryLists() throws Exception {
        // Initialize the database
        groceryListRepository.saveAndFlush(groceryList);

        // Get all the groceryListList
        restGroceryListMockMvc.perform(get("/api/grocery-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groceryList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllGroceryListsWithEagerRelationshipsIsEnabled() throws Exception {
        GroceryListResource groceryListResource = new GroceryListResource(groceryListServiceMock, userService, familyMemberService);
        when(groceryListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restGroceryListMockMvc = MockMvcBuilders.standaloneSetup(groceryListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restGroceryListMockMvc.perform(get("/api/grocery-lists?eagerload=true"))
        .andExpect(status().isOk());

        verify(groceryListServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllGroceryListsWithEagerRelationshipsIsNotEnabled() throws Exception {
        GroceryListResource groceryListResource = new GroceryListResource(groceryListServiceMock, userService, familyMemberService);
            when(groceryListServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restGroceryListMockMvc = MockMvcBuilders.standaloneSetup(groceryListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restGroceryListMockMvc.perform(get("/api/grocery-lists?eagerload=true"))
        .andExpect(status().isOk());

            verify(groceryListServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getGroceryList() throws Exception {
        // Initialize the database
        groceryListRepository.saveAndFlush(groceryList);

        // Get the groceryList
        restGroceryListMockMvc.perform(get("/api/grocery-lists/{id}", groceryList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(groceryList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGroceryList() throws Exception {
        // Get the groceryList
        restGroceryListMockMvc.perform(get("/api/grocery-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroceryList() throws Exception {
        // Initialize the database
        groceryListRepository.saveAndFlush(groceryList);

        int databaseSizeBeforeUpdate = groceryListRepository.findAll().size();

        // Update the groceryList
        GroceryList updatedGroceryList = groceryListRepository.findById(groceryList.getId()).get();
        // Disconnect from session so that the updates on updatedGroceryList are not directly saved in db
        em.detach(updatedGroceryList);
        updatedGroceryList
            .name(UPDATED_NAME);
        GroceryListDTO groceryListDTO = groceryListMapper.toDto(updatedGroceryList);

        restGroceryListMockMvc.perform(put("/api/grocery-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groceryListDTO)))
            .andExpect(status().isOk());

        // Validate the GroceryList in the database
        List<GroceryList> groceryListList = groceryListRepository.findAll();
        assertThat(groceryListList).hasSize(databaseSizeBeforeUpdate);
        GroceryList testGroceryList = groceryListList.get(groceryListList.size() - 1);
        assertThat(testGroceryList.getName()).isEqualTo(UPDATED_NAME);

        // Validate the GroceryList in Elasticsearch
        verify(mockGroceryListSearchRepository, times(1)).save(testGroceryList);
    }

    @Test
    @Transactional
    public void updateNonExistingGroceryList() throws Exception {
        int databaseSizeBeforeUpdate = groceryListRepository.findAll().size();

        // Create the GroceryList
        GroceryListDTO groceryListDTO = groceryListMapper.toDto(groceryList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroceryListMockMvc.perform(put("/api/grocery-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groceryListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GroceryList in the database
        List<GroceryList> groceryListList = groceryListRepository.findAll();
        assertThat(groceryListList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GroceryList in Elasticsearch
        verify(mockGroceryListSearchRepository, times(0)).save(groceryList);
    }

    @Test
    @Transactional
    public void deleteGroceryList() throws Exception {
        // Initialize the database
        groceryListRepository.saveAndFlush(groceryList);

        int databaseSizeBeforeDelete = groceryListRepository.findAll().size();

        // Delete the groceryList
        restGroceryListMockMvc.perform(delete("/api/grocery-lists/{id}", groceryList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GroceryList> groceryListList = groceryListRepository.findAll();
        assertThat(groceryListList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GroceryList in Elasticsearch
        verify(mockGroceryListSearchRepository, times(1)).deleteById(groceryList.getId());
    }

    @Test
    @Transactional
    public void searchGroceryList() throws Exception {
        // Initialize the database
        groceryListRepository.saveAndFlush(groceryList);
        when(mockGroceryListSearchRepository.search(queryStringQuery("id:" + groceryList.getId())))
            .thenReturn(Collections.singletonList(groceryList));
        // Search the groceryList
        restGroceryListMockMvc.perform(get("/api/_search/grocery-lists?query=id:" + groceryList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groceryList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroceryList.class);
        GroceryList groceryList1 = new GroceryList();
        groceryList1.setId(1L);
        GroceryList groceryList2 = new GroceryList();
        groceryList2.setId(groceryList1.getId());
        assertThat(groceryList1).isEqualTo(groceryList2);
        groceryList2.setId(2L);
        assertThat(groceryList1).isNotEqualTo(groceryList2);
        groceryList1.setId(null);
        assertThat(groceryList1).isNotEqualTo(groceryList2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroceryListDTO.class);
        GroceryListDTO groceryListDTO1 = new GroceryListDTO();
        groceryListDTO1.setId(1L);
        GroceryListDTO groceryListDTO2 = new GroceryListDTO();
        assertThat(groceryListDTO1).isNotEqualTo(groceryListDTO2);
        groceryListDTO2.setId(groceryListDTO1.getId());
        assertThat(groceryListDTO1).isEqualTo(groceryListDTO2);
        groceryListDTO2.setId(2L);
        assertThat(groceryListDTO1).isNotEqualTo(groceryListDTO2);
        groceryListDTO1.setId(null);
        assertThat(groceryListDTO1).isNotEqualTo(groceryListDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(groceryListMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(groceryListMapper.fromId(null)).isNull();
    }
}
