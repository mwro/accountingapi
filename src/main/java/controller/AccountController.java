package controller;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.DeserializationExclusionStrategy;
import model.Account;
import service.AccountService;

import static spark.Spark.*;
import static spark.Spark.get;

public class AccountController {
    public AccountController(AccountService accountService) {

        post("/accounts", (request, response) -> {
            response.type("application/json");

            ExclusionStrategy strategy = new DeserializationExclusionStrategy();

            Gson requestGson = new GsonBuilder().addDeserializationExclusionStrategy(strategy).create();

            Account account = requestGson.fromJson(request.body(), Account.class);
            accountService.addAccount(account);

            return new Gson().toJson(account);
        });

        get("/accounts/:id", (request, response) -> {
            response.type("application/json");

            int id = Integer.parseInt(request.params(":id"));
            Account account = accountService.getAccount(id);

            if (account != null) {
                return new Gson().toJson(account);
            } else {
                response.status(404);
                //TODO: fail response
                return new Gson();
            }
        });

        get("/accounts", (request, response) -> {
            return new Gson().toJson((accountService.getAccounts()));
        });
    }
}
