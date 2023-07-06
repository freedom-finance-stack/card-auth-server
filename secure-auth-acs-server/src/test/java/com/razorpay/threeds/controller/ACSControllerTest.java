package com.razorpay.threeds.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.razorpay.acs.contract.AREQ;
import com.razorpay.threeds.service.AuthenticationService;
import com.razorpay.threeds.utils.Util;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ACSController.class)
@Tag("Controller")
public class ACSControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private AuthenticationService authenticationService;

  @Test
  void aReq() throws Exception {

    mockMvc
        .perform(
            post("/v1/transaction/auth-request")
                .headers(getDefaultHeaders())
                .content(Util.toJson(new AREQ())))
        .andExpect(status().isBadRequest());
  }

  //    @Test
  //    void aReq() throws Exception {
  //        mockMvc.perform(post("/auth-request")).andExpect(status().isOk());
  //    }
  protected HttpHeaders getDefaultHeaders() {

    HttpHeaders headers = new HttpHeaders();
    headers.add(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    return headers;
  }
}
