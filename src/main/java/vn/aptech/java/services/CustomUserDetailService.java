package vn.aptech.java.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
