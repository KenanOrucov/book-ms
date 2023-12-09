package com.example.bookmanagementms.mapper;

import com.example.bookmanagementms.domain.BookEntity;
import com.example.bookmanagementms.dto.BookRequest;
import com.example.bookmanagementms.dto.BookResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    List<BookResponseDto> convertToBookResponseDtos(List<BookEntity> collect);

    BookResponseDto convertToBookResponseDto(BookEntity bookEntity);

    BookEntity convertToBookEntity(BookRequest bookRequestDto);
}
