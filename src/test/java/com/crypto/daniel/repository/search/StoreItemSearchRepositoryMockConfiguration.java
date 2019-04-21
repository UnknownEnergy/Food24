package com.crypto.daniel.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of StoreItemSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class StoreItemSearchRepositoryMockConfiguration {

    @MockBean
    private StoreItemSearchRepository mockStoreItemSearchRepository;

}
