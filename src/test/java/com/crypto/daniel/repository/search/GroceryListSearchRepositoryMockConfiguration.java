package com.crypto.daniel.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of GroceryListSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class GroceryListSearchRepositoryMockConfiguration {

    @MockBean
    private GroceryListSearchRepository mockGroceryListSearchRepository;

}
