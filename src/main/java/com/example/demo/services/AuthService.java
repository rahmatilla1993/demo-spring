package com.example.demo.services;

import com.example.demo.enums.Role;
import com.example.demo.models.Person;
import com.example.demo.payload.LoginDto;
import com.example.demo.payload.RegisterDto;
import com.example.demo.repositories.PersonRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.security.PersonSecurity;
import com.example.demo.util.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class AuthService {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(PersonRepository personRepository,
                       ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ApiResponse register(RegisterDto registerDto) {
        Optional<Person> optionalPerson = personRepository.findByUsername(registerDto.getUsername());
        if (optionalPerson.isPresent()) {
            return new ApiResponse("Bunday username bor", false);
        }
        Person person = modelMapper.map(registerDto, Person.class);
        person.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        person.setRole(Role.ROLE_USER);
        Person savedPerson = personRepository.save(person);
        return new ApiResponse("Person saqlandi", true, savedPerson);
    }

    public ApiResponse login(LoginDto loginDto) {
        String password = loginDto.getPassword();
        String username = loginDto.getUsername();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = jwtTokenProvider.createToken(username);
            return new ApiResponse("Login success", true, token);
        } catch (BadCredentialsException e) {
            return new ApiResponse(e.getMessage(), false);
        }
    }
}
