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


package io.swagger.client.model;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

/**
 * SwaggerOAuthIntrospectionRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-07-10T19:14:58.181+02:00")
public class SwaggerOAuthIntrospectionRequest {
  @SerializedName("scope")
  private String scope = null;

  @SerializedName("token")
  private String token = null;

  public SwaggerOAuthIntrospectionRequest scope(String scope) {
    this.scope = scope;
    return this;
  }

   /**
   * An optional, space separated list of required scopes. If the access token was not granted one of the scopes, the result of active will be false.  in: formData
   * @return scope
  **/
  @ApiModelProperty(value = "An optional, space separated list of required scopes. If the access token was not granted one of the scopes, the result of active will be false.  in: formData")
  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public SwaggerOAuthIntrospectionRequest token(String token) {
    this.token = token;
    return this;
  }

   /**
   * The string value of the token. For access tokens, this is the \&quot;access_token\&quot; value returned from the token endpoint defined in OAuth 2.0 [RFC6749], Section 5.1. This endpoint DOES NOT accept refresh tokens for validation.
   * @return token
  **/
  @ApiModelProperty(required = true, value = "The string value of the token. For access tokens, this is the \"access_token\" value returned from the token endpoint defined in OAuth 2.0 [RFC6749], Section 5.1. This endpoint DOES NOT accept refresh tokens for validation.")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SwaggerOAuthIntrospectionRequest swaggerOAuthIntrospectionRequest = (SwaggerOAuthIntrospectionRequest) o;
    return Objects.equals(this.scope, swaggerOAuthIntrospectionRequest.scope) &&
        Objects.equals(this.token, swaggerOAuthIntrospectionRequest.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scope, token);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SwaggerOAuthIntrospectionRequest {\n");
    
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

