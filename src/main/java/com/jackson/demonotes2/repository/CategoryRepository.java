package com.jackson.demonotes2.repository;

import com.jackson.demonotes2.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
