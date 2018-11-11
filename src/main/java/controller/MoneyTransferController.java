package controller;

import model.MoneyTransfer;
import service.MoneyTransferService;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static spark.Spark.get;
import static spark.Spark.post;

public class MoneyTransferController {

    public MoneyTransferController(MoneyTransferService moneyTransferService) {

        post("/moneytransfers", (request, response) -> {

            Integer accountFromID = Integer.parseInt(request.params(":accountfromid"));
            Integer accountToID = Integer.parseInt(request.params(":accounttoid"));

            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setParseBigDecimal(true);
            BigDecimal moneyAmount = (BigDecimal) decimalFormat.parse(request.params(":moneyamount"));

            int id = moneyTransferService.addTransfer(accountFromID, accountToID, moneyAmount);

            response.status(201); // 201 Created
            return id;
        });

        get("/moneytransfers/:id", (request, response) -> {
            MoneyTransfer moneyTransfer = moneyTransferService.getMoneyTransfer(Integer.parseInt(request.params(":id")));
            if (moneyTransfer != null) {
                return moneyTransfer.toString();
            } else {
                response.status(404); // 404 Not found
                return "Money transfer not found";
            }
        });

        get("/moneytransfers", (request, response) -> {
            StringBuilder moneyTransferString = new StringBuilder();
            for (MoneyTransfer moneyTransfer : moneyTransferService.getMoneyTransfers()) {
                moneyTransferString.append(moneyTransfer.toString()).append("\n");
            }
            return moneyTransferString.toString();
        });
    }
}
