package com.example.demo.endpoint;

import com.example.demo.DemoApplication;
import com.example.demo.DemoApplicationTests;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;

/**
 * 测试获取token
 * Package com.example.4sbtest.endpoint
 * Module sb-test
 * Project sb-test
 * Email 253498229@qq.com
 * Created on 2018/6/4 下午9:34
 *
 * @author wangbin
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {DemoApplication.class, DemoApplicationTests.class})
@RunWith(SpringRunner.class)
@ActiveProfiles("integration-test")
public class TokenTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String CLIENT = "client";
    private static final String SECRET = "secret";
    private static final String URL_PREFIX = "http://localhost:{port}";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";

    /**
     * 通过authorization_code方式获取Token
     */
    @Test
    public void codeType() throws IOException {
        String url = URL_PREFIX + "/oauth/authorize?client_id={client_id}&response_type={response_type}&redirect_uri={redirect_uri}";

        Map<String, Object> param = new HashMap<>();
        param.put("port", port);
        param.put("username", USERNAME);
        param.put("password", PASSWORD);
        param.put("client_id", CLIENT);
        param.put("response_type", "code");
        param.put("redirect_uri", "http://www.baidu.com");

        ResponseEntity<String> response;
        response = restTemplate.withBasicAuth(USERNAME, PASSWORD)
                .postForEntity(url, null, String.class, param);
        assertEquals(response.getStatusCode(), OK);

        String cookie = Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        param.put("user_oauth_approval", true);
        param.put("scope.app", true);
        param.put("authorize", "Authorize");
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(headers);
        response = restTemplate.withBasicAuth(USERNAME, PASSWORD)
                .postForEntity(url + "&user_oauth_approval=true&scope.app=true&authorize=Authorize", httpEntity, String.class, param);
        assertEquals(response.getStatusCode(), FOUND);
        assertNotNull(response.getHeaders().getLocation());

        String query = response.getHeaders().getLocation().getQuery();
        response = restTemplate.withBasicAuth(CLIENT, SECRET)
                .postForEntity(URL_PREFIX + "/oauth/token"
                        + "?" + query + "&grant_type=authorization_code&redirect_uri={redirect_uri}", null, String.class, param);
        assertEquals(response.getStatusCode(), OK);
        String body = response.getBody();
        assertNotNull(body);
        assertTrue(testResult(body));
    }

    /**
     * 通过password方式获取Token
     */
    @Test
    public void passwordType() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("port", port);
        map.put("username", USERNAME);
        map.put("password", PASSWORD);
        ResponseEntity<String> response = restTemplate.withBasicAuth(CLIENT, SECRET)
                .postForEntity(URL_PREFIX + "/oauth/token" +
                        "?grant_type=password&username={username}&password={password}", null, String.class, map);
        assertEquals(response.getStatusCode(), OK);
        String body = response.getBody();
        assertNotNull(body);
        assertTrue(testResult(body));
    }

    /**
     * 测试Token是否正确
     */
    private boolean testResult(String body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(body, HashMap.class);
        String access_token = (String) map.get("access_token");
        String userInfoJson = new String(Base64.getDecoder().decode(access_token.split("\\.")[1]), StandardCharsets.UTF_8);
        Map userInfo = mapper.readValue(userInfoJson, HashMap.class);
        return USERNAME.equals(userInfo.get("user_name"));
    }
}
