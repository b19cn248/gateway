package com.lawman.gateway.authserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawman.gateway.authclient.request.account.ChangePasswordRequest;
import com.lawman.gateway.authserver.configuration.GatewayConfigurationTest;
import com.lawman.gateway.authserver.exception.account.AccountNotFoundException;
import com.lawman.gateway.authserver.exception.account.PasswordIncorrectException;
import com.lawman.gateway.authserver.service.AccountService;
import com.lawman.gateway.authserver.service.MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;
import java.util.stream.Stream;

import static com.lawman.gateway.authclient.constant.AuthClientConstants.ValidationMessage.CONFIRM_PASSWORD_NOT_MATCH;
import static com.lawman.gateway.authclient.constant.AuthClientConstants.ValidationMessage.WRONG_FORMAT_PASSWORD;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CommonConstants.SUCCESS;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.ExceptionMessage.ACCOUNT_NOT_FOUND_EXCEPTION;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.ExceptionMessage.PASSWORD_INCORRECT_EXCEPTION;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@ContextConfiguration(classes = GatewayConfigurationTest.class)
class AccountControllerTests {

  private static final String END_POINT_PATH = "/api/v1/accounts";

  @MockBean
  private MessageService messageService;

  @Autowired
  private AccountController accountController;

  @MockBean
  private AccountService accountService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  private static final String ID = UUID.randomUUID().toString();


  @Test
  void changePassword_WhenInputValid_return200OK() throws Exception {

    ChangePasswordRequest request = mockRequest();

    String bodyContent = objectMapper.writeValueAsString(mockRequest());

    Mockito.when(messageService.getMessage(SUCCESS, "en"))
          .thenReturn("success");

    MvcResult result = mockMvc.perform(post(END_POINT_PATH + "/" + ID + "/change-password")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(bodyContent))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn();

    String actual = result.getResponse().getContentAsString();
    String expect = objectMapper.writeValueAsString(accountController.changePassword(ID, request, "en"));

    Assertions.assertEquals(actual, expect);
  }

  @Test
  void changePassword_WhenIDNotFound_thrownAccountNotFound() throws Exception {

    ChangePasswordRequest request = mockRequest();

    String bodyContent = objectMapper.writeValueAsString(request);

    Mockito.doThrow(new AccountNotFoundException()).when(accountService).changePassword(ID, request);

    mockMvc.perform(post(END_POINT_PATH + "/" + ID + "/change-password")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(bodyContent))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code", is(ACCOUNT_NOT_FOUND_EXCEPTION)))
          .andDo(print())
          .andReturn();
  }


  @Test
  void changePassword_WhenPasswordIncorrect_thrownPasswordIncorrect() throws Exception {

    ChangePasswordRequest request = mockRequest();

    String bodyContent = objectMapper.writeValueAsString(request);

    Mockito.doThrow(new PasswordIncorrectException()).when(accountService).changePassword(ID, request);

    mockMvc.perform(post(END_POINT_PATH + "/" + ID + "/change-password")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.code", is(PASSWORD_INCORRECT_EXCEPTION)))
          .andDo(print())
          .andReturn();
  }

  @Test
  void changePassword_WhenConfirmPasswordNotMatch_returnBadRequest() throws Exception {

    ChangePasswordRequest request = mockRequest();
    request.setConfirmPassword("Hieu12345@");

    String bodyContent = objectMapper.writeValueAsString(request);

    Mockito.doThrow(new PasswordIncorrectException()).when(accountService).changePassword(ID, request);

    mockMvc.perform(post(END_POINT_PATH + "/" + ID + "/change-password")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.detail.confirmPassword", is(CONFIRM_PASSWORD_NOT_MATCH)))
          .andDo(print())
          .andReturn();
  }

  @ParameterizedTest
  @MethodSource("invalidPasswordData")
  void create_WhenInvalidPassword_Return400BadRequest(String password) throws Exception {
    ChangePasswordRequest request = mockRequest();
    request.setNewPassword(password);

    String bodyContent = objectMapper.writeValueAsString(request);

    MvcResult result = mockMvc.perform(post(END_POINT_PATH + "/" + ID + "/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.detail.newPassword", is(WRONG_FORMAT_PASSWORD)))
          .andDo(print())
          .andReturn();

    assertEquals(400, result.getResponse().getStatus());

  }

  static Stream<String> invalidPasswordData() {
    return Stream.of(
          "Hieu230708",
          "MinhHieu",
          "HIEU1234@",
          "hieu1234@",
          null
    );
  }

  private ChangePasswordRequest mockRequest() {
    return ChangePasswordRequest.builder()
          .password("Hieu230708@")
          .newPassword("Hieu1234@")
          .confirmPassword("Hieu1234@")
          .build();
  }
}
