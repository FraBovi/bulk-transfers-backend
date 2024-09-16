package com.mycompany.bulk_transfer_application;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.bulk_transfer_application.controller.TransferController;
import com.mycompany.bulk_transfer_application.dao.TransferDAO;
import com.mycompany.bulk_transfer_application.service.TransferService;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TransferController.class) //Thanks to this annotation Spring will fire up an App Context with the needed beans
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

}
