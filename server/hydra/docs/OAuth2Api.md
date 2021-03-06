# OAuth2Api

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**acceptConsentRequest**](OAuth2Api.md#acceptConsentRequest) | **PUT** /oauth2/auth/requests/consent/{challenge}/accept | Accept an consent request
[**acceptLoginRequest**](OAuth2Api.md#acceptLoginRequest) | **PUT** /oauth2/auth/requests/login/{challenge}/accept | Accept an login request
[**createOAuth2Client**](OAuth2Api.md#createOAuth2Client) | **POST** /clients | Create an OAuth 2.0 client
[**deleteOAuth2Client**](OAuth2Api.md#deleteOAuth2Client) | **DELETE** /clients/{id} | Deletes an OAuth 2.0 Client
[**flushInactiveOAuth2Tokens**](OAuth2Api.md#flushInactiveOAuth2Tokens) | **POST** /oauth2/flush | Flush Expired OAuth2 Access Tokens
[**getConsentRequest**](OAuth2Api.md#getConsentRequest) | **GET** /oauth2/auth/requests/consent/{challenge} | Get consent request information
[**getLoginRequest**](OAuth2Api.md#getLoginRequest) | **GET** /oauth2/auth/requests/login/{challenge} | Get an login request
[**getOAuth2Client**](OAuth2Api.md#getOAuth2Client) | **GET** /clients/{id} | Get an OAuth 2.0 Client.
[**getWellKnown**](OAuth2Api.md#getWellKnown) | **GET** /.well-known/openid-configuration | Server well known configuration
[**introspectOAuth2Token**](OAuth2Api.md#introspectOAuth2Token) | **POST** /oauth2/introspect | Introspect OAuth2 tokens
[**listOAuth2Clients**](OAuth2Api.md#listOAuth2Clients) | **GET** /clients | List OAuth 2.0 Clients
[**oauthAuth**](OAuth2Api.md#oauthAuth) | **GET** /oauth2/auth | The OAuth 2.0 authorize endpoint
[**oauthToken**](OAuth2Api.md#oauthToken) | **POST** /oauth2/token | The OAuth 2.0 token endpoint
[**rejectConsentRequest**](OAuth2Api.md#rejectConsentRequest) | **PUT** /oauth2/auth/requests/consent/{challenge}/reject | Reject an consent request
[**rejectLoginRequest**](OAuth2Api.md#rejectLoginRequest) | **PUT** /oauth2/auth/requests/login/{challenge}/reject | Reject an logout request
[**revokeOAuth2Token**](OAuth2Api.md#revokeOAuth2Token) | **POST** /oauth2/revoke | Revoke OAuth2 tokens
[**updateOAuth2Client**](OAuth2Api.md#updateOAuth2Client) | **PUT** /clients/{id} | Update an OAuth 2.0 Client
[**userinfo**](OAuth2Api.md#userinfo) | **POST** /userinfo | OpenID Connect Userinfo
[**wellKnown**](OAuth2Api.md#wellKnown) | **GET** /.well-known/jwks.json | Get Well-Known JSON Web Keys


<a name="acceptConsentRequest"></a>
# **acceptConsentRequest**
> CompletedRequest acceptConsentRequest(challenge, body)

Accept an consent request

When an authorization code, hybrid, or implicit OAuth 2.0 Flow is initiated, ORY Hydra asks the login provider to authenticate the user and then tell ORY Hydra now about it. If the user authenticated, he/she must now be asked if the OAuth 2.0 Client which initiated the flow should be allowed to access the resources on the user&#39;s behalf.  The consent provider which handles this request and is a web app implemented and hosted by you. It shows a user interface which asks the user to grant or deny the client access to the requested scope (\&quot;Application my-dropbox-app wants write access to all your private files\&quot;).  The consent challenge is appended to the consent provider&#39;s URL to which the user&#39;s user-agent (browser) is redirected to. The consent provider uses that challenge to fetch information on the OAuth2 request and then tells ORY Hydra if the user accepted or rejected the request.  This endpoint tells ORY Hydra that the user has authorized the OAuth 2.0 client to access resources on his/her behalf. The consent provider includes additional information, such as session data for access and ID tokens, and if the consent request should be used as basis for future requests.  The response contains a redirect URL which the consent provider should redirect the user-agent to.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
String challenge = "challenge_example"; // String | 
AcceptConsentRequest body = new AcceptConsentRequest(); // AcceptConsentRequest | 
try {
    CompletedRequest result = apiInstance.acceptConsentRequest(challenge, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#acceptConsentRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **challenge** | **String**|  |
 **body** | [**AcceptConsentRequest**](AcceptConsentRequest.md)|  | [optional]

### Return type

[**CompletedRequest**](CompletedRequest.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="acceptLoginRequest"></a>
# **acceptLoginRequest**
> CompletedRequest acceptLoginRequest(challenge, body)

Accept an login request

When an authorization code, hybrid, or implicit OAuth 2.0 Flow is initiated, ORY Hydra asks the login provider (sometimes called \&quot;identity provider\&quot;) to authenticate the user and then tell ORY Hydra now about it. The login provider is an web-app you write and host, and it must be able to authenticate (\&quot;show the user a login screen\&quot;) a user (in OAuth2 the proper name for user is \&quot;resource owner\&quot;).  The authentication challenge is appended to the login provider URL to which the user&#39;s user-agent (browser) is redirected to. The login provider uses that challenge to fetch information on the OAuth2 request and then accept or reject the requested authentication process.  This endpoint tells ORY Hydra that the user has successfully authenticated and includes additional information such as the user&#39;s ID and if ORY Hydra should remember the user&#39;s user agent for future authentication attempts by setting a cookie.  The response contains a redirect URL which the login provider should redirect the user-agent to.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
String challenge = "challenge_example"; // String | 
AcceptLoginRequest body = new AcceptLoginRequest(); // AcceptLoginRequest | 
try {
    CompletedRequest result = apiInstance.acceptLoginRequest(challenge, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#acceptLoginRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **challenge** | **String**|  |
 **body** | [**AcceptLoginRequest**](AcceptLoginRequest.md)|  | [optional]

### Return type

[**CompletedRequest**](CompletedRequest.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createOAuth2Client"></a>
# **createOAuth2Client**
> OAuth2Client createOAuth2Client(body)

Create an OAuth 2.0 client

Create a new OAuth 2.0 client If you pass &#x60;client_secret&#x60; the secret will be used, otherwise a random secret will be generated. The secret will be returned in the response and you will not be able to retrieve it later on. Write the secret down and keep it somwhere safe.  OAuth 2.0 clients are used to perform OAuth 2.0 and OpenID Connect flows. Usually, OAuth 2.0 clients are generated for applications which want to consume your OAuth 2.0 or OpenID Connect capabilities. To manage ORY Hydra, you will need an OAuth 2.0 Client as well. Make sure that this endpoint is well protected and only callable by first-party components.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
OAuth2Client body = new OAuth2Client(); // OAuth2Client | 
try {
    OAuth2Client result = apiInstance.createOAuth2Client(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#createOAuth2Client");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**OAuth2Client**](OAuth2Client.md)|  |

### Return type

[**OAuth2Client**](OAuth2Client.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteOAuth2Client"></a>
# **deleteOAuth2Client**
> deleteOAuth2Client(id)

Deletes an OAuth 2.0 Client

Delete an existing OAuth 2.0 Client by its ID.  OAuth 2.0 clients are used to perform OAuth 2.0 and OpenID Connect flows. Usually, OAuth 2.0 clients are generated for applications which want to consume your OAuth 2.0 or OpenID Connect capabilities. To manage ORY Hydra, you will need an OAuth 2.0 Client as well. Make sure that this endpoint is well protected and only callable by first-party components.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
String id = "id_example"; // String | The id of the OAuth 2.0 Client.
try {
    apiInstance.deleteOAuth2Client(id);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#deleteOAuth2Client");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The id of the OAuth 2.0 Client. |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="flushInactiveOAuth2Tokens"></a>
# **flushInactiveOAuth2Tokens**
> flushInactiveOAuth2Tokens(body)

Flush Expired OAuth2 Access Tokens

This endpoint flushes expired OAuth2 access tokens from the database. You can set a time after which no tokens will be not be touched, in case you want to keep recent tokens for auditing. Refresh tokens can not be flushed as they are deleted automatically when performing the refresh flow.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
FlushInactiveOAuth2TokensRequest body = new FlushInactiveOAuth2TokensRequest(); // FlushInactiveOAuth2TokensRequest | 
try {
    apiInstance.flushInactiveOAuth2Tokens(body);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#flushInactiveOAuth2Tokens");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**FlushInactiveOAuth2TokensRequest**](FlushInactiveOAuth2TokensRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getConsentRequest"></a>
# **getConsentRequest**
> ConsentRequest getConsentRequest(challenge)

Get consent request information

When an authorization code, hybrid, or implicit OAuth 2.0 Flow is initiated, ORY Hydra asks the login provider to authenticate the user and then tell ORY Hydra now about it. If the user authenticated, he/she must now be asked if the OAuth 2.0 Client which initiated the flow should be allowed to access the resources on the user&#39;s behalf.  The consent provider which handles this request and is a web app implemented and hosted by you. It shows a user interface which asks the user to grant or deny the client access to the requested scope (\&quot;Application my-dropbox-app wants write access to all your private files\&quot;).  The consent challenge is appended to the consent provider&#39;s URL to which the user&#39;s user-agent (browser) is redirected to. The consent provider uses that challenge to fetch information on the OAuth2 request and then tells ORY Hydra if the user accepted or rejected the request.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
String challenge = "challenge_example"; // String | 
try {
    ConsentRequest result = apiInstance.getConsentRequest(challenge);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#getConsentRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **challenge** | **String**|  |

### Return type

[**ConsentRequest**](ConsentRequest.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getLoginRequest"></a>
# **getLoginRequest**
> LoginRequest getLoginRequest(challenge)

Get an login request

When an authorization code, hybrid, or implicit OAuth 2.0 Flow is initiated, ORY Hydra asks the login provider (sometimes called \&quot;identity provider\&quot;) to authenticate the user and then tell ORY Hydra now about it. The login provider is an web-app you write and host, and it must be able to authenticate (\&quot;show the user a login screen\&quot;) a user (in OAuth2 the proper name for user is \&quot;resource owner\&quot;).  The authentication challenge is appended to the login provider URL to which the user&#39;s user-agent (browser) is redirected to. The login provider uses that challenge to fetch information on the OAuth2 request and then accept or reject the requested authentication process.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
String challenge = "challenge_example"; // String | 
try {
    LoginRequest result = apiInstance.getLoginRequest(challenge);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#getLoginRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **challenge** | **String**|  |

### Return type

[**LoginRequest**](LoginRequest.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getOAuth2Client"></a>
# **getOAuth2Client**
> OAuth2Client getOAuth2Client(id)

Get an OAuth 2.0 Client.

Get an OAUth 2.0 client by its ID. This endpoint never returns passwords.  OAuth 2.0 clients are used to perform OAuth 2.0 and OpenID Connect flows. Usually, OAuth 2.0 clients are generated for applications which want to consume your OAuth 2.0 or OpenID Connect capabilities. To manage ORY Hydra, you will need an OAuth 2.0 Client as well. Make sure that this endpoint is well protected and only callable by first-party components.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
String id = "id_example"; // String | The id of the OAuth 2.0 Client.
try {
    OAuth2Client result = apiInstance.getOAuth2Client(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#getOAuth2Client");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| The id of the OAuth 2.0 Client. |

### Return type

[**OAuth2Client**](OAuth2Client.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getWellKnown"></a>
# **getWellKnown**
> WellKnown getWellKnown()

Server well known configuration

The well known endpoint an be used to retrieve information for OpenID Connect clients. We encourage you to not roll your own OpenID Connect client but to use an OpenID Connect client library instead. You can learn more on this flow at https://openid.net/specs/openid-connect-discovery-1_0.html

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
try {
    WellKnown result = apiInstance.getWellKnown();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#getWellKnown");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**WellKnown**](WellKnown.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, application/x-www-form-urlencoded
 - **Accept**: application/json

<a name="introspectOAuth2Token"></a>
# **introspectOAuth2Token**
> OAuth2TokenIntrospection introspectOAuth2Token(token, scope)

Introspect OAuth2 tokens

The introspection endpoint allows to check if a token (both refresh and access) is active or not. An active token is neither expired nor revoked. If a token is active, additional information on the token will be included. You can set additional data for a token by setting &#x60;accessTokenExtra&#x60; during the consent flow.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OAuth2Api;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

OAuth2Api apiInstance = new OAuth2Api();
String token = "token_example"; // String | The string value of the token. For access tokens, this is the \"access_token\" value returned from the token endpoint defined in OAuth 2.0 [RFC6749], Section 5.1. This endpoint DOES NOT accept refresh tokens for validation.
String scope = "scope_example"; // String | An optional, space separated list of required scopes. If the access token was not granted one of the scopes, the result of active will be false.
try {
    OAuth2TokenIntrospection result = apiInstance.introspectOAuth2Token(token, scope);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#introspectOAuth2Token");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **token** | **String**| The string value of the token. For access tokens, this is the \&quot;access_token\&quot; value returned from the token endpoint defined in OAuth 2.0 [RFC6749], Section 5.1. This endpoint DOES NOT accept refresh tokens for validation. |
 **scope** | **String**| An optional, space separated list of required scopes. If the access token was not granted one of the scopes, the result of active will be false. | [optional]

### Return type

[**OAuth2TokenIntrospection**](OAuth2TokenIntrospection.md)

### Authorization

[basic](../README.md#basic), [oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/x-www-form-urlencoded
 - **Accept**: application/json

<a name="listOAuth2Clients"></a>
# **listOAuth2Clients**
> List&lt;OAuth2Client&gt; listOAuth2Clients(limit, offset)

List OAuth 2.0 Clients

This endpoint lists all clients in the database, and never returns client secrets.  OAuth 2.0 clients are used to perform OAuth 2.0 and OpenID Connect flows. Usually, OAuth 2.0 clients are generated for applications which want to consume your OAuth 2.0 or OpenID Connect capabilities. To manage ORY Hydra, you will need an OAuth 2.0 Client as well. Make sure that this endpoint is well protected and only callable by first-party components.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
Long limit = 789L; // Long | The maximum amount of policies returned.
Long offset = 789L; // Long | The offset from where to start looking.
try {
    List<OAuth2Client> result = apiInstance.listOAuth2Clients(limit, offset);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#listOAuth2Clients");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **limit** | **Long**| The maximum amount of policies returned. | [optional]
 **offset** | **Long**| The offset from where to start looking. | [optional]

### Return type

[**List&lt;OAuth2Client&gt;**](OAuth2Client.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="oauthAuth"></a>
# **oauthAuth**
> oauthAuth()

The OAuth 2.0 authorize endpoint

This endpoint is not documented here because you should never use your own implementation to perform OAuth2 flows. OAuth2 is a very popular protocol and a library for your programming language will exists.  To learn more about this flow please refer to the specification: https://tools.ietf.org/html/rfc6749

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
try {
    apiInstance.oauthAuth();
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#oauthAuth");
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

 - **Content-Type**: application/x-www-form-urlencoded
 - **Accept**: application/json

<a name="oauthToken"></a>
# **oauthToken**
> OauthTokenResponse oauthToken()

The OAuth 2.0 token endpoint

This endpoint is not documented here because you should never use your own implementation to perform OAuth2 flows. OAuth2 is a very popular protocol and a library for your programming language will exists.  To learn more about this flow please refer to the specification: https://tools.ietf.org/html/rfc6749

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OAuth2Api;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

OAuth2Api apiInstance = new OAuth2Api();
try {
    OauthTokenResponse result = apiInstance.oauthToken();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#oauthToken");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**OauthTokenResponse**](OauthTokenResponse.md)

### Authorization

[basic](../README.md#basic), [oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/x-www-form-urlencoded
 - **Accept**: application/json

<a name="rejectConsentRequest"></a>
# **rejectConsentRequest**
> CompletedRequest rejectConsentRequest(challenge, body)

Reject an consent request

When an authorization code, hybrid, or implicit OAuth 2.0 Flow is initiated, ORY Hydra asks the login provider to authenticate the user and then tell ORY Hydra now about it. If the user authenticated, he/she must now be asked if the OAuth 2.0 Client which initiated the flow should be allowed to access the resources on the user&#39;s behalf.  The consent provider which handles this request and is a web app implemented and hosted by you. It shows a user interface which asks the user to grant or deny the client access to the requested scope (\&quot;Application my-dropbox-app wants write access to all your private files\&quot;).  The consent challenge is appended to the consent provider&#39;s URL to which the user&#39;s user-agent (browser) is redirected to. The consent provider uses that challenge to fetch information on the OAuth2 request and then tells ORY Hydra if the user accepted or rejected the request.  This endpoint tells ORY Hydra that the user has not authorized the OAuth 2.0 client to access resources on his/her behalf. The consent provider must include a reason why the consent was not granted.  The response contains a redirect URL which the consent provider should redirect the user-agent to.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
String challenge = "challenge_example"; // String | 
RejectRequest body = new RejectRequest(); // RejectRequest | 
try {
    CompletedRequest result = apiInstance.rejectConsentRequest(challenge, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#rejectConsentRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **challenge** | **String**|  |
 **body** | [**RejectRequest**](RejectRequest.md)|  | [optional]

### Return type

[**CompletedRequest**](CompletedRequest.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="rejectLoginRequest"></a>
# **rejectLoginRequest**
> CompletedRequest rejectLoginRequest(challenge, body)

Reject an logout request

When an authorization code, hybrid, or implicit OAuth 2.0 Flow is initiated, ORY Hydra asks the login provider (sometimes called \&quot;identity provider\&quot;) to authenticate the user and then tell ORY Hydra now about it. The login provider is an web-app you write and host, and it must be able to authenticate (\&quot;show the user a login screen\&quot;) a user (in OAuth2 the proper name for user is \&quot;resource owner\&quot;).  The authentication challenge is appended to the login provider URL to which the user&#39;s user-agent (browser) is redirected to. The login provider uses that challenge to fetch information on the OAuth2 request and then accept or reject the requested authentication process.  This endpoint tells ORY Hydra that the user has not authenticated and includes a reason why the authentication was be denied.  The response contains a redirect URL which the login provider should redirect the user-agent to.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
String challenge = "challenge_example"; // String | 
RejectRequest body = new RejectRequest(); // RejectRequest | 
try {
    CompletedRequest result = apiInstance.rejectLoginRequest(challenge, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#rejectLoginRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **challenge** | **String**|  |
 **body** | [**RejectRequest**](RejectRequest.md)|  | [optional]

### Return type

[**CompletedRequest**](CompletedRequest.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="revokeOAuth2Token"></a>
# **revokeOAuth2Token**
> revokeOAuth2Token(token)

Revoke OAuth2 tokens

Revoking a token (both access and refresh) means that the tokens will be invalid. A revoked access token can no longer be used to make access requests, and a revoked refresh token can no longer be used to refresh an access token. Revoking a refresh token also invalidates the access token that was created with it.

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OAuth2Api;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

OAuth2Api apiInstance = new OAuth2Api();
String token = "token_example"; // String | 
try {
    apiInstance.revokeOAuth2Token(token);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#revokeOAuth2Token");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **token** | **String**|  |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic), [oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/x-www-form-urlencoded
 - **Accept**: application/json

<a name="updateOAuth2Client"></a>
# **updateOAuth2Client**
> OAuth2Client updateOAuth2Client(id, body)

Update an OAuth 2.0 Client

Update an existing OAuth 2.0 Client. If you pass &#x60;client_secret&#x60; the secret will be updated and returned via the API. This is the only time you will be able to retrieve the client secret, so write it down and keep it safe.  OAuth 2.0 clients are used to perform OAuth 2.0 and OpenID Connect flows. Usually, OAuth 2.0 clients are generated for applications which want to consume your OAuth 2.0 or OpenID Connect capabilities. To manage ORY Hydra, you will need an OAuth 2.0 Client as well. Make sure that this endpoint is well protected and only callable by first-party components.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
String id = "id_example"; // String | 
OAuth2Client body = new OAuth2Client(); // OAuth2Client | 
try {
    OAuth2Client result = apiInstance.updateOAuth2Client(id, body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#updateOAuth2Client");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**|  |
 **body** | [**OAuth2Client**](OAuth2Client.md)|  |

### Return type

[**OAuth2Client**](OAuth2Client.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="userinfo"></a>
# **userinfo**
> UserinfoResponse userinfo()

OpenID Connect Userinfo

This endpoint returns the payload of the ID Token, including the idTokenExtra values, of the provided OAuth 2.0 access token. The endpoint implements http://openid.net/specs/openid-connect-core-1_0.html#UserInfo .

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OAuth2Api;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

OAuth2Api apiInstance = new OAuth2Api();
try {
    UserinfoResponse result = apiInstance.userinfo();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#userinfo");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**UserinfoResponse**](UserinfoResponse.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json, application/x-www-form-urlencoded
 - **Accept**: application/json

<a name="wellKnown"></a>
# **wellKnown**
> JsonWebKeySet wellKnown()

Get Well-Known JSON Web Keys

Returns metadata for discovering important JSON Web Keys. Currently, this endpoint returns the public key for verifying OpenID Connect ID Tokens.  A JSON Web Key (JWK) is a JavaScript Object Notation (JSON) data structure that represents a cryptographic key. A JWK Set is a JSON data structure that represents a set of JWKs. A JSON Web Key is identified by its set and key id. ORY Hydra uses this functionality to store cryptographic keys used for TLS and JSON Web Tokens (such as OpenID Connect ID tokens), and allows storing user-defined keys as well.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.OAuth2Api;


OAuth2Api apiInstance = new OAuth2Api();
try {
    JsonWebKeySet result = apiInstance.wellKnown();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OAuth2Api#wellKnown");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**JsonWebKeySet**](JsonWebKeySet.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

