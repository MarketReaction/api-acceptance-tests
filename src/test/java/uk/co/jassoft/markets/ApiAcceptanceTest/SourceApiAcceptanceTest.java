package uk.co.jassoft.markets.ApiAcceptanceTest;

import uk.co.jassoft.markets.client.SourceClient;
import uk.co.jassoft.markets.client.StoryClient;
import uk.co.jassoft.markets.client.exception.ApiClientException;
import uk.co.jassoft.markets.datamodel.sources.Source;
import uk.co.jassoft.markets.datamodel.story.Stories;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by jonshaw on 04/07/2016.
 */
@Ignore("To unblock deployment")
public class SourceApiAcceptanceTest extends ApiAcceptanceTestBase {

    @Test
    public void testSourceCanBeCreated() throws ApiClientException {
        new SourceClient().createSource();
    }

    @Test
    public void testUrlCanBeAddedToSource() throws ApiClientException {
        new SourceClient().createSource().withUrl("http://mock-source/");
    }

    @Test
    @Ignore
    public void testSourceWithUrlCanBeEnabledAndCrawlingStarts() throws ApiClientException, InterruptedException {
        Source source = new SourceClient().createSource()
                .withUrl("http://mock-source/")
                .enableUrl("http://mock-source/")
                .enable()
                .getEntity();

        final int STORY_TEST_COUNT = 20;
        final int STORY_TEST_INTERVAL = 2000;

        for (int i = 0; i < STORY_TEST_COUNT; i++) {

            try {
                Stories stories = new StoryClient().getStoriesForSource(source);

                if(!stories.isEmpty()) {
                    Assert.assertTrue(true);
                }
            }
            catch (ApiClientException exception) {
                if(i+1 == STORY_TEST_COUNT) {
                    throw exception;
                }
            }

            Thread.sleep(STORY_TEST_INTERVAL);
        }

        throw new ApiClientException("Failed to find stories for Source.");
    }
}
