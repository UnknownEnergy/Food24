package com.crypto.daniel.repository.search;

import com.crypto.daniel.domain.GroceryList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the GroceryList entity.
 */
public interface GroceryListSearchRepository extends ElasticsearchRepository<GroceryList, Long> {
}
