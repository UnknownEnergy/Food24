package com.crypto.daniel.repository.search;

import com.crypto.daniel.domain.StoreItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StoreItem entity.
 */
public interface StoreItemSearchRepository extends ElasticsearchRepository<StoreItem, Long> {
}
