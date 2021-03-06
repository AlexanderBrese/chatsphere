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
import org.threeten.bp.OffsetDateTime;

/**
 * AuthenticationSession
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-07-10T19:14:58.181+02:00")
public class AuthenticationSession {
  @SerializedName("AuthenticatedAt")
  private OffsetDateTime authenticatedAt = null;

  @SerializedName("ID")
  private String ID = null;

  @SerializedName("Subject")
  private String subject = null;

  public AuthenticationSession authenticatedAt(OffsetDateTime authenticatedAt) {
    this.authenticatedAt = authenticatedAt;
    return this;
  }

   /**
   * Get authenticatedAt
   * @return authenticatedAt
  **/
  @ApiModelProperty(value = "")
  public OffsetDateTime getAuthenticatedAt() {
    return authenticatedAt;
  }

  public void setAuthenticatedAt(OffsetDateTime authenticatedAt) {
    this.authenticatedAt = authenticatedAt;
  }

  public AuthenticationSession ID(String ID) {
    this.ID = ID;
    return this;
  }

   /**
   * Get ID
   * @return ID
  **/
  @ApiModelProperty(value = "")
  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public AuthenticationSession subject(String subject) {
    this.subject = subject;
    return this;
  }

   /**
   * Get subject
   * @return subject
  **/
  @ApiModelProperty(value = "")
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthenticationSession authenticationSession = (AuthenticationSession) o;
    return Objects.equals(this.authenticatedAt, authenticationSession.authenticatedAt) &&
        Objects.equals(this.ID, authenticationSession.ID) &&
        Objects.equals(this.subject, authenticationSession.subject);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authenticatedAt, ID, subject);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthenticationSession {\n");
    
    sb.append("    authenticatedAt: ").append(toIndentedString(authenticatedAt)).append("\n");
    sb.append("    ID: ").append(toIndentedString(ID)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
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

