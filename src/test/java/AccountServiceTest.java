import model.Account;
import org.junit.Before;
import org.junit.Test;
import service.AccountService;

import java.util.Collection;

import static org.junit.Assert.*;

public class AccountServiceTest {
    private AccountService service;

    @Before
    public void setUp() {
        service = new AccountService();
    }

    @Test
    public void testGetAccountsAndNoAccounts() {
        int expectedSize = 0;

        Collection<Account> accounts = service.getAccounts();
        assertNotNull(accounts);
        assertEquals(expectedSize, accounts.size());
    }

    @Test
    public void testGetAccountsAndAccountsExist() {
        int expectedSize = 1;

        Account account = new Account();
        account.setName("Name1");
        service.addAccount(account);

        Collection<Account> accounts = service.getAccounts();
        assertNotNull(accounts);
        assertEquals(expectedSize, accounts.size());
        assertTrue(accounts.contains(account));

        expectedSize = 2;

        Account account2 = new Account();
        account2.setName("Name2");
        service.addAccount(account2);

        accounts = service.getAccounts();
        assertNotNull(accounts);
        assertEquals(expectedSize, accounts.size());
        assertTrue(accounts.contains(account2));
        assertTrue(accounts.contains(account));
    }

    @Test
    public void testGetAccountAndNoAccounts() {
        Account account = service.getAccount(0);
        assertNull(account);

        account = service.getAccount(12232);
        assertNull(account);
    }

    @Test
    public void testAddGetAccountAndAccountsExist() {
        Account account = new Account();
        account.setName("Name1");
        service.addAccount(account);
        int expectedID = 0;

        Account account2 = new Account();
        account2.setName("Name2");
        service.addAccount(account2);
        int expectedID2 = 1;

        Account resultAccount = service.getAccount(expectedID);
        assertNotNull(resultAccount);
        assertEquals(account, resultAccount);
        assertEquals(expectedID, resultAccount.getID());

        resultAccount = service.getAccount(expectedID2);
        assertNotNull(resultAccount);
        assertEquals(account2, resultAccount);
        assertEquals(expectedID2, resultAccount.getID());

        resultAccount = service.getAccount(12232);
        assertNull(resultAccount);
    }

    @Test
    public void testGetAccountAndWrongParameter() {
        Account account = new Account();
        account.setName("Name1");
        service.addAccount(account);

        Account account2 = new Account();
        account2.setName("Name2");
        service.addAccount(account2);

        Account resultAccount = service.getAccount(null);
        assertNull(resultAccount);

        resultAccount = service.getAccount(-1);
        assertNull(resultAccount);
    }

    @Test
    public void testAddAccountAndWrongParameter() {
        Account account = new Account();
        account.setName("Name1");
        service.addAccount(account);

        Account account2 = new Account();
        account2.setName("Name2");
        service.addAccount(account2);

        service.addAccount(null);

        Account account3 = new Account();
        account3.setName("Name3");
        service.addAccount(account3);

        int expectedID = 2;
        Account resultAccount = service.getAccount(expectedID);
        assertNotNull(resultAccount);
        assertEquals(account3, resultAccount);
        assertEquals(expectedID, resultAccount.getID());
    }
}