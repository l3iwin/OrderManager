package org.example.service;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO toResponseDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    @Transactional
    public UserDTO createUser(User user) {
        User savedUser = userRepository.save(user);
        return toResponseDTO(savedUser);
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(this::toResponseDTO);
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado com o ID: " + id));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        User updatedUser = userRepository.save(user);

        return toResponseDTO(updatedUser);
    }
}
