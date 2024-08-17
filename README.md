# Bulk Transfer Application

>
> ***This is a very simple application implemented using Java Spring Boot and SQL as DB***
>

## How to run the app

1. Navigate to the root folder. Execute the following command (Linux):

    ```pwsh
    ./start.sh

    ```

    or Windows:

     ```pwsh
    start.bat

    ```

    It will start the download of two Docker containers, one for the SQL DB and the other for the Spring Boot application. If the second seems to not work properly, try to stop both containers manually Docker and then restarting SQL container first and then the application container.

2. Test the application (use JSON files in **/request-body** as body of the requests):

    First of all is important to check the current balance of the organization that will perform the bulk transfer. In order to get the information of all the organizations store in the DB we can perform a GET request to [localhost:8080/api/accounts](http://localhost:8080/api/accounts). The `/api/accounts` endpoint retrieves bank account information from the database. It operates in two modes depending on the presence of query string parameters:

    1. **Retrieve All Accounts**:  
    - **Method**: `GET`
    - **Description**: When no query parameters are provided, the endpoint returns a list of all bank accounts in the database.

    2. **Filter Accounts by Parameters**:  
    - **Method**: `GET`
    - **Description**: The endpoint supports optional query string parameters—`iban`, `bic`, and `name`. When one or more of these parameters are provided, the endpoint returns accounts that match any of the specified criteria (using an OR logic).

    **Query Parameters:**

    - `iban` (optional): The International Bank Account Number. If provided, the endpoint returns accounts with a matching IBAN.
    - `bic` (optional): The Bank Identifier Code. If provided, the endpoint returns accounts with a matching BIC.
    - `name` (optional): The organization or account holder's name. If provided, the endpoint returns accounts with a matching name.

    So using parameters in query string we can specify the organizations for which we want to retrieve information (those information can be found in **sample1.json** or **sample2.json** files). For example:
    [localhost:8080/api/accounts?iban=IT10474608000005006107XXXXX&bic=OIVUSCLQXXX](http://localhost:8080/api/accounts?iban=IT10474608000005006107XXXXX&bic=OIVUSCLQXXX)  

    Then using **sample1.json** or **sample2.json** as body of a POST request to [localhost:8080/api/customer/transfers](http://localhost:8080/api/customer/transfers) is it possible to test the application, trying to perform bulk transfers from an organization to another one.

    Using **sample1.json** it would execute successfully (updating the DB, in fact performing now a second GET of [localhost:8080/api/accounts](http://localhost:8080/api/accounts) we can see how the organization balance is updated), while using **sample2.json** will have always a negative outcome (credit not sufficient).
