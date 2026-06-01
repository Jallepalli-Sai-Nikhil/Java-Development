package com.example.profilingpractice.service;

import com.example.profilingpractice.entity.User;
import com.example.profilingpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Override
    public User getUser(Long id){
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));

    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }



    @Override
    public User updateUser(Long id, User user) {

        User existing = getUser(id);

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setCity(user.getCity());

        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}


