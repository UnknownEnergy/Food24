package com.crypto.daniel.repository;

import com.crypto.daniel.domain.StoreItemInstance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StoreItemInstance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreItemInstanceRepository extends JpaRepository<StoreItemInstance, Long> {

}
