package com.crypto.daniel.web.rest;

import com.crypto.daniel.Food24App;

import com.crypto.daniel.domain.StoreItemInstance;
import com.crypto.daniel.repository.StoreItemInstanceRepository;
import com.crypto.daniel.repository.search.StoreItemInstanceSearchRepository;
import com.crypto.daniel.service.*;
import com.crypto.daniel.service.dto.StoreItemInstanceDTO;
import com.crypto.daniel.service.mapper.StoreItemInstanceMapper;
import com.crypto.daniel.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
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
 * Test class for the StoreItemInstanceResource REST controller.
 *
 * @see StoreItemInstanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Food24App.class)
public class StoreItemInstanceResourceIntTest {

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    @Autowired
    private StoreItemInstanceRepository storeItemInstanceRepository;

    @Autowired
    private StoreItemInstanceMapper storeItemInstanceMapper;

    @Autowired
    private StoreItemInstanceService storeItemInstanceService;

    @Autowired
    private GroceryListService groceryListService;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private UserService userService;


    /**
     * This repository is mocked in the com.crypto.daniel.repository.search test package.
     *
     * @see com.crypto.daniel.repository.search.StoreItemInstanceSearchRepositoryMockConfiguration
     */
    @Autowired
    private StoreItemInstanceSearchRepository mockStoreItemInstanceSearchRepository;

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

    private MockMvc restStoreItemInstanceMockMvc;

    private StoreItemInstance storeItemInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StoreItemInstanceResource storeItemInstanceResource = new StoreItemInstanceResource(storeItemInstanceService, groceryListService, familyMemberService, userService);
        this.restStoreItemInstanceMockMvc = MockMvcBuilders.standaloneSetup(storeItemInstanceResource)
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
    public static StoreItemInstance createEntity(EntityManager em) {
        StoreItemInstance storeItemInstance = new StoreItemInstance()
            .price(DEFAULT_PRICE);
        return storeItemInstance;
    }

    @Before
    public void initTest() {
        storeItemInstance = createEntity(em);
    }

