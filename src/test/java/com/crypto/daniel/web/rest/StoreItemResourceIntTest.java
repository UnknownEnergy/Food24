package com.crypto.daniel.web.rest;

import com.crypto.daniel.Food24App;

import com.crypto.daniel.domain.StoreItem;
import com.crypto.daniel.repository.StoreItemRepository;
import com.crypto.daniel.repository.search.StoreItemSearchRepository;
import com.crypto.daniel.service.StoreItemService;
import com.crypto.daniel.service.dto.StoreItemDTO;
import com.crypto.daniel.service.mapper.StoreItemMapper;
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
 * Test class for the StoreItemResource REST controller.
 *
 * @see StoreItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Food24App.class)
public class StoreItemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private StoreItemRepository storeItemRepository;

    @Autowired
    private StoreItemMapper storeItemMapper;

    @Autowired
    private StoreItemService storeItemService;

    /**
     * This repository is mocked in the com.crypto.daniel.repository.search test package.
     *
     * @see com.crypto.daniel.repository.search.StoreItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private StoreItemSearchRepository mockStoreItemSearchRepository;

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

    private MockMvc restStoreItemMockMvc;

    private StoreItem storeItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StoreItemResource storeItemResource = new StoreItemResource(storeItemService);
        this.restStoreItemMockMvc = MockMvcBuilders.standaloneSetup(storeItemResource)
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
    public static StoreItem createEntity(EntityManager em) {
        StoreItem storeItem = new StoreItem()
            .name(DEFAULT_NAME);
        return storeItem;
    }

    @Before
    public void initTest() {
        storeItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createStoreItem() throws Exception {
        int databaseSizeBeforeCreate = storeItemRepository.findAll().size();

        // Create the StoreItem
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(storeItem);
        restStoreItemMockMvc.perform(post("/api/store-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeItemDTO)))
            .andExpect(status().isCreated());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeCreate + 1);
        StoreItem testStoreItem = storeItemList.get(storeItemList.size() - 1);
        assertThat(testStoreItem.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the StoreItem in Elasticsearch
        verify(mockStoreItemSearchRepository, times(1)).save(testStoreItem);
    }

    @Test
    @Transactional
    public void createStoreItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storeItemRepository.findAll().size();

        // Create the StoreItem with an existing ID
        storeItem.setId(1L);
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(storeItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreItemMockMvc.perform(post("/api/store-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the StoreItem in Elasticsearch
        verify(mockStoreItemSearchRepository, times(0)).save(storeItem);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = storeItemRepository.findAll().size();
        // set the field null
        storeItem.setName(null);

        // Create the StoreItem, which fails.
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(storeItem);

        restStoreItemMockMvc.perform(post("/api/store-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeItemDTO)))
            .andExpect(status().isBadRequest());

        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStoreItems() throws Exception {
        // Initialize the database
        storeItemRepository.saveAndFlush(storeItem);

        // Get all the storeItemList
        restStoreItemMockMvc.perform(get("/api/store-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getStoreItem() throws Exception {
        // Initialize the database
        storeItemRepository.saveAndFlush(storeItem);

        // Get the storeItem
        restStoreItemMockMvc.perform(get("/api/store-items/{id}", storeItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(storeItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStoreItem() throws Exception {
        // Get the storeItem
        restStoreItemMockMvc.perform(get("/api/store-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStoreItem() throws Exception {
        // Initialize the database
        storeItemRepository.saveAndFlush(storeItem);

        int databaseSizeBeforeUpdate = storeItemRepository.findAll().size();

        // Update the storeItem
        StoreItem updatedStoreItem = storeItemRepository.findById(storeItem.getId()).get();
        // Disconnect from session so that the updates on updatedStoreItem are not directly saved in db
        em.detach(updatedStoreItem);
        updatedStoreItem
            .name(UPDATED_NAME);
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(updatedStoreItem);

        restStoreItemMockMvc.perform(put("/api/store-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeItemDTO)))
            .andExpect(status().isOk());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeUpdate);
        StoreItem testStoreItem = storeItemList.get(storeItemList.size() - 1);
        assertThat(testStoreItem.getName()).isEqualTo(UPDATED_NAME);

        // Validate the StoreItem in Elasticsearch
        verify(mockStoreItemSearchRepository, times(1)).save(testStoreItem);
    }

    @Test
    @Transactional
    public void updateNonExistingStoreItem() throws Exception {
        int databaseSizeBeforeUpdate = storeItemRepository.findAll().size();

        // Create the StoreItem
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(storeItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreItemMockMvc.perform(put("/api/store-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storeItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StoreItem in Elasticsearch
        verify(mockStoreItemSearchRepository, times(0)).save(storeItem);
    }

    @Test
    @Transactional
    public void deleteStoreItem() throws Exception {
        // Initialize the database
        storeItemRepository.saveAndFlush(storeItem);

        int databaseSizeBeforeDelete = storeItemRepository.findAll().size();

        // Delete the storeItem
        restStoreItemMockMvc.perform(delete("/api/store-items/{id}", storeItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StoreItem in Elasticsearch
        verify(mockStoreItemSearchRepository, times(1)).deleteById(storeItem.getId());
    }

    @Test
    @Transactional
    public void searchStoreItem() throws Exception {
        // Initialize the database
        storeItemRepository.saveAndFlush(storeItem);
        when(mockStoreItemSearchRepository.search(queryStringQuery("id:" + storeItem.getId())))
            .thenReturn(Collections.singletonList(storeItem));
        // Search the storeItem
        restStoreItemMockMvc.perform(get("/api/_search/store-items?query=id:" + storeItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreItem.class);
        StoreItem storeItem1 = new StoreItem();
        storeItem1.setId(1L);
        StoreItem storeItem2 = new StoreItem();
        storeItem2.setId(storeItem1.getId());
        assertThat(storeItem1).isEqualTo(storeItem2);
        storeItem2.setId(2L);
        assertThat(storeItem1).isNotEqualTo(storeItem2);
        storeItem1.setId(null);
        assertThat(storeItem1).isNotEqualTo(storeItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoreItemDTO.class);
        StoreItemDTO storeItemDTO1 = new StoreItemDTO();
        storeItemDTO1.setId(1L);
        StoreItemDTO storeItemDTO2 = new StoreItemDTO();
        assertThat(storeItemDTO1).isNotEqualTo(storeItemDTO2);
        storeItemDTO2.setId(storeItemDTO1.getId());
        assertThat(storeItemDTO1).isEqualTo(storeItemDTO2);
        storeItemDTO2.setId(2L);
        assertThat(storeItemDTO1).isNotEqualTo(storeItemDTO2);
        storeItemDTO1.setId(null);
        assertThat(storeItemDTO1).isNotEqualTo(storeItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(storeItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(storeItemMapper.fromId(null)).isNull();
    }
}
