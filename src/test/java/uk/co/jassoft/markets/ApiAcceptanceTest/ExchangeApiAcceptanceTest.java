package uk.co.jassoft.markets.ApiAcceptanceTest;

import uk.co.jassoft.markets.client.CompanyClient;
import uk.co.jassoft.markets.client.ExchangeClient;
import uk.co.jassoft.markets.client.exception.ApiClientException;
import uk.co.jassoft.markets.datamodel.company.Exchange;
import uk.co.jassoft.markets.datamodel.company.Exchanges;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by jonshaw on 11/05/2016.
 */
public class ExchangeApiAcceptanceTest extends ApiAcceptanceTestBase {

    private final static int NUMBER_OF_CHECKS_LIMIT = 60;
    private final static int CHECK_SLEEP_PERIOD = 2000;

    @BeforeClass
    public static void findCompaies() throws ApiClientException, InterruptedException {
        new CompanyClient().findCompanies();

        int count = 0;

        while(true) {

            if (count > NUMBER_OF_CHECKS_LIMIT) {
                Assert.fail("No New Exchanges Added");
            }

            Exchanges exchanges = new ExchangeClient().retrieveAllExchanges();

            if(!exchanges.isEmpty()) {
                break;
            }

            Thread.sleep(CHECK_SLEEP_PERIOD);

            count++;
        }
    }

    @Test
    public void testExchangesFromWebServiceAreSuccessfullyImportedAsDisabled() throws ApiClientException, InterruptedException {

        Exchanges exchanges = new ExchangeClient().retrieveExchanges();

        for(Exchange exchange : exchanges) {
            Assert.assertTrue(exchange.isEnabled());
        }
    }

    @Test
    public void testExchangeDetailIsReturned() throws ApiClientException, InterruptedException {

        Exchanges exchanges = new ExchangeClient().retrieveAllExchanges();

        Assert.assertNotNull(new ExchangeClient().retrieveExchange(exchanges.get(0).getId()).getEntity());
    }

    @Test
    public void testDisabledExchangesAreShownToAdmin() throws ApiClientException, InterruptedException {

        Exchanges exchanges = new ExchangeClient().retrieveAllExchanges();

        Assert.assertTrue(exchanges.stream().filter(exchange -> !exchange.isEnabled()).findAny().isPresent());
    }

    @Test
    public void testExchangeCanBeEnabled() throws ApiClientException, InterruptedException {

        Exchanges exchanges = new ExchangeClient().retrieveAllExchanges();

        Assert.assertTrue(exchanges.stream().filter(exchange -> !exchange.isEnabled()).findAny().isPresent());

        Exchange exchangeToTest = exchanges.stream().filter(exchange -> !exchange.isEnabled()).findAny().get();

        ExchangeClient exchangeClient = new ExchangeClient().retrieveExchange(exchangeToTest.getId());

        Assert.assertFalse(exchangeClient.getEntity().isEnabled());

        exchangeClient.enableExchange();

        Assert.assertTrue(exchangeClient.getEntity().isEnabled());
    }

    @Test
    public void testExchangeCanBeDisabled() throws ApiClientException, InterruptedException {

        Exchanges exchanges = new ExchangeClient().retrieveAllExchanges();

//        Enable an exchange
        Exchange exchangeToEnable = exchanges.stream().filter(exchange -> !exchange.isEnabled()).findAny().get();
        ExchangeClient exchangeToEnableClient = new ExchangeClient().retrieveExchange(exchangeToEnable.getId());
        exchangeToEnableClient.enableExchange();

        exchanges = new ExchangeClient().retrieveAllExchanges();

        Assert.assertTrue(exchanges.stream().filter(exchange -> exchange.isEnabled()).findAny().isPresent());

        Exchange exchangeToTest = exchanges.stream().filter(exchange -> exchange.isEnabled()).findAny().get();

        ExchangeClient exchangeClient = new ExchangeClient().retrieveExchange(exchangeToTest.getId());

        Assert.assertTrue(exchangeClient.getEntity().isEnabled());

        exchangeClient.disableExchange();

        Assert.assertFalse(exchangeClient.getEntity().isEnabled());
    }

}
