package com.example.hogwartsartifactsonline.hogwartsuser;

import com.example.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public HogwartsUser getById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public List<HogwartsUser> getAllUsers() {
        return userRepository.findAll();
    }

    public HogwartsUser save(HogwartsUser user) {
        // before saving, encode the password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return this.userRepository.findByUsername(username)
                .map(user -> new MyUserPrincipal(user))
                .orElseThrow(() -> new UsernameNotFoundException("User not find with username: " + username));

    }
}
