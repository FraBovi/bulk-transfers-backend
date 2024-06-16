package com.mycompany.bulk_transfer_application.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.bulk_transfer_application.pojo.Request;
import com.mycompany.bulk_transfer_application.pojo.Response;

@RestController
@RequestMapping("/api")
public class TransferController {
	
	@PostMapping("/transfer")
	public Response bulkTransfer(@RequestBody Request transferRequest) {
		return null;
	}

}
