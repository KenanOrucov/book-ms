package com.example.usermanagementms.mapper;

import com.example.usermanagementms.domain.User;
import com.example.usermanagementms.dto.user.UserRequestDto;
import com.example.usermanagementms.dto.user.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    User convertToUser(UserRequestDto userDto);

    List<UserResponseDto> convertToUserResponseDtos(List<User> all);

    UserResponseDto convertToUserResponseDto(User user);
}
