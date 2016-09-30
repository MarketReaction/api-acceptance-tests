package com.jassoft.markets.ApiAcceptanceTest;

import com.jassoft.markets.client.SystemClient;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jonshaw on 19/08/15.
 */
public class ApiAcceptanceTestBase {

    private static final Logger LOG = LoggerFactory.getLogger(ApiAcceptanceTestBase.class);

    @BeforeClass
    public static void isApiActive() throws Exception {
        new SystemClient().validateApiStatus();
    }

}
