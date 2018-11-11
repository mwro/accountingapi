import controller.AccountController;
import service.AccountService;

public class Main {
    public static void main(String[] args) {
        new AccountController(new AccountService());
    }
}
