package com.crypto.daniel.web.rest;

import com.crypto.daniel.Food24App;

import com.crypto.daniel.domain.FamilyGroup;
import com.crypto.daniel.repository.FamilyGroupRepository;
import com.crypto.daniel.repository.search.FamilyGroupSearchRepository;
import com.crypto.daniel.service.FamilyGroupService;
import com.crypto.daniel.service.dto.FamilyGroupDTO;
import com.crypto.daniel.service.mapper.FamilyGroupMapper;
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
 * Test class for the FamilyGroupResource REST controller.
 *
 * @see FamilyGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Food24App.class)
public class FamilyGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private FamilyGroupRepository familyGroupRepository;

    @Autowired
    private FamilyGroupMapper familyGroupMapper;

    @Autowired
    private FamilyGroupService familyGroupService;

    /**
     * This repository is mocked in the com.crypto.daniel.repository.search test package.
     *
     * @see com.crypto.daniel.repository.search.FamilyGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private FamilyGroupSearchRepository mockFamilyGroupSearchRepository;

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

    private MockMvc restFamilyGroupMockMvc;

    private FamilyGroup familyGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FamilyGroupResource familyGroupResource = new FamilyGroupResource(familyGroupService);
        this.restFamilyGroupMockMvc = MockMvcBuilders.standaloneSetup(familyGroupResource)
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
    public static FamilyGroup createEntity(EntityManager em) {
        FamilyGroup familyGroup = new FamilyGroup()
            .name(DEFAULT_NAME);
        return familyGroup;
    }

    @Before
    public void initTest() {
        familyGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createFamilyGroup() throws Exception {
        int databaseSizeBeforeCreate = familyGroupRepository.findAll().size();

        // Create the FamilyGroup
        FamilyGroupDTO familyGroupDTO = familyGroupMapper.toDto(familyGroup);
        restFamilyGroupMockMvc.perform(post("/api/family-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the FamilyGroup in the database
        List<FamilyGroup> familyGroupList = familyGroupRepository.findAll();
        assertThat(familyGroupList).hasSize(databaseSizeBeforeCreate + 1);
        FamilyGroup testFamilyGroup = familyGroupList.get(familyGroupList.size() - 1);
        assertThat(testFamilyGroup.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the FamilyGroup in Elasticsearch
        verify(mockFamilyGroupSearchRepository, times(1)).save(testFamilyGroup);
    }

    @Test
    @Transactional
    public void createFamilyGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = familyGroupRepository.findAll().size();

        // Create the FamilyGroup with an existing ID
        familyGroup.setId(1L);
        FamilyGroupDTO familyGroupDTO = familyGroupMapper.toDto(familyGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyGroupMockMvc.perform(post("/api/family-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FamilyGroup in the database
        List<FamilyGroup> familyGroupList = familyGroupRepository.findAll();
        assertThat(familyGroupList).hasSize(databaseSizeBeforeCreate);

        // Validate the FamilyGroup in Elasticsearch
        verify(mockFamilyGroupSearchRepository, times(0)).save(familyGroup);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = familyGroupRepository.findAll().size();
        // set the field null
        familyGroup.setName(null);

        // Create the FamilyGroup, which fails.
        FamilyGroupDTO familyGroupDTO = familyGroupMapper.toDto(familyGroup);

        restFamilyGroupMockMvc.perform(post("/api/family-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyGroupDTO)))
            .andExpect(status().isBadRequest());

        List<FamilyGroup> familyGroupList = familyGroupRepository.findAll();
        assertThat(familyGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFamilyGroups() throws Exception {
        // Initialize the database
        familyGroupRepository.saveAndFlush(familyGroup);

        // Get all the familyGroupList
        restFamilyGroupMockMvc.perform(get("/api/family-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getFamilyGroup() throws Exception {
        // Initialize the database
        familyGroupRepository.saveAndFlush(familyGroup);

        // Get the familyGroup
        restFamilyGroupMockMvc.perform(get("/api/family-groups/{id}", familyGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(familyGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFamilyGroup() throws Exception {
        // Get the familyGroup
        restFamilyGroupMockMvc.perform(get("/api/family-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFamilyGroup() throws Exception {
        // Initialize the database
        familyGroupRepository.saveAndFlush(familyGroup);

        int databaseSizeBeforeUpdate = familyGroupRepository.findAll().size();

        // Update the familyGroup
        FamilyGroup updatedFamilyGroup = familyGroupRepository.findById(familyGroup.getId()).get();
        // Disconnect from session so that the updates on updatedFamilyGroup are not directly saved in db
        em.detach(updatedFamilyGroup);
        updatedFamilyGroup
            .name(UPDATED_NAME);
        FamilyGroupDTO familyGroupDTO = familyGroupMapper.toDto(updatedFamilyGroup);

        restFamilyGroupMockMvc.perform(put("/api/family-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyGroupDTO)))
            .andExpect(status().isOk());

        // Validate the FamilyGroup in the database
        List<FamilyGroup> familyGroupList = familyGroupRepository.findAll();
        assertThat(familyGroupList).hasSize(databaseSizeBeforeUpdate);
        FamilyGroup testFamilyGroup = familyGroupList.get(familyGroupList.size() - 1);
        assertThat(testFamilyGroup.getName()).isEqualTo(UPDATED_NAME);

        // Validate the FamilyGroup in Elasticsearch
        verify(mockFamilyGroupSearchRepository, times(1)).save(testFamilyGroup);
    }

    @Test
    @Transactional
    public void updateNonExistingFamilyGroup() throws Exception {
        int databaseSizeBeforeUpdate = familyGroupRepository.findAll().size();

        // Create the FamilyGroup
        FamilyGroupDTO familyGroupDTO = familyGroupMapper.toDto(familyGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyGroupMockMvc.perform(put("/api/family-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FamilyGroup in the database
        List<FamilyGroup> familyGroupList = familyGroupRepository.findAll();
        assertThat(familyGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FamilyGroup in Elasticsearch
        verify(mockFamilyGroupSearchRepository, times(0)).save(familyGroup);
    }

    @Test
    @Transactional
    public void deleteFamilyGroup() throws Exception {
        // Initialize the database
        familyGroupRepository.saveAndFlush(familyGroup);

        int databaseSizeBeforeDelete = familyGroupRepository.findAll().size();

        // Delete the familyGroup
        restFamilyGroupMockMvc.perform(delete("/api/family-groups/{id}", familyGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FamilyGroup> familyGroupList = familyGroupRepository.findAll();
        assertThat(familyGroupList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FamilyGroup in Elasticsearch
        verify(mockFamilyGroupSearchRepository, times(1)).deleteById(familyGroup.getId());
    }

    @Test
    @Transactional
    public void searchFamilyGroup() throws Exception {
        // Initialize the database
        familyGroupRepository.saveAndFlush(familyGroup);
        when(mockFamilyGroupSearchRepository.search(queryStringQuery("id:" + familyGroup.getId())))
            .thenReturn(Collections.singletonList(familyGroup));
        // Search the familyGroup
        restFamilyGroupMockMvc.perform(get("/api/_search/family-groups?query=id:" + familyGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyGroup.class);
        FamilyGroup familyGroup1 = new FamilyGroup();
        familyGroup1.setId(1L);
        FamilyGroup familyGroup2 = new FamilyGroup();
        familyGroup2.setId(familyGroup1.getId());
        assertThat(familyGroup1).isEqualTo(familyGroup2);
        familyGroup2.setId(2L);
        assertThat(familyGroup1).isNotEqualTo(familyGroup2);
        familyGroup1.setId(null);
        assertThat(familyGroup1).isNotEqualTo(familyGroup2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyGroupDTO.class);
        FamilyGroupDTO familyGroupDTO1 = new FamilyGroupDTO();
        familyGroupDTO1.setId(1L);
        FamilyGroupDTO familyGroupDTO2 = new FamilyGroupDTO();
        assertThat(familyGroupDTO1).isNotEqualTo(familyGroupDTO2);
        familyGroupDTO2.setId(familyGroupDTO1.getId());
        assertThat(familyGroupDTO1).isEqualTo(familyGroupDTO2);
        familyGroupDTO2.setId(2L);
        assertThat(familyGroupDTO1).isNotEqualTo(familyGroupDTO2);
        familyGroupDTO1.setId(null);
        assertThat(familyGroupDTO1).isNotEqualTo(familyGroupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(familyGroupMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(familyGroupMapper.fromId(null)).isNull();
    }
}
