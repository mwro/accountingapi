import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import model.Account;
import model.MoneyTransfer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import service.AccountService;
import service.MoneyTransferService;
import service.MoneyTransferServiceException;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

//ConcurrentTestRunner runs all Test methods in 4 separate threads
@RunWith(ConcurrentTestRunner.class)
public class MoneyTransferServiceConcurrencyTest {
    private AccountService accountService;
    private MoneyTransferService moneyTransferService;
    private static final int NUMBER_OF_RUNS = 25;
    private BigDecimal startingFunds = BigDecimal.valueOf(1000000);
    private BigDecimal transferValue = BigDecimal.valueOf(1000);

    @Before
    public void setUp() throws MoneyTransferServiceException {
        accountService = new AccountService();
        moneyTransferService = new MoneyTransferService(accountService);

        Account account = new Account();
        account.setName("Name1");
        accountService.addAccount(account);

        account = new Account();
        account.setName("Name2");
        accountService.addAccount(account);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setTransferValue(startingFunds);
        moneyTransfer.setAccountToID(0);
        moneyTransferService.addMoneyTransfer(moneyTransfer);

        moneyTransfer = new MoneyTransfer();
        moneyTransfer.setTransferValue(startingFunds);
        moneyTransfer.setAccountToID(1);
        moneyTransferService.addMoneyTransfer(moneyTransfer);
    }

    @Test
    public void testAddMultipleAccountsInSingleThread() throws MoneyTransferServiceException {
        for (int i = 0; i < NUMBER_OF_RUNS; i++)
        {
            MoneyTransfer moneyTransfer = new MoneyTransfer();
            moneyTransfer.setTransferValue(transferValue);
            moneyTransfer.setAccountToID(0);
            moneyTransfer.setAccountFromID(1);
            moneyTransferService.addMoneyTransfer(moneyTransfer);
        }
    }

    @After
    public void tearDown() {
        assertEquals(4 * NUMBER_OF_RUNS + 2, moneyTransferService.getMoneyTransfers().size());
        Assert.assertEquals(startingFunds.subtract(transferValue.multiply(BigDecimal.valueOf(4 * NUMBER_OF_RUNS))), accountService.getAccount(1).getBalance());
        Assert.assertEquals(startingFunds.add(transferValue.multiply(BigDecimal.valueOf(4 * NUMBER_OF_RUNS))), accountService.getAccount(0).getBalance());
    }
}