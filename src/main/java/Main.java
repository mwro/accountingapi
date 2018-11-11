import controller.AccountController;
import controller.MoneyTransferController;
import service.AccountService;
import service.MoneyTransferService;

public class Main {
    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        new AccountController(accountService);
        new MoneyTransferController(new MoneyTransferService(accountService));
    }
}
