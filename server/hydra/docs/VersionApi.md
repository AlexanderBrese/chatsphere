# VersionApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getVersion**](VersionApi.md#getVersion) | **GET** /version | Get the version of Hydra


<a name="getVersion"></a>
# **getVersion**
> Version getVersion()

Get the version of Hydra

This endpoint returns the version as &#x60;{ \&quot;version\&quot;: \&quot;VERSION\&quot; }&#x60;. The version is only correct with the prebuilt binary and not custom builds.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.VersionApi;


VersionApi apiInstance = new VersionApi();
try {
    Version result = apiInstance.getVersion();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling VersionApi#getVersion");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Version**](Version.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, application/x-www-form-urlencoded
 - **Accept**: application/json

