package com.example.LostAnimalsApp.service.implementation;

import com.example.LostAnimalsApp.dto.AuthDTO;
import com.example.LostAnimalsApp.dto.UserDTO;
import com.example.LostAnimalsApp.enums.Role;
import com.example.LostAnimalsApp.exception.ResourceNotFoundException;
import com.example.LostAnimalsApp.model.User;
import com.example.LostAnimalsApp.repository.UserRepository;
import com.example.LostAnimalsApp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_RO_PHONE_NUMBER = Pattern.compile("^(407)(0[1-9]|[2-8][0-9]|9[0-1])(\\d{6})$");

    @Override
    @Transactional
    public UserDTO createUser(final AuthDTO authUser) {
        if (checkFields(authUser)) {
            var user = User.builder()
                    .username(authUser.getUsername())
                    .fullName(authUser.getFullName())
                    .email(authUser.getEmail())
                    .phoneNumber(authUser.getPhoneNumber())
                    .password(passwordEncoder.encode(authUser.getPassword()))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            return modelMapper.map(user, UserDTO.class);
        } else throw new ResourceNotFoundException("User cannot be created!");
    }

    @Override
    @Transactional
    public UserDTO getUserByUsername(final String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with username " + username);
        }
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    @Transactional
    public UserDTO updateUser(final AuthDTO user) {
        User userToUpdate = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (userToUpdate == null) {
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with username " + user.getUsername());
        }
        if (checkFields(user)) {
            userToUpdate = User.builder()
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .role(Role.USER)
                    .build();
            userRepository.save(userToUpdate);
            return modelMapper.map(userToUpdate, UserDTO.class);
        } else throw new ResourceNotFoundException("User cannot be updated!");
    }


    private static boolean validate(final String stringToValidate, final Pattern pattern) {
        Matcher matcher = pattern.matcher(stringToValidate);
        return matcher.matches();
    }

    private boolean checkFields(AuthDTO user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            System.out.println("username");
            return false;
        }
        if (user.getEmail() == null || user.getEmail().isBlank() ||
                !validate(user.getEmail(), VALID_EMAIL_ADDRESS_REGEX)) {
            System.out.println("email");
            return false;
        }
        if (user.getPassword() == null || user.getPassword().isBlank() ||
                !user.getPassword().equals(user.getConfirmedPassword()) || !validate(user.getPassword(), VALID_PASSWORD_REGEX)) {
            System.out.println("password");
            return false;
        }
        if (user.getPhoneNumber() == null || !validate(user.getPhoneNumber().toString(), VALID_RO_PHONE_NUMBER)) {
            System.out.println("number");

            return false;
        }
//        if (user.getRole() == null || !Arrays.stream(Role.values()).toList().contains(user.getRole())) {
//            return false;
//        }
        return true;
    }
}
