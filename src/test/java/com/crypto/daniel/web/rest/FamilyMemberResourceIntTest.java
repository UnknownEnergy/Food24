package com.crypto.daniel.web.rest;

import com.crypto.daniel.Food24App;

import com.crypto.daniel.domain.FamilyMember;
import com.crypto.daniel.repository.FamilyMemberRepository;
import com.crypto.daniel.repository.search.FamilyMemberSearchRepository;
import com.crypto.daniel.service.FamilyMemberService;
import com.crypto.daniel.service.UserService;
import com.crypto.daniel.service.dto.FamilyMemberDTO;
import com.crypto.daniel.service.mapper.FamilyMemberMapper;
import com.crypto.daniel.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
 * Test class for the FamilyMemberResource REST controller.
 *
 * @see FamilyMemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Food24App.class)
public class FamilyMemberResourceIntTest {

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Mock
    private FamilyMemberRepository familyMemberRepositoryMock;

    @Autowired
    private FamilyMemberMapper familyMemberMapper;

    @Mock
    private FamilyMemberService familyMemberServiceMock;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private UserService userService;

    /**
     * This repository is mocked in the com.crypto.daniel.repository.search test package.
     *
     * @see com.crypto.daniel.repository.search.FamilyMemberSearchRepositoryMockConfiguration
     */
    @Autowired
    private FamilyMemberSearchRepository mockFamilyMemberSearchRepository;

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

    private MockMvc restFamilyMemberMockMvc;

    private FamilyMember familyMember;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FamilyMemberResource familyMemberResource = new FamilyMemberResource(familyMemberService, userService);
        this.restFamilyMemberMockMvc = MockMvcBuilders.standaloneSetup(familyMemberResource)
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
    public static FamilyMember createEntity(EntityManager em) {
        FamilyMember familyMember = new FamilyMember();
        return familyMember;
    }

    @Before
    public void initTest() {
        familyMember = createEntity(em);
    }

    @Test
    @Transactional
    public void createFamilyMember() throws Exception {
        int databaseSizeBeforeCreate = familyMemberRepository.findAll().size();

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);
        restFamilyMemberMockMvc.perform(post("/api/family-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyMemberDTO)))
            .andExpect(status().isCreated());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeCreate + 1);
        FamilyMember testFamilyMember = familyMemberList.get(familyMemberList.size() - 1);

        // Validate the FamilyMember in Elasticsearch
        verify(mockFamilyMemberSearchRepository, times(1)).save(testFamilyMember);
    }

    @Test
    @Transactional
    public void createFamilyMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = familyMemberRepository.findAll().size();

