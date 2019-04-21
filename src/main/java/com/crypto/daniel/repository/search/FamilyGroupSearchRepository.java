package com.crypto.daniel.repository.search;

import com.crypto.daniel.domain.FamilyGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FamilyGroup entity.
 */
public interface FamilyGroupSearchRepository extends ElasticsearchRepository<FamilyGroup, Long> {
}
