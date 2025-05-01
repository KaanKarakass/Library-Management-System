package com.kaankarakas.librarymanagement.mapper.user;

import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.request.user.UpdateUserRequest;
import com.kaankarakas.librarymanagement.dto.response.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserDTO toDTO(User user);

    void updateUserFromDto(UpdateUserRequest request, @MappingTarget User user);
}
