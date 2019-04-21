package com.crypto.daniel.repository;

import com.crypto.daniel.domain.FamilyGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FamilyGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilyGroupRepository extends JpaRepository<FamilyGroup, Long> {

}
