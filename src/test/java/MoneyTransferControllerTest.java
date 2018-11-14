import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import controller.MoneyTransferController;
import model.Account;
import model.MoneyTransfer;
import org.junit.Rule;
import org.junit.Test;
import service.AccountService;
import service.MoneyTransferService;
import spark.servlet.SparkApplication;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

public class MoneyTransferControllerTest {

    private static AccountService accountService;
    private static MoneyTransferService moneyTransferService;

    public static class MoneyTransferContollerTestSparkApplication implements SparkApplication {
        @Override
        public void init() {
            accountService = new AccountService();
            moneyTransferService = new MoneyTransferService(accountService);
            new MoneyTransferController(moneyTransferService);
        }
    }

    @Rule
    public SparkServer<MoneyTransferContollerTestSparkApplication> testServer = new SparkServer<>(MoneyTransferContollerTestSparkApplication.class, 4567);

    @Test
    public void testGetAllMethodAndNoAccounts() throws Exception {
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":[]" +
                "}";

        GetMethod get = testServer.get("/moneytransfers", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testGetAllMethodAndOneMoneyTransfer() throws Exception {
        addAccountToService("Name1");

        Integer toID1 = 0;
        BigDecimal transferValue1 = BigDecimal.valueOf(2500.99);
        MoneyTransfer transfer1 = addMoneyTransferToService(null, toID1, transferValue1);

        Integer ID1 = 0;
        String expectedDateString1 = getFormattedDate(transfer1.getDate());
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":[" +
                    "{" +
                        "\"ID\":" + ID1 + "," +
                        "\"accountToID\":" + toID1 + "," +
                        "\"transferValue\":" + transferValue1 + "," +
                        "\"date\":\"" + expectedDateString1 + "\"" +
                    "}]" +
                "}";

        GetMethod get = testServer.get("/moneytransfers", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testGetAllMethodAndMultipleMoneyTransfers() throws Exception {
        addAccountToService("Name1");
        addAccountToService("Name2");
        addAccountToService("Name3");

        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":[]" +
                "}";

        GetMethod get = testServer.get("/moneytransfers", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        Integer toID1 = 2;
        BigDecimal transferValue1 = BigDecimal.valueOf(2500.99);
        MoneyTransfer transfer1 = addMoneyTransferToService(null, toID1, transferValue1);

        Integer toID2 = 1;
        BigDecimal transferValue2 = BigDecimal.valueOf(35400.95);
        MoneyTransfer transfer2 = addMoneyTransferToService(null, toID2, transferValue2);

        String ID1 = "0";
        String ID2 = "1";
        String expectedDateString1 = getFormattedDate(transfer1.getDate());
        String expectedDateString2 = getFormattedDate(transfer2.getDate());

        expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":[" +
                        "{" +
                            "\"ID\":" + ID1 + "," +
                            "\"accountToID\":" + toID1 + "," +
                            "\"transferValue\":" + transferValue1 + "," +
                            "\"date\":\"" + expectedDateString1 + "\"" +
                        "}," +
                        "{" +
                            "\"ID\":" + ID2 + "," +
                            "\"accountToID\":" + toID2 + "," +
                            "\"transferValue\":" + transferValue2 + "," +
                            "\"date\":\"" + expectedDateString2 + "\"" +
                        "}" +
                    "]" +
                "}";

        httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        Integer toID3 = 0;
        Integer fromID3 = 1;
        BigDecimal transferValue3 = BigDecimal.valueOf(2580.99);
        MoneyTransfer transfer3 = addMoneyTransferToService(fromID3, toID3, transferValue3);

        String ID3 = "2";
        String expectedDateString3 = getFormattedDate(transfer3.getDate());

        expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":[" +
                        "{" +
                            "\"ID\":" + ID1 + "," +
                            "\"accountToID\":" + toID1 + "," +
                            "\"transferValue\":" + transferValue1 + "," +
                            "\"date\":\"" + expectedDateString1 + "\"" +
                        "}," +
                        "{" +
                            "\"ID\":" + ID2 + "," +
                            "\"accountToID\":" + toID2 + "," +
                            "\"transferValue\":" + transferValue2 + "," +
                            "\"date\":\"" + expectedDateString2 + "\"" +
                        "}," +
                        "{" +
                            "\"ID\":" + ID3 + "," +
                            "\"accountFromID\":" + fromID3 + "," +
                            "\"accountToID\":" + toID3 + "," +
                            "\"transferValue\":" + transferValue3 + "," +
                            "\"date\":\"" + expectedDateString3 + "\"" +
                        "}" +
                    "]" +
                "}";
        httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testGetMoneyTransferMethodAndNoMoneyTransfers() throws Exception {
        String expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Money transfer does not exist\"" +
                "}";

        GetMethod get = testServer.get("/moneytransfers/3", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(404, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        String badID = "koza";

        expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Error in request\"" +
                "}";

        //test request for bad ID
        get = testServer.get("/moneytransfers/" + badID, false);
        httpResponse = testServer.execute(get);
        assertEquals(400, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testGetMoneyTransferMethodAndOneMoneyTransfer() throws Exception {
        addAccountToService("Name1");
        Integer toID1 = 0;
        BigDecimal transferValue1 = BigDecimal.valueOf(9500.99);
        MoneyTransfer transfer1 = addMoneyTransferToService(null, toID1, transferValue1);

        Integer ID1 = 0;
        String expectedDateString1 = getFormattedDate(transfer1.getDate());
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                        "\"ID\":" + ID1 + "," +
                        "\"accountToID\":" + toID1 + "," +
                        "\"transferValue\":" + transferValue1 + "," +
                        "\"date\":\"" + expectedDateString1 + "\"" +
                    "}" +
                "}";

        //test request for first element after adding one money transfer to service
        GetMethod get = testServer.get("/moneytransfers/" + ID1, false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        String ID2 = "1";

        expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Money transfer does not exist\"" +
                "}";

        //test request for another element but it wasn't added to service
        get = testServer.get("/moneytransfers/" + ID2, false);
        httpResponse = testServer.execute(get);
        assertEquals(404, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        String badID = "koza";

        expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Error in request\"" +
                "}";

        //test request for bad ID
        get = testServer.get("/moneytransfers/" + badID, false);
        httpResponse = testServer.execute(get);
        assertEquals(400, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testGetMoneyTransferMethodAndMultipleMoneyTransfers() throws Exception {
        addAccountToService("Name1");
        addAccountToService("Name2");
        addAccountToService("Name3");

        Integer toID1 = 2;
        BigDecimal transferValue1 = BigDecimal.valueOf(2500.99);
        MoneyTransfer transfer1 = addMoneyTransferToService(null, toID1, transferValue1);
        
        Integer toID2 = 1;
        BigDecimal transferValue2 = BigDecimal.valueOf(3900.95);
        MoneyTransfer transfer2 = addMoneyTransferToService(null, toID2, transferValue2);

        Integer fromID3 = 2;
        Integer toID3 = 0;
        BigDecimal transferValue3 = BigDecimal.valueOf(2900.95);
        MoneyTransfer transfer3 = addMoneyTransferToService(fromID3, toID3, transferValue3);

        Integer ID1 = 0;
        String expectedDateString1 = getFormattedDate(transfer1.getDate());
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                        "\"ID\":" + ID1 + "," +
                        "\"accountToID\":" + toID1 + "," +
                        "\"transferValue\":" + transferValue1 + "," +
                        "\"date\":\"" + expectedDateString1 + "\"" +
                    "}" +
                "}";

        //test request for first element after adding three money transfers to service
        GetMethod get = testServer.get("/moneytransfers/" + ID1, false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        Integer ID2 = 1;
        String expectedDateString2 = getFormattedDate(transfer2.getDate());
        expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                        "\"ID\":" + ID2 + "," +
                        "\"accountToID\":" + toID2 + "," +
                        "\"transferValue\":" + transferValue2 + "," +
                        "\"date\":\"" + expectedDateString2 + "\"" +
                    "}" +
                "}";

        //test request for second element after adding three money transfers to service
        get = testServer.get("/moneytransfers/" + ID2, false);
        httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        Integer ID3 = 2;
        String expectedDateString3 = getFormattedDate(transfer3.getDate());
        expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                        "\"ID\":" + ID3 + "," +
                        "\"accountFromID\":" + fromID3 + "," +
                        "\"accountToID\":" + toID3 + "," +
                        "\"transferValue\":" + transferValue3 + "," +
                        "\"date\":\"" + expectedDateString3 + "\"" +
                    "}" +
                "}";

        //test request for second element after adding three money transfers to service
        get = testServer.get("/moneytransfers/" + ID3, false);
        httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        String ID4 = "4";

        expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Money transfer does not exist\"" +
                "}";

        //test request for another element but it wasn't added to service
        get = testServer.get("/moneytransfers/" + ID4, false);
        httpResponse = testServer.execute(get);
        assertEquals(404, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        String badID = "koza";

        expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Error in request\"" +
                "}";

        //test request for bad ID
        get = testServer.get("/moneytransfers/" + badID, false);
        httpResponse = testServer.execute(get);
        assertEquals(400, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testPostMethodAndNoMoneyTransfers() throws Exception {
        addAccountToService("Name1");

        Integer toID = 0;
        BigDecimal transferValue = BigDecimal.valueOf(1500.99);

        String inputJson = "{" +
                "\"accountToID\":" + toID + "," +
                "\"transferValue\":" + transferValue +
               "}";

        int expectedID = 0;

        PostMethod post = testServer.post("/moneytransfers", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());

        assertEquals(1, moneyTransferService.getMoneyTransfers().size());

        MoneyTransfer moneyTransfer = moneyTransferService.getMoneyTransfer(expectedID);
        assertNotNull(moneyTransfer);
        assertEquals(expectedID, moneyTransfer.getID());
        assertNull(moneyTransfer.getAccountFromID());
        assertEquals(toID, moneyTransfer.getAccountToID());

        Date expectedDate = moneyTransfer.getDate();
        String expectedDateString = getFormattedDate(expectedDate);
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                        "\"ID\":" + expectedID + "," +
                        "\"accountToID\":" + toID + "," +
                        "\"transferValue\":" + transferValue + "," +
                        "\"date\":\"" + expectedDateString + "\"" +
                    "}" +
                "}";
        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testPostMethodAndMoneyTransfersAlreadyExisting() throws Exception {
        addAccountToService("Name1");
        addAccountToService("Name2");
        addAccountToService("Name3");

        Integer testTransferToID1 = 1;
        BigDecimal transferValue1 = BigDecimal.valueOf(2000.99);

        addMoneyTransferToService(null, testTransferToID1, transferValue1);
        Integer testTransferToID2 = 2;
        BigDecimal transferValue2 = BigDecimal.valueOf(3400.95);

        addMoneyTransferToService(null, testTransferToID2, transferValue2);

        Integer fromID = 2;
        Integer toID = 0;
        BigDecimal transferValue = BigDecimal.valueOf(1500.99);

        String inputJson = "{" +
                "\"accountFromID\":" + fromID + "," +
                "\"accountToID\":" + toID + "," +
                "\"transferValue\":" + transferValue +
               "}";

        int expectedID = 2;

        PostMethod post = testServer.post("/moneytransfers", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());

        assertEquals(3, moneyTransferService.getMoneyTransfers().size());

        MoneyTransfer moneyTransfer = moneyTransferService.getMoneyTransfer(expectedID);
        assertNotNull(moneyTransfer);
        assertEquals(expectedID, moneyTransfer.getID());
        assertEquals(fromID, moneyTransfer.getAccountFromID());
        assertEquals(toID, moneyTransfer.getAccountToID());

        Date expectedDate = moneyTransfer.getDate();
        String expectedDateString = getFormattedDate(expectedDate);
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                        "\"ID\":" + expectedID + "," +
                        "\"accountFromID\":" + fromID + "," +
                        "\"accountToID\":" + toID + "," +
                        "\"transferValue\":" + transferValue + "," +
                        "\"date\":\"" + expectedDateString + "\"" +
                    "}" +
                "}";

        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testPostMethodAndBadJsonValueFormat() throws Exception {
        addAccountToService("Name1");
        addAccountToService("Name2");
        addAccountToService("Name3");

        Integer testTransferToID1 = 1;
        BigDecimal transferValue1 = BigDecimal.valueOf(2600.99);

        addMoneyTransferToService(null, testTransferToID1, transferValue1);
        Integer testTransferToID2 = 2;
        BigDecimal transferValue2 = BigDecimal.valueOf(3430.95);

        addMoneyTransferToService(null, testTransferToID2, transferValue2);

        Integer fromID = 2;
        Integer toID = 0;
        String badTransferValue = "abc";

        String inputJson = "{" +
                "\"accountFromID\":" + fromID + "," +
                "\"accountToID\":" + toID + "," +
                "\"transferValue\":" + badTransferValue +
                "}";

        int expectedID = 2;

        PostMethod post = testServer.post("/moneytransfers", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(400, httpResponse.code());

        assertEquals(2, moneyTransferService.getMoneyTransfers().size());

        MoneyTransfer moneyTransfer = moneyTransferService.getMoneyTransfer(expectedID);
        assertNull(moneyTransfer);

        String expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Error in request\"" +
                "}";

        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testMultiplePostMethods() throws Exception {
        addAccountToService("Name1");
        addAccountToService("Name2");
        addAccountToService("Name3");

        Integer toID = 0;
        BigDecimal transferValue = BigDecimal.valueOf(1500.99);

        String inputJson = "{" +
                "\"accountToID\":" + toID + "," +
                "\"transferValue\":" + transferValue +
               "}";

        int expectedID = 0;

        PostMethod post = testServer.post("/moneytransfers", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());

        assertEquals(1, moneyTransferService.getMoneyTransfers().size());

        MoneyTransfer moneyTransfer = moneyTransferService.getMoneyTransfer(expectedID);
        assertNotNull(moneyTransfer);
        assertEquals(expectedID, moneyTransfer.getID());
        assertNull(moneyTransfer.getAccountFromID());
        assertEquals(toID, moneyTransfer.getAccountToID());

        Date expectedDate = moneyTransfer.getDate();
        String expectedDateString = getFormattedDate(expectedDate);
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                        "\"ID\":" + expectedID + "," +
                        "\"accountToID\":" + toID + "," +
                        "\"transferValue\":" + transferValue + "," +
                        "\"date\":\"" + expectedDateString + "\"" +
                    "}" +
                "}";

        assertEquals(expectedJson, getStringFromBody(httpResponse));


        Integer fromID2 = 1;
        Integer toID2 = 2;
        BigDecimal transferValue2 = BigDecimal.valueOf(1200.99);

        String inputJson2 = "{" +
                "\"accountFromID\":" + fromID2 + "," +
                "\"accountToID\":" + toID2 + "," +
                "\"transferValue\":" + transferValue2 +
               "}";

        int expectedID2 = 1;

        post = testServer.post("/moneytransfers", inputJson2, false);
        httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());

        assertEquals(2, moneyTransferService.getMoneyTransfers().size());

        moneyTransfer = moneyTransferService.getMoneyTransfer(expectedID2);
        assertNotNull(moneyTransfer);
        assertEquals(expectedID2, moneyTransfer.getID());
        assertEquals(fromID2, moneyTransfer.getAccountFromID());
        assertEquals(toID2, moneyTransfer.getAccountToID());

        Date expectedDate2 = moneyTransfer.getDate();
        String expectedDateString2 = getFormattedDate(expectedDate2);
        String expectedJson2 = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                        "\"ID\":" + expectedID2 + "," +
                        "\"accountFromID\":" + fromID2 + "," +
                        "\"accountToID\":" + toID2 + "," +
                        "\"transferValue\":" + transferValue2 + "," +
                        "\"date\":\"" + expectedDateString2 + "\"" +
                    "}" +
                "}";
        assertEquals(expectedJson2, getStringFromBody(httpResponse));
    }

    @Test
    public void testPostMethodAndIDNotSetFromJson() throws Exception {
        addAccountToService("Name1");
        addAccountToService("Name2");

        int inputID = 22;
        BigDecimal transferValue = BigDecimal.valueOf(1000.99);

        String inputJson = "{" +
                "\"ID\":" + inputID + "," +
                "\"accountFromID\":0," +
                "\"accountToID\":1," +
                "\"transferValue\":" + transferValue +
               "}";

        int expectedID = 0;

        PostMethod post = testServer.post("/moneytransfers", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());

        assertEquals(1, moneyTransferService.getMoneyTransfers().size());

        MoneyTransfer moneyTransfer = moneyTransferService.getMoneyTransfer(expectedID);
        assertNotNull(moneyTransfer);
        assertEquals(expectedID, moneyTransfer.getID());
        assertNotEquals(inputID, moneyTransfer.getID());

        Date expectedDate = moneyTransfer.getDate();
        String expectedDateString = getFormattedDate(expectedDate);
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                        "\"ID\":" + expectedID + "," +
                        "\"accountFromID\":0," +
                        "\"accountToID\":1," +
                        "\"transferValue\":" + transferValue + "," +
                        "\"date\":\"" + expectedDateString + "\"" +
                    "}" +
                "}";

        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    @Test
    public void testPostMethodAndDateNotSetFromJson() throws Exception {
        addAccountToService("Name1");
        addAccountToService("Name2");

        BigDecimal transferValue = BigDecimal.valueOf(1000.99);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(2018, Calendar.NOVEMBER, 14, 15, 0, 0);
        Date inputDate = cal.getTime();
        String inputJson = "{" +
                "\"accountFromID\":0," +
                "\"accountToID\":1," +
                "\"transferValue\":" + transferValue + "," +
                "\"date\":\"" + inputDate + "\"" +
                "}";

        PostMethod post = testServer.post("/moneytransfers", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());

        assertEquals(1, moneyTransferService.getMoneyTransfers().size());

        int expectedID = 0;
        MoneyTransfer moneyTransfer = moneyTransferService.getMoneyTransfer(expectedID);
        assertNotNull(moneyTransfer);
        assertNotEquals(getFormattedDate(inputDate), getFormattedDate(moneyTransfer.getDate()));


        Date expectedDate = moneyTransfer.getDate();
        String expectedDateString = getFormattedDate(expectedDate);
        String expectedJson = "{" +
                "\"status\":\"SUCCESS\"," +
                "\"data\":" +
                    "{" +
                        "\"ID\":" + expectedID + "," +
                        "\"accountFromID\":0," +
                        "\"accountToID\":1," +
                        "\"transferValue\":" + transferValue + "," +
                        "\"date\":\"" + expectedDateString + "\"" +
                    "}" +
                "}";

        assertEquals(expectedJson, getStringFromBody(httpResponse));
    }

    private String getFormattedDate(Date expectedDate) {
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.US).format(expectedDate);
    }

    @Test
    public void testPostMethodWithBadJsonRequest() throws Exception {
        String inputJson = "koza";
        String expectedJson = "{" +
                "\"status\":\"FAIL\"," +
                "\"message\":\"Error in request\"" +
                "}";

        PostMethod post = testServer.post("/moneytransfers", inputJson, false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(400, httpResponse.code());
        assertEquals(expectedJson, getStringFromBody(httpResponse));

        assertEquals(0, moneyTransferService.getMoneyTransfers().size());

        MoneyTransfer moneyTransfer = moneyTransferService.getMoneyTransfer(0);
        assertNull(moneyTransfer);
    }

    private void addAccountToService(String name) {
        Account account = new Account();
        account.setName(name);
        accountService.addAccount(account);
    }

    private MoneyTransfer addMoneyTransferToService(Integer accountFromID, Integer accountToID, BigDecimal transferValue) {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setAccountFromID(accountFromID);
        moneyTransfer.setAccountToID(accountToID);
        moneyTransfer.setTransferValue(transferValue);
        moneyTransferService.addMoneyTransfer(moneyTransfer);
        return  moneyTransfer;
    }

    private String getStringFromBody(HttpResponse httpResponse) {
        return new String(httpResponse.body());
    }

}