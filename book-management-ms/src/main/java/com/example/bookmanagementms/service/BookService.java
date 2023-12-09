package com.example.bookmanagementms.service;

import com.example.bookmanagementms.domain.BookEntity;
import com.example.bookmanagementms.dto.BookRequest;
import com.example.bookmanagementms.dto.BookResponseDto;
import com.example.bookmanagementms.exception.AuthorCanNotDeleteBook;
import com.example.bookmanagementms.exception.AuthorCanNotUpdateBook;
import com.example.bookmanagementms.exception.BookException;
import com.example.bookmanagementms.exception.CustomException;
import com.example.bookmanagementms.mapper.GroupMapper;
import com.example.bookmanagementms.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {
    private static final GroupMapper mapper = GroupMapper.INSTANCE;
    private final BookRepository bookRepository;
    private final JwtService jwtService;

    public List<BookResponseDto> getAll(){
        List<BookResponseDto> bookResponseDtos = mapper.convertToBookResponseDtos(bookRepository.findAll());
        return bookResponseDtos;
    }

    public List<BookResponseDto> searchSpecificBook(String name){
        List<BookEntity> bookEntity = bookRepository.findByName(name);
        return mapper.convertToBookResponseDtos(bookEntity);
    }

    public BookResponseDto getById(Long id){
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(()-> new BookException(id));
        BookResponseDto bookResponseDto = mapper.convertToBookResponseDto(bookEntity);
        return bookResponseDto;
    }

    public BookResponseDto getDetails(Long id){
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(()-> new BookException(id));
        return mapper.convertToBookResponseDto(bookEntity);
    }

    public List<BookResponseDto> getByPublisher(String name){
        List<BookEntity> books = bookRepository.findBookEntitiesByAuthor(name);
        return mapper.convertToBookResponseDtos(books);
    }

    public BookResponseDto create(BookRequest bookRequestDto){
        BookEntity bookEntity = mapper.convertToBookEntity(bookRequestDto);

        if (!jwtService.isAuthor()) {
            throw new CustomException("User does not have the required role to perform this action.");
        }

        return mapper.convertToBookResponseDto(bookRepository.save(bookEntity));
    }

    public BookResponseDto update(Long id, BookRequest bookRequestDto){
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(()-> new BookException(id));

        if (!jwtService.isAuthor()) {
            throw new CustomException("User does not have the required role to perform this action.");
        }

        if (!bookRepository.findBookEntitiesByAuthor(jwtService.getSubject()).contains(bookEntity)){
            throw new AuthorCanNotUpdateBook(bookEntity.getName());
        }
        bookEntity.setName(bookRequestDto.getName());
        bookEntity.setType(bookRequestDto.getType());
        bookEntity.setReleaseDate(bookRequestDto.getReleaseDate());
        bookEntity.setAuthor(bookRequestDto.getAuthor());
        return mapper.convertToBookResponseDto(bookRepository.save(bookEntity));
    }

    public void delete(Long id){
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(()-> new BookException(id));

        if (!jwtService.isAuthor()) {
            throw new CustomException("User does not have the required role to perform this action.");
        }

        if (!bookRepository.findBookEntitiesByAuthor(jwtService.getSubject()).contains(bookEntity)){
            throw new AuthorCanNotDeleteBook(bookEntity.getName());
        }

        bookRepository.delete(bookEntity);
    }
}
