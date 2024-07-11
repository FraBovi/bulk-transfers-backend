// TODO: "pojo" stands for?
// POJO is "Plain Old Java Object", a type with no references to a particular framework
package com.mycompany.bulk_transfer_application.pojo;

import lombok.Data;

/**
 * Response represent the response JSON
 */
@Data
public class Response {
	// TODO: what does "code" mean?
	// It is redundant, I add this property in order to display the status of the response also in the body
	private int code;
	private String description;

}
