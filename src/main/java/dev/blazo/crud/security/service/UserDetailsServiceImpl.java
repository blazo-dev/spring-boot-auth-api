package dev.blazo.crud.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.blazo.crud.security.entity.PrimaryUser;
import dev.blazo.crud.security.entity.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userService.getByUsername(username);

        if (!userOptional.isPresent())
            throw new UsernameNotFoundException("User not found with username: " + username);

        User user = userOptional.get();
        return PrimaryUser.build(user);
    }

}
