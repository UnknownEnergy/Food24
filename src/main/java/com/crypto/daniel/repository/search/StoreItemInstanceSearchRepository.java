package com.crypto.daniel.repository.search;

import com.crypto.daniel.domain.StoreItemInstance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StoreItemInstance entity.
 */
public interface StoreItemInstanceSearchRepository extends ElasticsearchRepository<StoreItemInstance, Long> {
}
