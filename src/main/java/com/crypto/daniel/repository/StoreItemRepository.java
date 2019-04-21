package com.crypto.daniel.repository;

import com.crypto.daniel.domain.StoreItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StoreItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreItemRepository extends JpaRepository<StoreItem, Long> {

}
