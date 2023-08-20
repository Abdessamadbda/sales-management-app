// Step 5: AuthenticationController.java

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        // Handle login request and generate authentication token
        // You can use JWT or any other authentication mechanism here
    }

    @GetMapping("/user")
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Retrieve the user from the database based on the username
        User user = userRepository.findByUsername(username);
        return user;
    }

    @GetMapping("/dashboard")
    public String getDashboard() {
        // Return the dashboard data
    }

    @GetMapping("/logout")
    public void logout() {
        // Handle logout logic
    }
}

