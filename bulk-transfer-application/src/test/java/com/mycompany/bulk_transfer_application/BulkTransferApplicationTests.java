package com.mycompany.bulk_transfer_application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.bulk_transfer_application.controller.TransferController;
import com.mycompany.bulk_transfer_application.dao.TransferDAO;
import com.mycompany.bulk_transfer_application.dto.SearchParameters;
import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.service.TransferService;

@WebMvcTest(TransferController.class) // Thanks to this annotation Spring will fire up an App Context with the needed
                                      // beans
class BulkTransferApplicationTests {

  // In order to simulate HTTP requests
  @Autowired
  private MockMvc mockMvc;

  // Provided by Spring to map to and from JSON
  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TransferService transferService;

  @MockBean
  private TransferDAO transferDAO;

  @Test
  void checkEndpointAvailable() throws Exception {
    mockMvc.perform(get("/api/accounts")
        .contentType("application/json"))
        .andExpect(status().isOk());
  }

  @Test
  void checkExistingIban_returnOkAndResponse() throws Exception {

    SearchParameters params = new SearchParameters("IT10474608000005006107XXXXX");
    List<BankAccount> getBankAccountsResult = new ArrayList<>();
    getBankAccountsResult
        .add(new BankAccount(1, "COMPANY 1", "10000000", "IT10474608000005006107XXXXX", "OIVUSCLQXXX"));

    given(transferDAO.searchBankAccounts(params)).willReturn(getBankAccountsResult);

    MockHttpServletResponse response = mockMvc.perform(get("/api/accounts")
        .contentType("application/json")
        .param("iban", "IT10474608000005006107XXXXX"))
        .andReturn().getResponse();

    // map String to object
    List<Map<String, Object>> jsonResponse = objectMapper.readValue(response.getContentAsString(), List.class);

    assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
    assertThat(jsonResponse, hasSize(1));
    assertThat(jsonResponse.get(0).get("iban"), is("IT10474608000005006107XXXXX"));
  }

  @Test
  void checkIbanNotValidRequest_returnBadRequest() throws Exception {

    MockHttpServletResponse response = mockMvc.perform(get("/api/accounts")
        .contentType("application/json")
        .param("iban", "abcd"))
        .andReturn().getResponse();

    assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  void checkNotExistingCompanyByName_returnOkAndEmpty() throws Exception {

    SearchParameters params = new SearchParameters();
    params.setName("notexistentcompany");

    List<BankAccount> getBankAccountsResult = new ArrayList<>();
    given(transferDAO.searchBankAccounts(params)).willReturn(getBankAccountsResult);

    MockHttpServletResponse response = mockMvc.perform(get("/api/accounts")
        .contentType("application/json")
        .param("name", "notexistentcompany"))
        .andReturn().getResponse();

    // map String to object
    List<Map<String, Object>> jsonResponse = objectMapper.readValue(response.getContentAsString(), List.class);

    assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
    assertThat(jsonResponse, hasSize(0));
  }

}
