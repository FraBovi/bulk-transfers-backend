# Bulk Transfer Application

>
> ***This is a very simple application implemented using Java Spring Boot and SQL as DB***
>


## How to run the app

1. Navigate to the root folder. Execute the following command:

    ```pwsh
    > docker compose up

    ```
    It will start the download of two Docker containers, one for the SQL DB and the other for the Spring Boot application. If the second seems to not work propertly, try to stop both containers manually using Docker desktop and then restarting SQL container first and then tha application container.

2. Test the application (use JSON files in __/request-body__ as body of the requests):

    First of all is important to check the current balance of the unique organization in the DB (present by default). In order to get the account information perform a GET request to [localhost:8080/api/account](http://localhost:8080/api/accounts) with parameters in query string IBAN and BIC fo the organization (those information can be found in __sample1.json__ or __sample2.json__ files). The final URI will be:
    [localhost:8080/api/accounts?organizationIban=IT10474608000005006107XXXXX&organizationBic=OIVUSCLQXXX](http://localhost:8080/api/accounts?organizationIban=IT10474608000005006107XXXXX&organizationBic=OIVUSCLQXXX)  

    Then using __sample1.json__ or __sample2.json__ as body of a POST request to [localhost:8080/api/customer/transfers](http://localhost:8080/api/customer/transfers) is it possible to test the application, trying to perform bulk transfers from the organization to others. 
    
    For __sample1.json__ it would execute successfully (updating the DB, in fact performing now a second GET we can see how the organization baance is updated), whil using __sample2.json__ will have always a negative outcome (credit not sufficient).
