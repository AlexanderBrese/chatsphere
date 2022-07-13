# MetricsApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getPrometheusMetrics**](MetricsApi.md#getPrometheusMetrics) | **GET** /metrics/prometheus | Retrieve Prometheus metrics


<a name="getPrometheusMetrics"></a>
# **getPrometheusMetrics**
> getPrometheusMetrics()

Retrieve Prometheus metrics

This endpoint returns metrics formatted for Prometheus.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.MetricsApi;


MetricsApi apiInstance = new MetricsApi();
try {
    apiInstance.getPrometheusMetrics();
} catch (ApiException e) {
    System.err.println("Exception when calling MetricsApi#getPrometheusMetrics");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, application/x-www-form-urlencoded
 - **Accept**: application/json

