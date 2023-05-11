package ru.clevertec.ecl.service.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.dto.request.filter.impl.UserFilter;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.entity.User;

@Mapper
public interface UserMapper {

    User toFrom(UserRequest userRequest);

    User toFrom(UserResponse userResponse);

    UserResponse toFrom(User user);

    User toFrom(UserFilter userFilter);

    @Mapping(target = "orders", ignore = true)
    void updateUser(@MappingTarget User user, UserRequest userRequest);

}