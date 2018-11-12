package controller;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json.DeserializationExclusionStrategy;
import model.MoneyTransfer;
import service.MoneyTransferService;

import static spark.Spark.get;
import static spark.Spark.post;

public class MoneyTransferController {

    public MoneyTransferController(MoneyTransferService moneyTransferService) {

        post("/moneytransfers", (request, response) -> {
            response.type("application/json");

            ExclusionStrategy strategy = new DeserializationExclusionStrategy();

            Gson requestGson = new GsonBuilder().addDeserializationExclusionStrategy(strategy).create();

            MoneyTransfer moneyTransfer = requestGson.fromJson(request.body(), MoneyTransfer.class);
            moneyTransferService.addMoneyTransfer(moneyTransfer);

            return new Gson().toJson(moneyTransfer);
        });

        get("/moneytransfers/:id", (request, response) -> {
            response.type("application/json");

            int id = Integer.parseInt(request.params(":id"));
            MoneyTransfer moneyTransfer = moneyTransferService.getMoneyTransfer(id);

            if (moneyTransfer != null) {
                return new Gson().toJson(moneyTransfer);
            } else {
                response.status(404);
                //TODO: fail response
                return new Gson();
            }
        });

        get("/moneytransfers", (request, response) -> {
            return new Gson().toJson((moneyTransferService.getMoneyTransfers()));
        });
    }
}
