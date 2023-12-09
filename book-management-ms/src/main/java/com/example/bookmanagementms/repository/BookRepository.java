package com.example.bookmanagementms.repository;

import com.example.bookmanagementms.domain.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    @Query("select b from BookEntity b where b.name= :name")
    List<BookEntity> findByName(String name);
    List<BookEntity> findBookEntitiesByAuthor(String author);
}
