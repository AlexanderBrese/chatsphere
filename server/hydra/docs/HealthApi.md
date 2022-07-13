# HealthApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**isInstanceAlive**](HealthApi.md#isInstanceAlive) | **GET** /health/alive | Check the Alive Status
[**isInstanceReady**](HealthApi.md#isInstanceReady) | **GET** /health/ready | Check the Readiness Status


<a name="isInstanceAlive"></a>
# **isInstanceAlive**
> HealthStatus isInstanceAlive()

Check the Alive Status

This endpoint returns a 200 status code when the HTTP server is up running. This status does currently not include checks whether the database connection is working. This endpoint does not require the &#x60;X-Forwarded-Proto&#x60; header when TLS termination is set.  Be aware that if you are running multiple nodes of ORY Hydra, the health status will never refer to the cluster state, only to a single instance.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.HealthApi;


HealthApi apiInstance = new HealthApi();
try {
    HealthStatus result = apiInstance.isInstanceAlive();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling HealthApi#isInstanceAlive");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**HealthStatus**](HealthStatus.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, application/x-www-form-urlencoded
 - **Accept**: application/json

<a name="isInstanceReady"></a>
# **isInstanceReady**
> HealthStatus isInstanceReady()

Check the Readiness Status

This endpoint returns a 200 status code when the HTTP server is up running and the environment dependencies (e.g. the database) are responsive as well.  This status does currently not include checks whether the database connection is working. This endpoint does not require the &#x60;X-Forwarded-Proto&#x60; header when TLS termination is set.  Be aware that if you are running multiple nodes of ORY Hydra, the health status will never refer to the cluster state, only to a single instance.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.HealthApi;


HealthApi apiInstance = new HealthApi();
try {
    HealthStatus result = apiInstance.isInstanceReady();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling HealthApi#isInstanceReady");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**HealthStatus**](HealthStatus.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, application/x-www-form-urlencoded
 - **Accept**: application/json

