package com.lawman.gateway.authserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawman.gateway.authclient.request.auth.LoginRequest;
import com.lawman.gateway.authclient.response.auth.LoginResponse;
import com.lawman.gateway.authserver.configuration.GatewayConfigurationTest;
import com.lawman.gateway.authserver.exception.account.PasswordIncorrectException;
import com.lawman.gateway.authserver.exception.account.UsernameNotFoundException;
import com.lawman.gateway.authserver.facade.AuthenticationFacadeService;
import com.lawman.gateway.authserver.service.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CommonConstants.SUCCESS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = GatewayConfigurationTest.class)
class AuthControllerTests {

  private static final String END_POINT_PATH = "/api/v1/auth/login";
  private static final String USER_NAME = "hieuPTIT";
  private static final String PASSWORD = "Hieu230708@";
  private static final String ACCESS_TOKEN = "mock_access_token";
  private static final String REFRESH_TOKEN = "mock_refresh_toke";

  private static final String NOT_FOUND_CODE = "com.gateway.server.exception.account.UsernameNotFoundException";

  private static final String INCORRECT_PASSWORD_CODE = "com.gateway.server.exception.account.PasswordIncorrectException";

  @MockBean
  private AuthenticationFacadeService authenticationFacadeService;

  @MockBean
  private MessageService messageService;

  @Autowired
  private AuthController authController;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void login_WhenInputValid_return200OK() throws Exception {
    LoginResponse loginResponse = new LoginResponse(
          ACCESS_TOKEN,
          REFRESH_TOKEN
    );

    LoginRequest loginRequest = new LoginRequest(
          USER_NAME,
          PASSWORD
    );

    String bodyContent = objectMapper.writeValueAsString(loginRequest);

    Mockito.when(authenticationFacadeService.login(USER_NAME, PASSWORD)).thenReturn(loginResponse);
    Mockito.when(messageService.getMessage(SUCCESS, "en"))
          .thenReturn("success");

    MvcResult mvcResult = mockMvc.perform(post(END_POINT_PATH).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(bodyContent))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn();

    String actual = mvcResult.getResponse().getContentAsString();
    String expect = objectMapper.writeValueAsString(authController.login(loginRequest, "en"));

    assertEquals(actual, expect);

  }

  @Test
  void login_WhenUserNotFound_return404NOTFOUND() throws Exception {
    Mockito.when(authenticationFacadeService.login(USER_NAME, PASSWORD)).thenThrow(new UsernameNotFoundException());

    LoginRequest loginRequest = new LoginRequest(
          USER_NAME,
          PASSWORD
    );

    String bodyContent = objectMapper.writeValueAsString(loginRequest);

    mockMvc.perform(post(END_POINT_PATH).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(bodyContent))
          .andExpect(status().isNotFound())
          .andDo(print())
          .andExpect(jsonPath("$.data.code", is(NOT_FOUND_CODE)));
  }

  @Test
  void login_WhenPasswordIncorrect_return400BADREQUEST() throws Exception {
    Mockito.when(authenticationFacadeService.login(USER_NAME, PASSWORD)).thenThrow(new PasswordIncorrectException());

    LoginRequest loginRequest = new LoginRequest(
          USER_NAME,
          PASSWORD
    );

    String bodyContent = objectMapper.writeValueAsString(loginRequest);

    mockMvc.perform(post(END_POINT_PATH).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(bodyContent))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andExpect(jsonPath("$.data.code", is(INCORRECT_PASSWORD_CODE)));
  }

}