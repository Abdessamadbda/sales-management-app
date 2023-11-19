package com.fcom.service;

import org.springframework.stereotype.Repository;

import com.fcom.model.sale;
import com.fcom.model.user;

@Repository
public interface UserService {
    public user loginValidation(String username, String password);

    user save(user user);

    user getUserById(Long id);

    public user getSellerInfo(Long sellerId);

    public user updateUser(user user);

    public void deleteUser(Long userId);

}
