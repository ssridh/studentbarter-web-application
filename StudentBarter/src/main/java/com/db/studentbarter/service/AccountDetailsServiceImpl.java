package com.db.studentbarter.service;

import com.db.studentbarter.model.Account;
import com.db.studentbarter.model.Role;
import com.db.studentbarter.model.User;
import com.db.studentbarter.repository.AccountRepository;
import com.db.studentbarter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

public class AccountDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Account account = accountRepository.findByUsername(username);
        User user = userRepository.findByAccount(account);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        
        return new org.springframework.security.core.userdetails.User(account.getUsername(), account.getPassword(), grantedAuthorities);
    }
}
