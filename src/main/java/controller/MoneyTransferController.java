package controller;

import com.google.gson.*;
import json.DeserializationExclusionStrategy;
import json.JsonUtil;
import json.StatusResponse;
import model.MoneyTransfer;
import service.MoneyTransferService;
import service.MoneyTransferServiceException;

import static spark.Spark.get;
import static spark.Spark.post;

public class MoneyTransferController {

    public MoneyTransferController(MoneyTransferService moneyTransferService) {

        post("/moneytransfers", (request, response) -> {
            response.type("application/json");

            ExclusionStrategy strategy = new DeserializationExclusionStrategy();

            Gson requestGson = new GsonBuilder().addDeserializationExclusionStrategy(strategy).create();

            MoneyTransfer moneyTransfer;

            try {
                moneyTransfer = requestGson.fromJson(request.body(), MoneyTransfer.class);
            }
            catch (JsonSyntaxException e) {
                response.status(400);
                return JsonUtil.getJsonWithStatusAndMessage(StatusResponse.Status.FAIL, "Error in request");
            }

            try {
                moneyTransferService.addMoneyTransfer(moneyTransfer);
            }
            catch (MoneyTransferServiceException e) {
                response.status(400);
                return JsonUtil.getJsonWithStatusAndMessage(StatusResponse.Status.FAIL, e.getMessage());
            }

            JsonElement jsonElement = new Gson().toJsonTree(moneyTransfer);
            return JsonUtil.getJsonWithStatusAndData(StatusResponse.Status.SUCCESS, jsonElement);
        });

        get("/moneytransfers/:id", (request, response) -> {
            response.type("application/json");

            int id;
            try {
                id = Integer.parseInt(request.params(":id"));
            }
            catch (NumberFormatException e) {
                response.status(400);
                return JsonUtil.getJsonWithStatusAndMessage(StatusResponse.Status.FAIL, "Error in request");
            }

            MoneyTransfer moneyTransfer = moneyTransferService.getMoneyTransfer(id);

            if (moneyTransfer != null) {
                JsonElement jsonElement = new Gson().toJsonTree(moneyTransfer);
                return JsonUtil.getJsonWithStatusAndData(StatusResponse.Status.SUCCESS, jsonElement);
            } else {
                response.status(404);
                return JsonUtil.getJsonWithStatusAndMessage(StatusResponse.Status.FAIL, "Money transfer does not exist");
            }
        });

        get("/moneytransfers", (request, response) -> {
            JsonElement jsonElement = new Gson().toJsonTree(moneyTransferService.getMoneyTransfers());
            return JsonUtil.getJsonWithStatusAndData(StatusResponse.Status.SUCCESS, jsonElement);
        });
    }
}
