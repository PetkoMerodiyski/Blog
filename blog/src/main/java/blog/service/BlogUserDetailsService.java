package blog.service;
import blog.entity.User;
import java.util.*;
import java.util.stream.Collectors;

import blog.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by user on 14.8.2017 г..
 */

@Service("blogUserDetailsService")
public class BlogUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public BlogUserDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user=userRepository.findByEmail(email);

        if(user==null){
            throw new UsernameNotFoundException("Invalid user");
        }else{
            Set<GrantedAuthority> grantedAuthorities=user.getRoles().stream()
                    .map(role->new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toSet());
            return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),grantedAuthorities);
        }
    }
}
