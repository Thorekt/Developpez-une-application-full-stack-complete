package com.thorekt.mdd.microservice.user_service.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import com.thorekt.mdd.microservice.user_service.dto.UserDto;
import com.thorekt.mdd.microservice.user_service.model.User;

/**
 * Mapper for User entity and User DTO
 * 
 * @author thorekt
 */
@Component
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, User> {
}