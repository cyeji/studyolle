package com.sgyjdev.studyolle.security;

import com.sgyjdev.studyolle.account.AccountRepository;
import com.sgyjdev.studyolle.domain.Account;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByEmail(username);
        if (optionalAccount.isEmpty()) {
            optionalAccount = accountRepository.findByNickname(username);
        }
        if (optionalAccount.isEmpty()) {
            throw new UsernameNotFoundException(username + "invalid credentials");
        }
        Account account = optionalAccount.get();
        return UserAccount.of(account.getEmail(), account.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