    @Test
    @Transactional
    public void createStoreItemInstance() throws Exception {
        int databaseSizeBeforeCreate = storeItemInstanceRepository.findAll().size();

        // Create the StoreItemInstance
        StoreItemInstanceDTO storeItemInstanceDTO = storeItemInstanceMapper.toDto(storeItemInstance);
        restStoreItemInstanceMockMvc.perform(post("/api/store-item-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeItemInstanceDTO)))
            .andExpect(status().isCreated());

        // Validate the StoreItemInstance in the database
        List<StoreItemInstance> storeItemInstanceList = storeItemInstanceRepository.findAll();
        assertThat(storeItemInstanceList).hasSize(databaseSizeBeforeCreate + 1);
        StoreItemInstance testStoreItemInstance = storeItemInstanceList.get(storeItemInstanceList.size() - 1);
        assertThat(testStoreItemInstance.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the StoreItemInstance in Elasticsearch
        verify(mockStoreItemInstanceSearchRepository, times(1)).save(testStoreItemInstance);
    }

    @Test
    @Transactional
    public void createStoreItemInstanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storeItemInstanceRepository.findAll().size();

        // Create the StoreItemInstance with an existing ID
        storeItemInstance.setId(1L);
        StoreItemInstanceDTO storeItemInstanceDTO = storeItemInstanceMapper.toDto(storeItemInstance);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreItemInstanceMockMvc.perform(post("/api/store-item-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeItemInstanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StoreItemInstance in the database
        List<StoreItemInstance> storeItemInstanceList = storeItemInstanceRepository.findAll();
        assertThat(storeItemInstanceList).hasSize(databaseSizeBeforeCreate);

        // Validate the StoreItemInstance in Elasticsearch
        verify(mockStoreItemInstanceSearchRepository, times(0)).save(storeItemInstance);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeItemInstanceRepository.findAll().size();
        // set the field null
        storeItemInstance.setPrice(null);

        // Create the StoreItemInstance, which fails.
        StoreItemInstanceDTO storeItemInstanceDTO = storeItemInstanceMapper.toDto(storeItemInstance);

        restStoreItemInstanceMockMvc.perform(post("/api/store-item-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeItemInstanceDTO)))
            .andExpect(status().isBadRequest());

        List<StoreItemInstance> storeItemInstanceList = storeItemInstanceRepository.findAll();
        assertThat(storeItemInstanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStoreItemInstances() throws Exception {
        // Initialize the database
        storeItemInstanceRepository.saveAndFlush(storeItemInstance);

        // Get all the storeItemInstanceList
        restStoreItemInstanceMockMvc.perform(get("/api/store-item-instances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeItemInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getStoreItemInstance() throws Exception {
        // Initialize the database
        storeItemInstanceRepository.saveAndFlush(storeItemInstance);

        // Get the storeItemInstance
        restStoreItemInstanceMockMvc.perform(get("/api/store-item-instances/{id}", storeItemInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(storeItemInstance.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStoreItemInstance() throws Exception {
        // Get the storeItemInstance
        restStoreItemInstanceMockMvc.perform(get("/api/store-item-instances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStoreItemInstance() throws Exception {
        // Initialize the database
        storeItemInstanceRepository.saveAndFlush(storeItemInstance);

        int databaseSizeBeforeUpdate = storeItemInstanceRepository.findAll().size();

        // Update the storeItemInstance
        StoreItemInstance updatedStoreItemInstance = storeItemInstanceRepository.findById(storeItemInstance.getId()).get();
        // Disconnect from session so that the updates on updatedStoreItemInstance are not directly saved in db
        em.detach(updatedStoreItemInstance);
        updatedStoreItemInstance
            .price(UPDATED_PRICE);
        StoreItemInstanceDTO storeItemInstanceDTO = storeItemInstanceMapper.toDto(updatedStoreItemInstance);

        restStoreItemInstanceMockMvc.perform(put("/api/store-item-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeItemInstanceDTO)))
            .andExpect(status().isOk());

        // Validate the StoreItemInstance in the database
        List<StoreItemInstance> storeItemInstanceList = storeItemInstanceRepository.findAll();
        assertThat(storeItemInstanceList).hasSize(databaseSizeBeforeUpdate);
        StoreItemInstance testStoreItemInstance = storeItemInstanceList.get(storeItemInstanceList.size() - 1);
        assertThat(testStoreItemInstance.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the StoreItemInstance in Elasticsearch
        verify(mockStoreItemInstanceSearchRepository, times(1)).save(testStoreItemInstance);
    }

    @Test
    @Transactional
    public void updateNonExistingStoreItemInstance() throws Exception {
        int databaseSizeBeforeUpdate = storeItemInstanceRepository.findAll().size();

        // Create the StoreItemInstance
        StoreItemInstanceDTO storeItemInstanceDTO = storeItemInstanceMapper.toDto(storeItemInstance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreItemInstanceMockMvc.perform(put("/api/store-item-instances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeItemInstanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StoreItemInstance in the database
        List<StoreItemInstance> storeItemInstanceList = storeItemInstanceRepository.findAll();
        assertThat(storeItemInstanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StoreItemInstance in Elasticsearch
        verify(mockStoreItemInstanceSearchRepository, times(0)).save(storeItemInstance);
    }

    @Test
    @Transactional
    public void deleteStoreItemInstance() throws Exception {
        // Initialize the database
        storeItemInstanceRepository.saveAndFlush(storeItemInstance);

        int databaseSizeBeforeDelete = storeItemInstanceRepository.findAll().size();

        // Delete the storeItemInstance
        restStoreItemInstanceMockMvc.perform(delete("/api/store-item-instances/{id}", storeItemInstance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StoreItemInstance> storeItemInstanceList = storeItemInstanceRepository.findAll();
        assertThat(storeItemInstanceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StoreItemInstance in Elasticsearch
        verify(mockStoreItemInstanceSearchRepository, times(1)).deleteById(storeItemInstance.getId());
    }

    @Test
    @Transactional
    public void searchStoreItemInstance() throws Exception {
        // Initialize the database
        storeItemInstanceRepository.saveAndFlush(storeItemInstance);
        when(mockStoreItemInstanceSearchRepository.search(queryStringQuery("id:" + storeItemInstance.getId())))
            .thenReturn(Collections.singletonList(storeItemInstance));
        // Search the storeItemInstance
        restStoreItemInstanceMockMvc.perform(get("/api/_search/store-item-instances?query=id:" + storeItemInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeItemInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreItemInstance.class);
        StoreItemInstance storeItemInstance1 = new StoreItemInstance();
        storeItemInstance1.setId(1L);
        StoreItemInstance storeItemInstance2 = new StoreItemInstance();
        storeItemInstance2.setId(storeItemInstance1.getId());
        assertThat(storeItemInstance1).isEqualTo(storeItemInstance2);
        storeItemInstance2.setId(2L);
        assertThat(storeItemInstance1).isNotEqualTo(storeItemInstance2);
        storeItemInstance1.setId(null);
        assertThat(storeItemInstance1).isNotEqualTo(storeItemInstance2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreItemInstanceDTO.class);
        StoreItemInstanceDTO storeItemInstanceDTO1 = new StoreItemInstanceDTO();
        storeItemInstanceDTO1.setId(1L);
        StoreItemInstanceDTO storeItemInstanceDTO2 = new StoreItemInstanceDTO();
        assertThat(storeItemInstanceDTO1).isNotEqualTo(storeItemInstanceDTO2);
        storeItemInstanceDTO2.setId(storeItemInstanceDTO1.getId());
        assertThat(storeItemInstanceDTO1).isEqualTo(storeItemInstanceDTO2);
        storeItemInstanceDTO2.setId(2L);
        assertThat(storeItemInstanceDTO1).isNotEqualTo(storeItemInstanceDTO2);
        storeItemInstanceDTO1.setId(null);
        assertThat(storeItemInstanceDTO1).isNotEqualTo(storeItemInstanceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(storeItemInstanceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(storeItemInstanceMapper.fromId(null)).isNull();
    }
}
