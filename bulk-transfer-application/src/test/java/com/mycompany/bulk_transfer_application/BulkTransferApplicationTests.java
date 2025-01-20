package com.mycompany.bulk_transfer_application;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.bulk_transfer_application.controller.TransferController;
import com.mycompany.bulk_transfer_application.dao.TransferDAO;
import com.mycompany.bulk_transfer_application.dto.Request;
import com.mycompany.bulk_transfer_application.dto.SearchParameters;
import com.mycompany.bulk_transfer_application.dto.Transfer;
import com.mycompany.bulk_transfer_application.entity.BankAccount;
import com.mycompany.bulk_transfer_application.entity.TransferEntity;
import com.mycompany.bulk_transfer_application.service.TransferService;

// Thanks to this annotation Spring will fire up an App Context with the needed beans
@WebMvcTest(TransferController.class)
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

  @Test
  void checkGetWithEmptyNameParam_returnBadRequest() throws Exception {

    mockMvc.perform(get("/api/accounts")
        .contentType("application/json")
        .param("name", ""))
        .andExpect(status().isBadRequest());
  }

  @Test
  void checkCustomerTransfer_updatedAmountCentsCorrectly() throws Exception {

    BankAccount account = new BankAccount(1, "COMPANY 1", "10000000", "IT10474608000005006107XXXXX", "OIVUSCLQXXX");
    int preTransferBalance = Integer.parseInt(account.getBalanceCents());

    List<Transfer> transfers = new ArrayList<>();
    Transfer transfer = new Transfer("14.5", "COUNTERPARTY 1", "CRLYFTTTOU", "EE383680981021245685",
        "Telephone Company");
    transfers.add(transfer);
    int amountCentsInt = (int) Float.parseFloat(transfer.getAmount()) * 100;

    int amountCentsDifference = preTransferBalance - amountCentsInt;
    account.setBalanceCents(String.valueOf(amountCentsDifference));

    Request transferBody = new Request("COMPANY 1", "OIVUSCLQXXX", "IT10474608000005006107XXXXX", transfers);
    List<TransferEntity> responseList = new ArrayList<>();
    TransferEntity responseEntity = new TransferEntity();
    responseEntity.setCounterpartyName(transfer.getCounterpartyName());
    responseEntity.setCounterpartyIban(transfer.getCounterpartyIban());
    responseEntity.setCounterpartyBic(transfer.getCounterpartyBic());
    responseEntity.setAmountCents(amountCentsInt);
    responseEntity.setDescription(transfer.getDescription());
    responseEntity.setBankAccountId(account);

    responseList.add(responseEntity);

    given(transferService.insertTransfers(transferBody)).willReturn(responseList);

    byte[] transferBodyAsBytes = objectMapper.writeValueAsBytes(transferBody);
    MockHttpServletResponse response = mockMvc.perform(post("/api/customer/transfers")
        .contentType("application/json")
        .content(transferBodyAsBytes))
        .andReturn().getResponse();

    assertThat(response.getStatus(), equalTo(HttpStatus.CREATED.value()));
    byte[] responseInByte = response.getContentAsByteArray();

    TransferEntity[] responseArray = objectMapper.readValue(responseInByte, TransferEntity[].class);
    List<TransferEntity> jsonResponse = Arrays.asList(responseArray);

    assertThat(jsonResponse, hasSize(1));

    account = jsonResponse.get(0).getBankAccountId();

    assertEquals(Integer.parseInt(account.getBalanceCents()), amountCentsDifference);

  }
}
