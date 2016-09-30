package com.jassoft.markets.ApiAcceptanceTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by jonshaw on 11/05/2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserApiAcceptanceTest.class,
        ExchangeApiAcceptanceTest.class,
        SystemApiAcceptanceTest.class,
        SourceApiAcceptanceTest.class
})
public class ApiTestSuite {
}
