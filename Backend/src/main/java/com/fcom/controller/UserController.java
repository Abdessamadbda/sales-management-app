package com.fcom.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.fcom.model.LoginRequest;
import com.fcom.model.UserDTO;
import com.fcom.model.user;
import com.fcom.repository.UserRepository;
import com.fcom.security.JWTGenerator;
import com.fcom.security.SecurityConstants;
import com.fcom.service.SalesService;
import com.fcom.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Configuration
public class UserController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private UserService userservice;
    @Autowired
    private JWTGenerator jwtGenerator;

    @PostMapping("/seller/login")
    public ResponseEntity<UserLoginResponse> authLogin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        user user = userservice.loginValidation(username, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserLoginResponse(null, null));
        }

        String token = jwtGenerator.generateToken(user.getUsername(), "user");
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getNom_complet(), user.getVille(),
                user.getPhone(), user.getAgence());
        return ResponseEntity.ok(new UserLoginResponse(userDTO, token));
    }

    private UserDTO createUserDTO(user user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getNom_complet(), user.getVille(), user.getPhone(),
                user.getAgence());
    }

    public class UserLoginResponse {
        private UserDTO user;
        private String token;

        public UserLoginResponse(UserDTO user, String token) {
            this.user = user;
            this.token = token;
        }

        public UserDTO getUser() {
            return user;
        }

        public void setUser(UserDTO user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
    }

    @PostMapping("/user/save")
    public ResponseEntity<user> saveUser(@RequestBody user user) {
        user savedUser = userService.save(user);
        if (savedUser != null) {
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/admin/sellers")
    public List<user> getAllSales() {
        return userRepository.findAll();
    }

    @GetMapping("/seller/sellers/{sellerId}")
    public List<user> getSalesByVille(@PathVariable Long sellerId) {
        user seller = userService.getSellerInfo(sellerId);
        List<user> listUsers = userRepository.findByAgence(seller.getAgence());
        Iterator<user> iterator = listUsers.iterator();

        while (iterator.hasNext()) {
            user user = iterator.next();
            String user1 = (user.getNom_complet() != null) ? String.valueOf(user.getNom_complet()) : "";

            if (user1.equals(seller.getNom_complet())) {
                iterator.remove();
            }
        }

        return listUsers;
    }

}
