package app.task_manager.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public UserDTO create(UserDTO userDTO) {
        if (userRepository.existByEmail(userDTO.getEmail())) {
            throw new UserNotFoundException("User with this email already exist ");
        }
        UserEntity userEntity = userMapper.toEntity(userDTO);
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userEntity.setPassword(encodedPassword);
        UserEntity savedUser = userRepository.saveAndFlush(userEntity);
        return userMapper.toDTO(savedUser);
    }


    public UserDTO login(UserDTO userDTO) {
        UserEntity userEntity = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with this email not found"));

        if (!passwordEncoder.matches(userDTO.getPassword(), userEntity.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return userMapper.toDTO(userEntity);
    }


    public List<UserDTO> getAll() {
        List<UserEntity> users = userRepository.findAll();
        return userMapper.toDTOList(users);
    }

    public UserDTO getById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.toDTO(userEntity);
    }


    @Transactional
    public UserDTO update(Long userId, UserDTO userDTO) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userMapper.updateEntityFromDto(userEntity, userDTO);

        UserEntity updatedUser = userRepository.saveAndFlush(userEntity);

        return userMapper.toDTO(updatedUser);
    }

    @Transactional
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }

        userRepository.deleteById(userId);
    }
    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("No authenticated user found.");
        }

        String username = authentication.getName();

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        return userMapper.toDTO(userEntity);
    }
}
