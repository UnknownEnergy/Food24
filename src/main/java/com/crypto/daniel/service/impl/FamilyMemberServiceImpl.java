package com.crypto.daniel.service.impl;

import com.crypto.daniel.service.FamilyMemberService;
import com.crypto.daniel.domain.FamilyMember;
import com.crypto.daniel.repository.FamilyMemberRepository;
import com.crypto.daniel.repository.search.FamilyMemberSearchRepository;
import com.crypto.daniel.service.dto.FamilyMemberDTO;
import com.crypto.daniel.service.mapper.FamilyMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing FamilyMember.
 */
@Service
@Transactional
public class FamilyMemberServiceImpl implements FamilyMemberService {

    private final Logger log = LoggerFactory.getLogger(FamilyMemberServiceImpl.class);

    private final FamilyMemberRepository familyMemberRepository;

    private final FamilyMemberMapper familyMemberMapper;

    private final FamilyMemberSearchRepository familyMemberSearchRepository;

    public FamilyMemberServiceImpl(FamilyMemberRepository familyMemberRepository, FamilyMemberMapper familyMemberMapper, FamilyMemberSearchRepository familyMemberSearchRepository) {
        this.familyMemberRepository = familyMemberRepository;
        this.familyMemberMapper = familyMemberMapper;
        this.familyMemberSearchRepository = familyMemberSearchRepository;
    }

    /**
     * Save a familyMember.
     *
     * @param familyMemberDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FamilyMemberDTO save(FamilyMemberDTO familyMemberDTO) {
        log.debug("Request to save FamilyMember : {}", familyMemberDTO);
        FamilyMember familyMember = familyMemberMapper.toEntity(familyMemberDTO);
        familyMember = familyMemberRepository.save(familyMember);
        FamilyMemberDTO result = familyMemberMapper.toDto(familyMember);
        familyMemberSearchRepository.save(familyMember);
        return result;
    }

    /**
     * Get all the familyMembers.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FamilyMemberDTO> findAll() {
        log.debug("Request to get all FamilyMembers");
        return familyMemberRepository.findAllWithEagerRelationships().stream()
            .map(familyMemberMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the FamilyMember with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<FamilyMemberDTO> findAllWithEagerRelationships(Pageable pageable) {
        return familyMemberRepository.findAllWithEagerRelationships(pageable).map(familyMemberMapper::toDto);
    }
    

    /**
     * Get one familyMember by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FamilyMemberDTO> findOne(Long id) {
        log.debug("Request to get FamilyMember : {}", id);
        return familyMemberRepository.findOneWithEagerRelationships(id)
            .map(familyMemberMapper::toDto);
    }

    /**
     * Delete the familyMember by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FamilyMember : {}", id);
        familyMemberRepository.deleteById(id);
        familyMemberSearchRepository.deleteById(id);
    }

    /**
     * Search for the familyMember corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<FamilyMemberDTO> search(String query) {
        log.debug("Request to search FamilyMembers for query {}", query);
        return StreamSupport
            .stream(familyMemberSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(familyMemberMapper::toDto)
            .collect(Collectors.toList());
    }
}
