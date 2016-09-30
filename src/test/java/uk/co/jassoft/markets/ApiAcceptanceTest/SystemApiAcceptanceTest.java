package uk.co.jassoft.markets.ApiAcceptanceTest;

import uk.co.jassoft.markets.client.SystemClient;
import uk.co.jassoft.markets.client.exception.ApiClientException;
import uk.co.jassoft.markets.datamodel.system.Queue;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by jonshaw on 11/05/2016.
 */
public class SystemApiAcceptanceTest extends ApiAcceptanceTestBase {

    @Test
    @Ignore ("Doesnt work with ARM activeMQ")
    public void testAdminUserCanGetQueueNames() throws ApiClientException {
        List<String> queues = new SystemClient().getQueueNames();

        Assert.assertFalse(queues.isEmpty());
    }

    @Test
    @Ignore ("Doesnt work with ARM activeMQ")
    public void testAdminUserCanGetQueueStats() throws ApiClientException {
        final SystemClient systemClient = new SystemClient();

        List<String> queues = systemClient.getQueueNames();

        for(String queue: queues) {
            Map<String, String> stats = systemClient.getQueueStats(queue);

            Assert.assertNotNull(stats.get("QueueSize"));
        }
    }

    @Test
    @Ignore ("Wont work yet as not all listerers are started for test")
    public void testAllListenersAreConnectedToQueue() throws ApiClientException {
        final SystemClient systemClient = new SystemClient();

        for(Queue queue : Queue.values()) {
            Map<String, String> stats = systemClient.getQueueStats(queue.name());

            Assert.assertNotNull(String.format("Queue [%s] has no Consumer Count", queue.name()), stats.get("ConsumerCount"));

            int consumerCount = Integer.parseInt(stats.get("ConsumerCount"));

            Assert.assertTrue(String.format("Queue [%s] has Consumer Count of [%s]", queue.name(), consumerCount), consumerCount > 0);
        }
    }

}
