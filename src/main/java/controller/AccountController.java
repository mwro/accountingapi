package controller;

import com.google.gson.*;
import json.DeserializationExclusionStrategy;
import json.JsonUtil;
import model.Account;
import service.AccountService;

import static json.StatusResponse.Status;
import static spark.Spark.get;
import static spark.Spark.post;

public class AccountController {
    public AccountController(AccountService accountService) {

        post("/accounts", (request, response) -> {
            response.type("application/json");

            ExclusionStrategy strategy = new DeserializationExclusionStrategy();

            Gson requestGson = new GsonBuilder().addDeserializationExclusionStrategy(strategy).create();

            Account account;

            try {
                account = requestGson.fromJson(request.body(), Account.class);
            }
            catch (JsonSyntaxException e) {
                response.status(400);
                return JsonUtil.getJsonWithStatusAndMessage(Status.FAIL, "Error in request");
            }

            accountService.addAccount(account);

            JsonElement jsonElement = new Gson().toJsonTree(account);
            return JsonUtil.getJsonWithStatusAndData(Status.SUCCESS, jsonElement);
        });

        get("/accounts/:id", (request, response) -> {
            response.type("application/json");
            int id;

            try {
                id = Integer.parseInt(request.params(":id"));
            }
            catch (NumberFormatException e) {
                response.status(400);
                return JsonUtil.getJsonWithStatusAndMessage(Status.FAIL, "Error in request");
            }

            Account account = accountService.getAccount(id);

            if (account != null) {
                JsonElement jsonElement = new Gson().toJsonTree(account);
                return JsonUtil.getJsonWithStatusAndData(Status.SUCCESS, jsonElement);
            } else {
                response.status(404);
                return JsonUtil.getJsonWithStatusAndMessage(Status.FAIL, "Account does not exist");
            }
        });

        get("/accounts", (request, response) -> {
            JsonElement jsonElement = new Gson().toJsonTree(accountService.getAccounts());
            return JsonUtil.getJsonWithStatusAndData(Status.SUCCESS, jsonElement);
        });
    }
}
