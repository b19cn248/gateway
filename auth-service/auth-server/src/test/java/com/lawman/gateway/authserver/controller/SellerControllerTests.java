package com.lawman.gateway.authserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawman.gateway.authclient.request.seller.SellerRequest;
import com.lawman.gateway.authclient.response.seller.SellerResponse;
import com.lawman.gateway.authserver.configuration.GatewayConfigurationTest;
import com.lawman.gateway.authserver.facade.SellerFacadeService;
import com.lawman.gateway.authserver.service.MessageService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;
import java.util.stream.Stream;

import static com.lawman.gateway.authclient.constant.AuthClientConstants.ValidationMessage.*;
import static com.lawman.gateway.authserver.constanst.GatewayServerConstants.CustomerMessage.CREATE_SELLER_SUCCESS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SellerController.class)
@ContextConfiguration(classes = GatewayConfigurationTest.class)
class SellerControllerTests {

  private static final String END_POINT_PATH = "/api/v1/sellers";

  private static final String UID = UUID.randomUUID().toString();

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SellerFacadeService sellerFacadeService;

  @MockBean
  private MessageService messageService;

  @Autowired
  private SellerController sellerController;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void create_InputValid_return201CREATED() throws Exception {
    SellerRequest request = mockRequest();
    SellerResponse response = mockResponse();

    String bodyContent = objectMapper.writeValueAsString(request);

    Mockito.when(sellerFacadeService.create(request)).thenReturn(response);
    Mockito.when(messageService.getMessage(CREATE_SELLER_SUCCESS, "en"))
          .thenReturn("create seller successfully");

    MvcResult mvcResult = mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                .content(bodyContent))
          .andExpect(status().isCreated())
          .andDo(print())
          .andReturn();

    String actual = mvcResult.getResponse().getContentAsString();
    String expect = objectMapper.writeValueAsString(sellerController.create(request, "en"));

    assertEquals(actual, expect);
  }

  @ParameterizedTest
  @MethodSource("invalidPasswordData")
  void create_WhenInvalidPassword_Return400BadRequest(String password) throws Exception {
    SellerRequest request = mockRequest();
    request.setPassword(password);

    String bodyContent = objectMapper.writeValueAsString(request);

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.detail.password", is(WRONG_FORMAT_PASSWORD)))
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


  @Test
  void create_WhenConfirmPasswordDoesNotMatch_Return400BadRequest() throws Exception {

    SellerRequest request = mockRequest();
    request.setConfirmPassword("Hieu230709@");


    String bodyContent = objectMapper.writeValueAsString(request);

    mockMvc.perform(post(END_POINT_PATH).contentType("application/json").
                content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.detail.confirmPassword", is(CONFIRM_PASSWORD_NOT_MATCH)))
          .andDo(print())
          .andReturn();
  }

  @Test
  void create_WhenEmailInvalid_Return400BadRequest() throws Exception {

    SellerRequest request = mockRequest();
    request.setEmail("Email.com");

    String bodyContent = objectMapper.writeValueAsString(request);

    mockMvc.perform(post(END_POINT_PATH).contentType("application/json").
                content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.detail.email", is(INVALID_EMAIL)))
          .andDo(print())
          .andReturn();
  }

  @ParameterizedTest
  @MethodSource("invalidUsernameData")
  void create_WhenInvalidUsername_Return400BadRequest(String username) throws Exception {
    SellerRequest request = mockRequest();
    request.setUsername(username);

    String bodyContent = objectMapper.writeValueAsString(request);

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.detail.username", is(INVALID_USERNAME)))
          .andDo(print())
          .andReturn();

    assertEquals(400, result.getResponse().getStatus());
  }

  static Stream<String> invalidUsernameData() {
    return Stream.of(
          null,
          "AB",
          "HelloWorld4141413132131321321",
          "AB@@@1232132"
    );
  }


  @Test
  void create_WhenWrongDateFormat_Return400BadRequest() throws Exception {

    SellerRequest request = mockRequest();
    request.setDob("22-12-2001");

    String bodyContent = objectMapper.writeValueAsString(request);

    mockMvc.perform(post(END_POINT_PATH).contentType("application/json").
                content(bodyContent))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.data.detail.dob", is(INVALID_DATE_FORMAT)))
          .andDo(print())
          .andReturn();
  }

  private SellerRequest mockRequest() {
    return SellerRequest.builder()
          .brandName("ShopSport")
          .username("hieuPTIT")
          .password("Hieu230708@")
          .confirmPassword("Hieu230708@")
          .fullName("Nguyen Minh Hieu")
          .phoneNumber("0987975814")
          .email("hieunm123.ptit@gmail.com")
          .dob("23/08/2001")
          .build();
  }

  private SellerResponse mockResponse() {
    return SellerResponse.builder()
          .id(UID)
          .brandName("ShopSport")
          .username("hieuPTIT")
          .fullName("Nguyen Minh Hieu")
          .phoneNumber("0987654321")
          .email("hieunm123.ptit@gmail.com")
          .dob("23/08/2001")
          .build();
  }
}
