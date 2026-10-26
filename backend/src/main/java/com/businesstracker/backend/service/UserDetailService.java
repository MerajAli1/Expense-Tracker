package com.businesstracker.backend.service;

import com.businesstracker.backend.UserDetailRepository;
import com.businesstracker.backend.Userdetail;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;

    private static final String DEFAULT_USER_ID = "DEFAULT_USER";

    public UserDetailService(UserDetailRepository userDetailRepository) {
        this.userDetailRepository = userDetailRepository;
    }

    public Userdetail getOrCreateUser() {
        return userDetailRepository.findById(DEFAULT_USER_ID)
                .orElseGet(() -> {
                    Userdetail newUser = new Userdetail(DEFAULT_USER_ID, "Business Owner", 150000.0, 75000.0);
                    return userDetailRepository.save(newUser);
                });
    }

    public Userdetail updateBalances(double businessBalance, double savingsBalance) {
        Userdetail user = getOrCreateUser();
        user.setBusinessBalance(businessBalance);
        user.setSavingsBalance(savingsBalance);
        user.setUpdatedAt(LocalDateTime.now());
        return userDetailRepository.save(user);
    }

    public Userdetail save(Userdetail user) {
        return userDetailRepository.save(user);
    }
}
