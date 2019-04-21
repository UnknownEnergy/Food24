package com.crypto.daniel.repository.search;

import com.crypto.daniel.domain.FamilyMember;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FamilyMember entity.
 */
public interface FamilyMemberSearchRepository extends ElasticsearchRepository<FamilyMember, Long> {
}
