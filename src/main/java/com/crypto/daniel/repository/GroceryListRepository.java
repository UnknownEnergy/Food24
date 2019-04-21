package com.crypto.daniel.repository;

import com.crypto.daniel.domain.GroceryList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the GroceryList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroceryListRepository extends JpaRepository<GroceryList, Long> {

    @Query(value = "select distinct grocery_list from GroceryList grocery_list left join fetch grocery_list.storeItems",
        countQuery = "select count(distinct grocery_list) from GroceryList grocery_list")
    Page<GroceryList> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct grocery_list from GroceryList grocery_list left join fetch grocery_list.storeItems")
    List<GroceryList> findAllWithEagerRelationships();

    @Query("select grocery_list from GroceryList grocery_list left join fetch grocery_list.storeItems where grocery_list.id =:id")
    Optional<GroceryList> findOneWithEagerRelationships(@Param("id") Long id);

}
