package com.fcom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fcom.controller.UserController.UserLoginResponse;
import com.fcom.model.LoginRequest;
import com.fcom.model.UserDTO;
import com.fcom.model.admin;
import com.fcom.model.user;
import com.fcom.security.JWTGenerator;
import com.fcom.service.AdminService;
import com.fcom.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Configuration
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private JWTGenerator jwtGenerator;

    @PostMapping("/admin/alogin")
    public ResponseEntity<AdminLoginResponse> authLogin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        admin admin = adminService.loginValidation1(username, password);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AdminLoginResponse(null, null));
        }

        String token = jwtGenerator.generateToken(admin.getUsername(), "admin");
        return ResponseEntity.ok(new AdminLoginResponse(admin, token));
    }

    public class AdminLoginResponse {
        private admin admin;
        private String token;

        public AdminLoginResponse(admin admin, String token) {
            this.admin = admin;
            this.token = token;
        }

        public admin getAdmin() {
            return admin;
        }

        public void setAdmin(admin admin) {
            this.admin = admin;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
        this.jwtGenerator = jwtGenerator;
    }
}
