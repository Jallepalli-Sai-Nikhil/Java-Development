package com.example.profilingpractice.service;

import com.example.profilingpractice.entity.User;
import com.example.profilingpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User saveUser(User user){
        return userRepository.save(user);
    }

    /**
     * Upsert behavior: if id is present, update; otherwise try to find by email and update; else create.
     */
    @Override
    public User upsertUser(User user) {
        if (user.getId() != null) {
            // update existing
            User existing = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found for upsert"));
            existing.setName(user.getName());
            existing.setEmail(user.getEmail());
            existing.setCity(user.getCity());
            return userRepository.save(existing);
        }

        if (user.getEmail() != null) {
            return userRepository.findByEmail(user.getEmail())
                    .map(existing -> {
                        existing.setName(user.getName());
                        existing.setCity(user.getCity());
                        existing.setDeleted(false);
                        existing.setDeletedAt(null);
                        return userRepository.save(existing);
                    })
                    .orElseGet(() -> userRepository.save(user));
        }

        return userRepository.save(user);
    }

    @Override
    public User getUser(Long id){
        return userRepository.findByIdAndDeletedFalse(id).orElseThrow(()-> new RuntimeException("User not found"));

    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAllByDeletedFalse();
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
        User existing = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existing.setDeleted(true);
        existing.setDeletedAt(LocalDateTime.now());
        userRepository.save(existing);
    }

    @Override
    public User restoreUser(Long id) {
        User existing = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existing.setDeleted(false);
        existing.setDeletedAt(null);
        return userRepository.save(existing);
    }
}


