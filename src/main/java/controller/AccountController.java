package controller;

import model.Account;
import service.AccountService;

import static spark.Spark.*;
import static spark.Spark.get;

public class AccountController {
    public AccountController(AccountService accountService) {

        post("/accounts", (request, response) -> {
            String name = request.queryParams("name");

            int id = accountService.createAccount(name);

            response.status(201); // 201 Created
            return id;
        });

        get("/accounts/:id", (request, response) -> {
            Account account = accountService.getAccount(Integer.parseInt(request.params(":id")));
            if (account != null) {
                return account.toString();
            } else {
                response.status(404); // 404 Not found
                return "Account not found";
            }
        });

        get("/accounts", (request, response) -> {
            StringBuilder accountsString = new StringBuilder();
            for (Account account : accountService.getAccounts()) {
                accountsString.append(account.toString()).append("\n");
            }
            return accountsString.toString();
        });
    }
}
