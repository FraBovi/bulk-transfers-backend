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

    It will start the download of two Docker containers, one for the SQL DB and the other for the Spring Boot application. If the second seems to not work properly, try to stop both containers manually using Docker desktop and then restarting SQL container first and then the application container.

2. Test the application (use JSON files in **/request-body** as body of the requests):

    First of all is important to check the current balance of the organization that will be performe later the bulk transfer. In order to get the information of all the organizations store in the DB we can perform a GET request to [localhost:8080/api/accounts](http://localhost:8080/api/accounts). Then using parameters in query string IBAN and/or BIC we can specify the organization for which we want to retrieve the information (those information can be found in **sample1.json** or **sample2.json** files). In this case we can use (it is possible to use a single parameter, even though only together do these parameters represent a unique identifier for the organization):
    [localhost:8080/api/accounts?organizationIban=IT10474608000005006107XXXXX&organizationBic=OIVUSCLQXXX](http://localhost:8080/api/accounts?organizationIban=IT10474608000005006107XXXXX&organizationBic=OIVUSCLQXXX)  

    Then using **sample1.json** or **sample2.json** as body of a POST request to [localhost:8080/api/customer/transfers](http://localhost:8080/api/customer/transfers) is it possible to test the application, trying to perform bulk transfers from the organization to others.

    For **sample1.json** it would execute successfully (updating the DB, in fact performing now a second GET of [localhost:8080/api/accounts](http://localhost:8080/api/accounts) we can see how the organization balance is updated), while using **sample2.json** will have always a negative outcome (credit not sufficient).
