package vn.aptech.java.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.aptech.java.models.CustomUserDetails;
import vn.aptech.java.models.User;
import vn.aptech.java.services.CustomUserDetailService;
import vn.aptech.java.services.UserService;

import java.util.Collection;
import java.util.HashSet;

@Service
public class CustomUserDetailServiceImpl implements CustomUserDetailService , UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Vui lòng nhập đúng thông tin");
        }
        if (user.getStatus() != User.Status.ACTIVE) {
            throw new UsernameNotFoundException("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ quản trị viên để biết thêm chi tiết.");
        }
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
        System.out.println("ROLE: " + user.getRole().toString());
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        return new CustomUserDetails(user, grantedAuthorities);
    }
}