        // Create the FamilyMember with an existing ID
        familyMember.setId(1L);
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyMemberMockMvc.perform(post("/api/family-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyMemberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeCreate);

        // Validate the FamilyMember in Elasticsearch
        verify(mockFamilyMemberSearchRepository, times(0)).save(familyMember);
    }

    @Test
    @Transactional
    public void getAllFamilyMembers() throws Exception {
        // Initialize the database
        familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList
        restFamilyMemberMockMvc.perform(get("/api/family-members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyMember.getId().intValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllFamilyMembersWithEagerRelationshipsIsEnabled() throws Exception {
        FamilyMemberResource familyMemberResource = new FamilyMemberResource(familyMemberServiceMock, userService);
        when(familyMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restFamilyMemberMockMvc = MockMvcBuilders.standaloneSetup(familyMemberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFamilyMemberMockMvc.perform(get("/api/family-members?eagerload=true"))
        .andExpect(status().isOk());

        verify(familyMemberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllFamilyMembersWithEagerRelationshipsIsNotEnabled() throws Exception {
        FamilyMemberResource familyMemberResource = new FamilyMemberResource(familyMemberServiceMock, userService);
            when(familyMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restFamilyMemberMockMvc = MockMvcBuilders.standaloneSetup(familyMemberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restFamilyMemberMockMvc.perform(get("/api/family-members?eagerload=true"))
        .andExpect(status().isOk());

            verify(familyMemberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getFamilyMember() throws Exception {
        // Initialize the database
        familyMemberRepository.saveAndFlush(familyMember);

        // Get the familyMember
        restFamilyMemberMockMvc.perform(get("/api/family-members/{id}", familyMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(familyMember.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFamilyMember() throws Exception {
        // Get the familyMember
        restFamilyMemberMockMvc.perform(get("/api/family-members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFamilyMember() throws Exception {
        // Initialize the database
        familyMemberRepository.saveAndFlush(familyMember);

        int databaseSizeBeforeUpdate = familyMemberRepository.findAll().size();

        // Update the familyMember
        FamilyMember updatedFamilyMember = familyMemberRepository.findById(familyMember.getId()).get();
        // Disconnect from session so that the updates on updatedFamilyMember are not directly saved in db
        em.detach(updatedFamilyMember);
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(updatedFamilyMember);

        restFamilyMemberMockMvc.perform(put("/api/family-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyMemberDTO)))
            .andExpect(status().isOk());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeUpdate);
        FamilyMember testFamilyMember = familyMemberList.get(familyMemberList.size() - 1);

        // Validate the FamilyMember in Elasticsearch
        verify(mockFamilyMemberSearchRepository, times(1)).save(testFamilyMember);
    }

    @Test
    @Transactional
    public void updateNonExistingFamilyMember() throws Exception {
        int databaseSizeBeforeUpdate = familyMemberRepository.findAll().size();

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc.perform(put("/api/family-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familyMemberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FamilyMember in the database
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FamilyMember in Elasticsearch
        verify(mockFamilyMemberSearchRepository, times(0)).save(familyMember);
    }

    @Test
    @Transactional
    public void deleteFamilyMember() throws Exception {
        // Initialize the database
        familyMemberRepository.saveAndFlush(familyMember);

        int databaseSizeBeforeDelete = familyMemberRepository.findAll().size();

        // Delete the familyMember
        restFamilyMemberMockMvc.perform(delete("/api/family-members/{id}", familyMember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FamilyMember> familyMemberList = familyMemberRepository.findAll();
        assertThat(familyMemberList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FamilyMember in Elasticsearch
        verify(mockFamilyMemberSearchRepository, times(1)).deleteById(familyMember.getId());
    }

    @Test
    @Transactional
    public void searchFamilyMember() throws Exception {
        // Initialize the database
        familyMemberRepository.saveAndFlush(familyMember);
        when(mockFamilyMemberSearchRepository.search(queryStringQuery("id:" + familyMember.getId())))
            .thenReturn(Collections.singletonList(familyMember));
        // Search the familyMember
        restFamilyMemberMockMvc.perform(get("/api/_search/family-members?query=id:" + familyMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyMember.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyMember.class);
        FamilyMember familyMember1 = new FamilyMember();
        familyMember1.setId(1L);
        FamilyMember familyMember2 = new FamilyMember();
        familyMember2.setId(familyMember1.getId());
        assertThat(familyMember1).isEqualTo(familyMember2);
        familyMember2.setId(2L);
        assertThat(familyMember1).isNotEqualTo(familyMember2);
        familyMember1.setId(null);
        assertThat(familyMember1).isNotEqualTo(familyMember2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyMemberDTO.class);
        FamilyMemberDTO familyMemberDTO1 = new FamilyMemberDTO();
        familyMemberDTO1.setId(1L);
        FamilyMemberDTO familyMemberDTO2 = new FamilyMemberDTO();
        assertThat(familyMemberDTO1).isNotEqualTo(familyMemberDTO2);
        familyMemberDTO2.setId(familyMemberDTO1.getId());
        assertThat(familyMemberDTO1).isEqualTo(familyMemberDTO2);
        familyMemberDTO2.setId(2L);
        assertThat(familyMemberDTO1).isNotEqualTo(familyMemberDTO2);
        familyMemberDTO1.setId(null);
        assertThat(familyMemberDTO1).isNotEqualTo(familyMemberDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(familyMemberMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(familyMemberMapper.fromId(null)).isNull();
    }
}
