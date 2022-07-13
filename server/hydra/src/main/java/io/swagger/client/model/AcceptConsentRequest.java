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
import io.swagger.client.model.ConsentRequestSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AcceptConsentRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-07-10T19:14:58.181+02:00")
public class AcceptConsentRequest {
  @SerializedName("grant_scope")
  private List<String> grantScope = null;

  @SerializedName("remember")
  private Boolean remember = null;

  @SerializedName("remember_for")
  private Long rememberFor = null;

  @SerializedName("session")
  private ConsentRequestSession session = null;

  public AcceptConsentRequest grantScope(List<String> grantScope) {
    this.grantScope = grantScope;
    return this;
  }

  public AcceptConsentRequest addGrantScopeItem(String grantScopeItem) {
    if (this.grantScope == null) {
      this.grantScope = new ArrayList<String>();
    }
    this.grantScope.add(grantScopeItem);
    return this;
  }

   /**
   * GrantScope sets the scope the user authorized the client to use. Should be a subset of &#x60;requested_scope&#x60;
   * @return grantScope
  **/
  @ApiModelProperty(value = "GrantScope sets the scope the user authorized the client to use. Should be a subset of `requested_scope`")
  public List<String> getGrantScope() {
    return grantScope;
  }

  public void setGrantScope(List<String> grantScope) {
    this.grantScope = grantScope;
  }

  public AcceptConsentRequest remember(Boolean remember) {
    this.remember = remember;
    return this;
  }

   /**
   * Remember, if set to true, tells ORY Hydra to remember this consent authorization and reuse it if the same client asks the same user for the same, or a subset of, scope.
   * @return remember
  **/
  @ApiModelProperty(value = "Remember, if set to true, tells ORY Hydra to remember this consent authorization and reuse it if the same client asks the same user for the same, or a subset of, scope.")
  public Boolean isRemember() {
    return remember;
  }

  public void setRemember(Boolean remember) {
    this.remember = remember;
  }

  public AcceptConsentRequest rememberFor(Long rememberFor) {
    this.rememberFor = rememberFor;
    return this;
  }

   /**
   * RememberFor sets how long the consent authorization should be remembered for in seconds. If set to &#x60;0&#x60;, the authorization will be remembered indefinitely.
   * @return rememberFor
  **/
  @ApiModelProperty(value = "RememberFor sets how long the consent authorization should be remembered for in seconds. If set to `0`, the authorization will be remembered indefinitely.")
  public Long getRememberFor() {
    return rememberFor;
  }

  public void setRememberFor(Long rememberFor) {
    this.rememberFor = rememberFor;
  }

  public AcceptConsentRequest session(ConsentRequestSession session) {
    this.session = session;
    return this;
  }

   /**
   * Get session
   * @return session
  **/
  @ApiModelProperty(value = "")
  public ConsentRequestSession getSession() {
    return session;
  }

  public void setSession(ConsentRequestSession session) {
    this.session = session;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcceptConsentRequest acceptConsentRequest = (AcceptConsentRequest) o;
    return Objects.equals(this.grantScope, acceptConsentRequest.grantScope) &&
        Objects.equals(this.remember, acceptConsentRequest.remember) &&
        Objects.equals(this.rememberFor, acceptConsentRequest.rememberFor) &&
        Objects.equals(this.session, acceptConsentRequest.session);
  }

  @Override
  public int hashCode() {
    return Objects.hash(grantScope, remember, rememberFor, session);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AcceptConsentRequest {\n");
    
    sb.append("    grantScope: ").append(toIndentedString(grantScope)).append("\n");
    sb.append("    remember: ").append(toIndentedString(remember)).append("\n");
    sb.append("    rememberFor: ").append(toIndentedString(rememberFor)).append("\n");
    sb.append("    session: ").append(toIndentedString(session)).append("\n");
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
