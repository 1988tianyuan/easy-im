package com.tianyuan.easyim.gateway.integration;

import com.google.common.collect.Lists;
import com.tianyuan.easyim.gateway.controller.request.LoginRequest;
import com.tianyuan.easyim.common.model.LoginResponse;
import com.tianyuan.easyim.gateway.model.Server;
import com.tianyuan.easyim.gateway.service.server.ServerCollector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginTest {

    @Autowired
    private TestRestTemplate template;

    @MockBean
    private ServerCollector serverCollector;

    @Test
    public void login() {
        given(serverCollector.getServers())
                .willReturn(Lists.newArrayList(new Server("server-1", "localhost:8080")));

        LoginResponse response = template.postForObject(
                "/users/login", new LoginRequest("shen.yuahng"), LoginResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isNotEmpty();
        assertThat(response.getServerAddress()).isEqualTo("localhost:8080");
    }

    @Test
    public void login_withNoServer() {
        given(serverCollector.getServers())
                .willReturn(Lists.newArrayList());

        HttpEntity<LoginRequest> requestHttpEntity = new HttpEntity<>(new LoginRequest("shen.yuahng"));
        ResponseEntity<String> response =
                template.exchange("/users/login", HttpMethod.POST, requestHttpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
