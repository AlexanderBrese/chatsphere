/*
 * ORY Hydra - Cloud Native OAuth 2.0 and OpenID Connect Server
 * Welcome to the ORY Hydra HTTP API documentation. You will find documentation for all HTTP APIs here. Keep in mind that this document reflects the latest branch, always. Support for versioned documentation is coming in the future.
 *
 * OpenAPI spec version: Latest
 * Contact: hi@ory.am
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.model.Version;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for VersionApi
 */
@Ignore
public class VersionApiTest {

    private final VersionApi api = new VersionApi();

    
    /**
     * Get the version of Hydra
     *
     * This endpoint returns the version as &#x60;{ \&quot;version\&quot;: \&quot;VERSION\&quot; }&#x60;. The version is only correct with the prebuilt binary and not custom builds.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getVersionTest() throws ApiException {
        Version response = api.getVersion();

        // TODO: test validations
    }
    
}
