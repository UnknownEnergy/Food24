package com.crypto.daniel.repository;

import com.crypto.daniel.domain.FamilyMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the FamilyMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {

    @Query(value = "select distinct family_member from FamilyMember family_member left join fetch family_member.familyGroups",
        countQuery = "select count(distinct family_member) from FamilyMember family_member")
    Page<FamilyMember> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct family_member from FamilyMember family_member left join fetch family_member.familyGroups")
    List<FamilyMember> findAllWithEagerRelationships();

    @Query("select family_member from FamilyMember family_member left join fetch family_member.familyGroups where family_member.id =:id")
    Optional<FamilyMember> findOneWithEagerRelationships(@Param("id") Long id);

}
