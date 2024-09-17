# Bulk Transfer Application

# TODO: beautify this readme.md file by making it compliance with the Markdown syntax rules.

> ***This is a very simple application implemented using Java Spring Boot and SQL as DB***

## How to run the app

1. Navigate to the root folder. Execute the following command (Linux):

    ```bash
    ./start.sh
    ```

    or on Windows:

    ```cmd
    start.bat
    ```

    This will start the download of two Docker containers, one for the SQL DB and the other for the Spring Boot application. If the second container doesn't work properly, try stopping both containers manually in Docker, then restart the SQL container first, followed by the application container.

2. Test the application:

    ### GET Request

    First, it's important to check the current balance of the organization that will perform the bulk transfer.

    To retrieve information of all the organizations stored in the DB, you can perform a GET request to [localhost:8080/api/accounts](http://localhost:8080/api/accounts). The `/api/accounts` endpoint retrieves bank account information from the database. It operates in two modes depending on the presence of query string parameters:

    1. **Retrieve All Accounts**:  
        When no query parameters are provided, the endpoint returns a list of all bank accounts in the database.

    2. **Filter Accounts by Parameters**:  
        The endpoint supports optional query string parameters `iban`, `bic`, and `name`. When one or more of these parameters are provided, the endpoint returns accounts that match any of the specified criteria (it uses by default AND logic, but it can be specified by adding `type` as parameter).

        **Query Parameters:**
        - `iban` : The International Bank Account Number. If provided, the endpoint returns accounts with a matching IBAN.
        - `bic` : The Bank Identifier Code. If provided, the endpoint returns accounts with a matching BIC.
        - `name` : The organization or account holder's name. If provided, the endpoint returns accounts with a matching name.
        - `type` : It specifies the type of inclusion used when finding a match between the other parameter values from the database (value can be either AND or OR). 

    You can specify the organizations for which you want to retrieve information using query string parameters (these can be found in **sample1.json** or **sample2.json** files). For example:
    
    [localhost:8080/api/accounts?iban=IT10474608000005006107XXXXX&bic=OIVUSCLQXXX](http://localhost:8080/api/accounts?iban=IT10474608000005006107XXXXX&bic=OIVUSCLQXXX)

    ### POST Request

    Using **sample1.json** or **sample2.json** (located in **/request-body**) as the body of a POST request to [localhost:8080/api/customer/transfers](http://localhost:8080/api/customer/transfers), you can test the application by performing bulk transfers from one organization to another.

    - Using **sample1.json** will execute successfully updating the DB.
    
        Performing GET request to [localhost:8080/api/accounts](http://localhost:8080/api/accounts) before and after the POST, you will see how the organization's balance is updated.
    
    - Using **sample2.json** will always result in a negative outcome (insufficient credit).
