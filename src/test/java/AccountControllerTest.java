import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.sparkjava.test.SparkServer;
import controller.AccountController;
import model.Account;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import service.AccountService;
import spark.servlet.SparkApplication;

import static org.junit.Assert.assertEquals;

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

    private void addAccountToService(String name) {
        Account account = new Account();
        account.setName(name);
        accountService.addAccount(account);
    }

    private String getStringFromBody(HttpResponse httpResponse) {
        return new String(httpResponse.body());
    }

}