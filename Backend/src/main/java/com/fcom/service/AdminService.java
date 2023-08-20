package com.fcom.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.fcom.model.admin;
import com.fcom.model.user;

@Repository

public interface AdminService {
	public admin loginValidation1(String username, String password);
    admin getAdminById(Long id);


}
