package com.markolo.library_manager.service.user;

import com.markolo.library_manager.exception.AlreadyExistsException;
import com.markolo.library_manager.model.User;
import com.markolo.library_manager.repository.LoanRepository;
import com.markolo.library_manager.repository.MembershipRepository;
import com.markolo.library_manager.repository.UserRepository;
import com.markolo.library_manager.request.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.markolo.library_manager.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {



    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final LoanRepository loanRepository;

    private final MembershipRepository membershipRepository;


    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setName(request.getName());
                    user.setRole(request.getRole());
                    user.setUsername(request.getUsername());
                    return userRepository.save(user);
                }).orElseThrow(()-> new AlreadyExistsException("User: "+request.getEmail()+" already exists!"));
    }

    @Override
    public User updateUser(CreateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setEmail(request.getEmail());
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
            existingUser.setName(request.getName());
            existingUser.setRole(request.getRole());
            existingUser.setUsername(request.getUsername());
            return userRepository.save(existingUser);
        }).orElseThrow(()->new ResourceNotFoundException("User not found!"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long userId) {
        User user =userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        membershipRepository.deleteByUserId(userId);

        loanRepository.deleteByUserId(userId);
        userRepository.delete(user);


    }
}
