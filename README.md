# Bulk Transfer Application

>
> ***This is a very simple application implemented using Java Spring Boot and SQL as DB***
>

## How to run the app

1. Navigate to the root folder. Execute the following command:

    ```pwsh
    > docker compose up

    ```

    It will start the download of two Docker containers, one for the SQL DB and the other for the Spring Boot application. If the second seems to not work properly, try to stop both containers manually using Docker desktop and then restarting SQL container first and then the application container.

2. Test the application (use JSON files in **/request-body** as body of the requests):

    First of all is important to check the current balance of the unique organization in the DB (present by default). In order to get the account information perform a GET request to [localhost:8080/api/account](http://localhost:8080/api/accounts) with parameters in query string IBAN and BIC fo the organization (those information can be found in **sample1.json** or **sample2.json** files). The final URI will be:
    [localhost:8080/api/accounts?organizationIban=IT10474608000005006107XXXXX&organizationBic=OIVUSCLQXXX](http://localhost:8080/api/accounts?organizationIban=IT10474608000005006107XXXXX&organizationBic=OIVUSCLQXXX)  

    Then using **sample1.json** or **sample2.json** as body of a POST request to [localhost:8080/api/customer/transfers](http://localhost:8080/api/customer/transfers) is it possible to test the application, trying to perform bulk transfers from the organization to others.

    For **sample1.json** it would execute successfully (updating the DB, in fact performing now a second GET we can see how the organization balance is updated), while using **sample2.json** will have always a negative outcome (credit not sufficient).

## Wrong Stuff

<!-- TODO: check these things, then remove the section -->

1. `http://localhost:8080/api/accounts`: this cannot return 500 since the query string parameters are optional by default. Instead, list all the accounts without any filter. This kind of error should return `400 BadRequest`.
1. Furthermore, if you hit that scenario an exception must not be thrown in the container's logs. It's normal, some requests won't follow the happy path.
1. `http://localhost:8080/api/accounts?organizationIban=IT10474608000005006107XXXXY&organizationBic=OIVUSCLQXXX`: this cannot return this body and `500 Internal` error. It's the typical `404 NotFound`.

```json
{
    "code": -99,
    "description": "No result found for query [from BankAccount where iban = :iban AND bic = :bic]"
}
```
