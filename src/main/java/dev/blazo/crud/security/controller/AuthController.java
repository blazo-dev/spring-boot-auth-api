package dev.blazo.crud.security.controller;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.blazo.crud.dto.MessageDTO;
import dev.blazo.crud.security.dto.JwtDTO;
import dev.blazo.crud.security.dto.UserDTO;
import dev.blazo.crud.security.dto.UserLoginDTO;
import dev.blazo.crud.security.entity.Role;
import dev.blazo.crud.security.entity.User;
import dev.blazo.crud.security.enums.RoleName;
import dev.blazo.crud.security.jwt.JwtProvider;
import dev.blazo.crud.security.service.RoleService;
import dev.blazo.crud.security.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("")
    public ResponseEntity<MessageDTO> register(@Valid @RequestBody UserDTO userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return createRequestResponse("Error: Invalid data", HttpStatus.BAD_REQUEST);
        if (userService.existsByUsername(userDto.getUsername()))
            return createRequestResponse("Error: Username is already taken", HttpStatus.BAD_REQUEST);
        if (userService.existsByEmail(userDto.getEmail()))
            return createRequestResponse("Error: Email is already taken", HttpStatus.BAD_REQUEST);

        User user = new User(userDto.getName(), userDto.getUsername(), userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Optional<Role> userRoleName = roleService.findByRoleName(RoleName.ROLE_USER);
        Optional<Role> adminRoleName = roleService.findByRoleName(RoleName.ROLE_ADMIN);

        if (!userRoleName.isPresent())
            return createRequestResponse("Error: User role is not found", HttpStatus.BAD_REQUEST);

        if (!adminRoleName.isPresent())
            return createRequestResponse("Error: Admin role is not found", HttpStatus.BAD_REQUEST);

        roles.add(userRoleName.get());

        if (userDto.getRoles().contains("admin"))
            roles.add(adminRoleName.get());

        user.setRoles(roles);
        userService.save(user);

        return createRequestResponse("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return createRequestResponse("Error: Invalid user", HttpStatus.UNAUTHORIZED);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        JwtDTO jwtDto = new JwtDTO(jwt);
        return new ResponseEntity<>(jwtDto, HttpStatus.ACCEPTED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDTO> refresh(@RequestBody JwtDTO jwtDTO) throws ParseException {
        String token = jwtProvider.refreshToken(jwtDTO);
        JwtDTO jwt = new JwtDTO(token);
        return new ResponseEntity<>(jwt, HttpStatus.OK);

    }

    private ResponseEntity<MessageDTO> createRequestResponse(String message, HttpStatus httpStatus) {
        return new ResponseEntity<>(new MessageDTO(message), httpStatus);
    }

}
