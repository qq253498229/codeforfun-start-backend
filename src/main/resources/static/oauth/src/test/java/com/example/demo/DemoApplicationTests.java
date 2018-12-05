package com.example.demo;

import com.example.demo.client.Client;
import com.example.demo.client.ClientRepository;
import com.example.demo.user.Role;
import com.example.demo.user.RoleRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integration-test")
public class DemoApplicationTests {
    @Test
    public void contextLoads() {
    }
    @Bean
    CommandLineRunner runner(UserRepository userRepository, RoleRepository roleRepository, ClientRepository clientRepository) {
        return args -> {
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            String roleName = "USER";
            Role role = roleRepository.findByName(roleName);
            if (role == null) {
                role = new Role(roleName);
                roleRepository.save(role);
            }

            String userName = "user";
            User user = userRepository.loadUserByUsername(userName);
            if (user == null) {
                user = new User(userName, encoder.encode("password"), true, Collections.singletonList(role));
                userRepository.save(user);
            }

            String clientId = "client";
            Client client = clientRepository.loadClientByClientId(clientId);
            if (client == null) {
                client = new Client(clientId, encoder.encode("secret"), "app",
                        "password,authorization_code,refresh_token", "http://www.baidu.com",
                        7200, 604800);
                clientRepository.save(client);
            }
        };
    }
}
