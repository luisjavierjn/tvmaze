package com.realpage.tvmaze.repositories.dbrepo;

import com.realpage.tvmaze.domain.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findAll();

    User findByUsername(String username);
}