package org.epam.securitytask.service;

import org.epam.securitytask.entity.UserEntity;
import org.epam.securitytask.exception.UserNotFoundException;
import org.epam.securitytask.jpa.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }

    public UserEntity getUserByEmail(String email) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        return userOptional.orElseThrow(() -> new UserNotFoundException(String.format("User %s does not exist.", email)));
    }
}
