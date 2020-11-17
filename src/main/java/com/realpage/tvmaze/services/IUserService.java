package com.realpage.tvmaze.services;

import com.realpage.tvmaze.domain.dto.UserDto;

import java.util.List;

public interface IUserService {

    List<UserDto> findAll();

    UserDto save(UserDto user);

    void delete(int id);

    UserDto findOne(String username);

    UserDto findById(int id);

    UserDto update(UserDto userDto);
}
