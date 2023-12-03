package com.example.bookmanagementms.controller;

import com.example.bookmanagementms.dto.BookDetailResponse;
import com.example.bookmanagementms.dto.BookRequest;
import com.example.bookmanagementms.dto.BookResponseDto;
import com.example.bookmanagementms.service.BookService;
import com.example.bookmanagementms.util.HasRoleAuthor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/book")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAll(){
        return ResponseEntity.ok(bookService.getAll());
    }

    @GetMapping("/bookName")
    public ResponseEntity<List<BookResponseDto>> getSpecificBook(@RequestParam String name){
        return ResponseEntity.ok(bookService.searchSpecificBook(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getById(id));
    }

    @GetMapping("/bookDetail")
    public ResponseEntity<BookResponseDto> getBookDetail(@RequestParam Long id){
        return ResponseEntity.ok(bookService.getDetails(id));
    }

    @GetMapping("/author")
    public ResponseEntity<List<BookResponseDto>> getBookByAuthor(@RequestParam String name){
        return ResponseEntity.ok(bookService.getByPublisher(name));
    }

    @HasRoleAuthor
    @PostMapping
    public ResponseEntity<BookResponseDto> create(@RequestBody BookRequest bookRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(bookRequestDto));
    }

    @HasRoleAuthor
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> update(@PathVariable Long id, @RequestBody BookRequest bookRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.update(id, bookRequestDto));
    }

    @HasRoleAuthor
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        bookService.delete(id);
    }
}
