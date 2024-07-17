package com.mycompany.bulk_transfer_application.pojo;

import lombok.Data;

/**
 * Response represent the response JSON
 */
@Data
public class Response {
    // TODO: what does "code" mean?
    // It is redundant, I add this property in order to display the status of the
    // response also in the body
    // Please have a look at this:
    // https://www.toptal.com/java/spring-boot-rest-api-error-handling
    /*
     * {
     * "apierror": {
     * "status": "NOT_FOUND",
     * "timestamp": "22-07-2022 06:20:19",
     * "message": "Bird was not found for parameters {id=2}"
     * }
     * }
     */
    // FIXME: get rid of this "code" and stick to a more consistent error handling.
    // Define your dictionary of errors that you're expecting from your app.
    private int code;
    private String description;

}
