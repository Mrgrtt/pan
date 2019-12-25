package com.haylen.pan.repository;

import com.haylen.pan.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author haylen
 * @date 2019-12-25
 */
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
}
