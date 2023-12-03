package com.example.bookmanagementms.repository;

import com.example.bookmanagementms.domain.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findBookEntitiesByName(String name);
    List<BookEntity> findBookEntitiesByAuthor(String author);
//    Set<BookEntity> findBookEntitiesByAuthor(AuthorEntity author);
}
