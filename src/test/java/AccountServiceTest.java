import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import model.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import service.AccountService;

import static org.junit.Assert.assertEquals;

//ConcurrentTestRunner runs all Test methods in 4 separate threads
@RunWith(ConcurrentTestRunner.class)
public class AccountServiceTest {
    private AccountService service;
    private static final int NUMBER_OF_RUNS = 100;

    @Before
    public void setUp() {
        service = new AccountService();
    }

    @Test
    public void testAddMultipleAccountsInSingleThread() {
        for (int i = 0; i < NUMBER_OF_RUNS; i++)
        {
            Account account = new Account();
            account.setName("Name1");
            service.addAccount(account);
        }
    }

    @After
    public void tearDown() {
        assertEquals(4 * NUMBER_OF_RUNS, service.getAccounts().size());
    }
}