package com.example.hogwartsartifactsonline.hogwartsuser;

import com.example.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public HogwartsUser getById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public List<HogwartsUser> getAllUsers() {
        return userRepository.findAll();
    }

    public HogwartsUser save(HogwartsUser user) {
        return userRepository.save(user);
    }

    public HogwartsUser update(Integer id, HogwartsUser updated) {
        return userRepository.findById(id).map(existing -> {
            existing.setUsername(updated.getUsername());
            existing.setPassword(updated.getPassword());
            existing.setRoles(updated.getRoles());
            existing.setEnabled(updated.isEnabled());
            return userRepository.save(existing);
        }).orElseThrow(() -> new ObjectNotFoundException("user", id));
    }

    public void delete(Integer userId) {
        userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        userRepository.deleteById(userId);
    }
}
