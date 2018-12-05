package com.example.demo.start;

import com.example.demo.client.Client;
import com.example.demo.client.ClientRepository;
import com.example.demo.user.Role;
import com.example.demo.user.RoleRepository;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

@DataJpaTest
@RunWith(SpringRunner.class)
public class StartTest {
    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private ClientRepository clientRepository;

    @Test
    @Ignore
    public void testCreateUser() {
        Role role = new Role("USER");
        roleRepository.save(role);
        User user = new User("user", "password", true, Collections.singletonList(role));
        userRepository.save(user);
        List<User> all = userRepository.findAll();
        assertTrue(all.size() > 0);
    }

    @Test
    @Ignore
    public void testCreateClient() {
        Client client = new Client("client", "secret", "app",
                "password,authorization_code,refresh_token", "http://localhost:4200",
                7200, 604800);
        clientRepository.save(client);
        List<Client> all = clientRepository.findAll();
        assertTrue(all.size() > 0);
    }
}
