package org.epam.securitytask.service;

import org.epam.securitytask.entity.Permissions;
import org.epam.securitytask.entity.UserEntity;
import org.epam.securitytask.exception.BlockedUserException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("userDetailsService")
@Transactional
public class CustomUserDetailService implements UserDetailsService {

    private UserService userService;
    private LoginAttemptService loginAttemptService;

    public CustomUserDetailService(UserService userService, LoginAttemptService loginAttemptService) {
        this.userService = userService;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked(email)) {
            throw new BlockedUserException(String.format("User %s is blocked. Try again later.", email));
        }
        UserEntity user = userService.getUserByEmail(email);
        return User.withUsername(user.getEmail()).password(user.getPassword()).authorities(getAuthorities(user)).build();
    }

    private Collection<GrantedAuthority> getAuthorities(UserEntity user){
        List<Permissions> permissions = user.getPermissions();
        return permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());
    }
}
