# Bulk Transfer Application

>
> ***This is a very simple application implemented using Java Spring Boot and SQL as DB***
>


## How to run the app

1. Navigate to the root folder. Execute the following command:

    ```pwsh
    > docker compose up

    ```
    It will start the download of two Docker containers, one for the SQL DB and the other for the Spring Boot application.

2. Test the application:

    Using request1.json or request2.json as body of a POST request to [localhost:8080/api/transfer](http://localhost:8080/api/transfer) is it possible to test the application. 
    
    For __request1.json__ it would execute successfully (updating the DB, in fact a second execution of the same request would fail for credit not sufficient). Using __request2.json__ will have always a negative outcome (credit not sufficient).
