package com.realpage.tvmaze.services;

import com.realpage.tvmaze.domain.dto.UserDto;
import com.realpage.tvmaze.domain.entities.User;
import com.realpage.tvmaze.repositories.dbrepo.UserRepository;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService, IUserService {

    static Logger log = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public List<UserDto> findAll() {
        log.info("Getting all the users");
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user,UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto save(UserDto user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setAge(user.getAge());
        newUser.setRol(user.getRol());
        Optional<User> uopt = Optional.of(userRepository.save(newUser));
        log.info("Saving user: " + newUser.getUsername());
        return uopt.map(value -> modelMapper.map(value, UserDto.class)).orElse(null);
    }

    @Override
    public void delete(int id) {
        log.info("Deleting userId: " + id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDto findOne(String username) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        return user.map(value -> modelMapper.map(value, UserDto.class)).orElse(null);
    }

    @Override
    public UserDto findById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(user -> modelMapper.map(user, UserDto.class)).orElse(null);
    }

    @Override
    public UserDto update(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(userDto.getId());
        if(optionalUser.isPresent()) {
            BeanUtils.copyProperties(userDto, optionalUser.get(), "password");
            userRepository.save(optionalUser.get());
            log.info("Updating user: " + userDto.getUsername());
        }
        return userDto;
    }

}
