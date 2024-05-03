package com.example.santa.domain.preferredcategory.repository;

import com.example.santa.domain.preferredcategory.entity.PreferredCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferredCategoryRepository extends JpaRepository<PreferredCategory, Long> {
    List<PreferredCategory> findAllByUserId(Long id);
}
