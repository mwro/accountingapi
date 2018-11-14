import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import controller.AccountController;
import model.Account;
import org.junit.Rule;
import org.junit.Test;
import service.AccountService;
import spark.servlet.SparkApplication;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountControllerTest {

    private static AccountService accountService;

    public static class AccountContollerTestSparkApplication implements SparkApplication {
        @Override
        public void init() {
            accountService = new AccountService();
            new AccountController(accountService);
        }
    }

    @Rule
    public SparkServer<AccountContollerTestSparkApplication> testServer = new SparkServer<>(AccountContollerTestSparkApplication.class, 4567);

    @Test
    public void testGetAllMethodAndNoAccounts() throws Exception {
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":[]" +
                "}";

        GetMethod get = testServer.get("/accounts", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testGetAllMethodAndOneAccount() throws Exception {
        String testAccountName1 = "Name1";
        addAccountToService(testAccountName1);

        String ID1 = "0";
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":[" +
                        "{" +
                             "\"ID\":" + ID1 + "," +
                            "\"name\":\""+ testAccountName1 +"\"," +
                            "\"balance\":0" +
                        "}" +
                    "]" +
                "}";

        GetMethod get = testServer.get("/accounts", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testGetAllMethodAndMultipleAccounts() throws Exception {
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":[]" +
                "}";

        GetMethod get = testServer.get("/accounts", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        String testAccountName1 = "Name1";
        addAccountToService(testAccountName1);
        String testAccountName2 = "Name2";
        addAccountToService(testAccountName2);

        String ID1 = "0";
        String ID2 = "1";
        expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":[" +
                        "{" +
                            "\"ID\":" + ID1 + "," +
                            "\"name\":\""+ testAccountName1 +"\"," +
                            "\"balance\":0" +
                        "}," +
                        "{" +
                            "\"ID\":" + ID2 + "," +
                            "\"name\":\"" + testAccountName2 + "\"," +
                            "\"balance\":0" +
                        "}" +
                    "]" +
                "}";
        httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));


        String testAccountName3 = "Name3";
        addAccountToService(testAccountName3);

        String ID3 = "2";
        expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":[" +
                        "{" +
                            "\"ID\":" + ID1 + "," +
                            "\"name\":\"" + testAccountName1 + "\"," +
                            "\"balance\":0" +
                        "}," +
                        "{" +
                            "\"ID\":" + ID2 + "," +
                            "\"name\":\"" + testAccountName2 + "\"," +
                            "\"balance\":0" +
                        "}," +
                        "{" +
                            "\"ID\":" + ID3 + "," +
                            "\"name\":\"" + testAccountName3 +"\"," +
                            "\"balance\":0" +
                        "}" +
                    "]" +
                "}";
        httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testGetAccountMethodAndNoAccounts() throws Exception {
        String expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Account does not exist\"" +
                "}";

        GetMethod get = testServer.get("/accounts/3", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(404, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        String badID = "koza";

        expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Error in request\"" +
                "}";

        //test request for bad ID
        get = testServer.get("/accounts/" + badID, false);
        httpResponse = testServer.execute(get);
        assertEquals(400, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testGetAccountMethodAndOneAccount() throws Exception {
        String testAccountName1 = "Name1";
        addAccountToService(testAccountName1);
        String ID1 = "0";

        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":{" +
                    "\"ID\":" + ID1 + "," +
                    "\"name\":\"" + testAccountName1 +"\"," +
                    "\"balance\":0" +
                    "}" +
                "}";

        //test request for first element after adding first account to service
        GetMethod get = testServer.get("/accounts/" + ID1, false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        String ID2 = "1";

        expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Account does not exist\"" +
                "}";

        //test request for another element but it wasn't added to service
        get = testServer.get("/accounts/" + ID2, false);
        httpResponse = testServer.execute(get);
        assertEquals(404, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        String badID = "koza";

        expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Error in request\"" +
                "}";

        //test request for bad ID
        get = testServer.get("/accounts/" + badID, false);
        httpResponse = testServer.execute(get);
        assertEquals(400, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testGetAccountMethodAndMultipleAccounts() throws Exception {
        String testAccountName1 = "Name1";
        addAccountToService(testAccountName1);
        String testAccountName2 = "Name2";
        addAccountToService(testAccountName2);

        String ID1 = "0";
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":{" +
                    "\"ID\":" + ID1 + "," +
                    "\"name\":\"" + testAccountName1 +"\"," +
                    "\"balance\":0" +
                    "}" +
                "}";

        //test request for first element after adding second account to service
        GetMethod get = testServer.get("/accounts/" + ID1, false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        String ID2 = "1";
        expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":{" +
                    "\"ID\":" + ID2 + "," +
                    "\"name\":\"" + testAccountName2 +"\"," +
                    "\"balance\":0" +
                    "}" +
                "}";

        //test request for second element after adding second account to service
        get = testServer.get("/accounts/" + ID2, false);
        httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        String ID3 = "2";

        expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Account does not exist\"" +
                "}";

        //test request for another element but it wasn't added to service
        get = testServer.get("/accounts/" + ID3, false);
        httpResponse = testServer.execute(get);
        assertEquals(404, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        String badID = "koza";

        expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Error in request\"" +
                "}";

        //test request for bad ID
        get = testServer.get("/accounts/" + badID, false);
        httpResponse = testServer.execute(get);
        assertEquals(400, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testPostMethodAndNoAccounts() throws Exception {
        String testAccountName1 = "Name1";
        String inputJson = "{" +
                "\"name\": \"" + testAccountName1 + "\"" +
                "}";

        int expectedID = 0;
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                         "\"ID\":" + expectedID + "," +
                        "\"name\":\""+ testAccountName1 +"\"," +
                        "\"balance\":0" +
                    "}" +
                "}";

        PostMethod post = testServer.post("/accounts", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        assertEquals(1, accountService.getAccounts().size());
        Account account = accountService.getAccount(expectedID);
        assertNotNull(account);
        assertEquals(testAccountName1, account.getName());
        assertEquals(expectedID, account.getID());
    }

    @Test
    public void testPostMethodAndAccountsAlreadyExisting() throws Exception {
        String testAccountName1 = "Name1";
        addAccountToService(testAccountName1);
        String testAccountName2 = "Name2";
        addAccountToService(testAccountName2);

        String testAccountName3 = "Name3";
        String inputJson = "{" +
                "\"name\": \"" + testAccountName3 + "\"" +
                "}";

        int expectedID = 2;
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                         "\"ID\":" + expectedID + "," +
                        "\"name\":\""+ testAccountName3 +"\"," +
                        "\"balance\":0" +
                    "}" +
                "}";

        PostMethod post = testServer.post("/accounts", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        assertEquals(3, accountService.getAccounts().size());
        Account account = accountService.getAccount(expectedID);
        assertNotNull(account);
        assertEquals(testAccountName3, account.getName());
        assertEquals(expectedID, account.getID());
    }

    @Test
    public void testMultiplePostMethods() throws Exception {
        String testAccountName1 = "Name1";
        String inputJson = "{" +
                "\"name\": \"" + testAccountName1 + "\"" +
                "}";

        int expectedID = 0;
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                         "\"ID\":" + expectedID + "," +
                        "\"name\":\""+ testAccountName1 +"\"," +
                        "\"balance\":0" +
                    "}" +
                "}";

        PostMethod post = testServer.post("/accounts", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        assertEquals(1, accountService.getAccounts().size());
        Account account = accountService.getAccount(expectedID);
        assertNotNull(account);
        assertEquals(testAccountName1, account.getName());
        assertEquals(expectedID, account.getID());

        String testAccountName2 = "Name2";
        String inputJson2 = "{" +
                "\"name\": \"" + testAccountName2 + "\"" +
                "}";

        int expectedID2 = 1;
        String expectedJson2 = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                         "\"ID\":" + expectedID2 + "," +
                        "\"name\":\""+ testAccountName2 +"\"," +
                        "\"balance\":0" +
                    "}" +
                "}";

        post = testServer.post("/accounts", inputJson2, false);
        httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson2, getStringFromBody(httpResponse));

        assertEquals(2, accountService.getAccounts().size());
        account = accountService.getAccount(expectedID2);
        assertNotNull(account);
        assertEquals(testAccountName2, account.getName());
        assertEquals(expectedID2, account.getID());
    }

    @Test
    public void testPostMethodAndIDNotSetFromJson() throws Exception {
        String testAccountName1 = "Name1";
        int inputID = 22;
        String inputJson = "{" +
                "\"ID\": " + inputID + "," +
                "\"name\": \"" + testAccountName1 + "\"" +
                "}";

        int expectedID = 0;
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                         "\"ID\":" + expectedID + "," +
                        "\"name\":\""+ testAccountName1 +"\"," +
                        "\"balance\":0" +
                    "}" +
                "}";

        PostMethod post = testServer.post("/accounts", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        assertEquals(1, accountService.getAccounts().size());

        assertNull(accountService.getAccount(inputID));
        Account account = accountService.getAccount(0);
        assertNotNull(account);
        assertEquals(testAccountName1, account.getName());
        assertEquals(expectedID, account.getID());
    }

    @Test
    public void testPostMethodAndBalanceNotSetFromJson() throws Exception {
        String testAccountName1 = "Name1";
        BigDecimal inputBalance = BigDecimal.valueOf(1000.99);
        String inputJson = "{" +
                "\"name\": \"" + testAccountName1 + "\"," +
                "\"balance\": " + inputBalance +
                "}";

        int expectedID = 0;
        BigDecimal expectedBalance = BigDecimal.ZERO;
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                        "\"ID\":" + expectedID + "," +
                        "\"name\":\""+ testAccountName1 +"\"," +
                        "\"balance\":" + expectedBalance +
                    "}" +
                "}";

        PostMethod post = testServer.post("/accounts", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        assertEquals(1, accountService.getAccounts().size());

        Account account = accountService.getAccount(expectedID);
        assertNotNull(account);
        assertEquals(testAccountName1, account.getName());
        assertNotEquals(inputBalance, account.getBalance());
        assertEquals(expectedBalance, account.getBalance());
    }

    @Test
    public void testPostMethodWithBadJsonRequest() throws Exception {
        String inputJson = "koza";

        String expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Error in request\"" +
                "}";

        PostMethod post = testServer.post("/accounts", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(400, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        assertEquals(0, accountService.getAccounts().size());

        Account account = accountService.getAccount(0);
        assertNull(account);
    }

    private void addAccountToService(String name) {
        Account account = new Account();
        account.setName(name);
        accountService.addAccount(account);
    }

    private String getStringFromBody(HttpResponse httpResponse) {
        return new String(httpResponse.body());
    }

}