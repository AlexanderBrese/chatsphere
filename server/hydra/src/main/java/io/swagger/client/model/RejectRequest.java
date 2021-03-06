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
 * RejectRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-07-10T19:14:58.181+02:00")
public class RejectRequest {
  @SerializedName("error")
  private String error = null;

  @SerializedName("error_debug")
  private String errorDebug = null;

  @SerializedName("error_description")
  private String errorDescription = null;

  @SerializedName("error_hint")
  private String errorHint = null;

  @SerializedName("status_code")
  private Long statusCode = null;

  public RejectRequest error(String error) {
    this.error = error;
    return this;
  }

   /**
   * Get error
   * @return error
  **/
  @ApiModelProperty(value = "")
  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public RejectRequest errorDebug(String errorDebug) {
    this.errorDebug = errorDebug;
    return this;
  }

   /**
   * Get errorDebug
   * @return errorDebug
  **/
  @ApiModelProperty(value = "")
  public String getErrorDebug() {
    return errorDebug;
  }

  public void setErrorDebug(String errorDebug) {
    this.errorDebug = errorDebug;
  }

  public RejectRequest errorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
    return this;
  }

   /**
   * Get errorDescription
   * @return errorDescription
  **/
  @ApiModelProperty(value = "")
  public String getErrorDescription() {
    return errorDescription;
  }

  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }

  public RejectRequest errorHint(String errorHint) {
    this.errorHint = errorHint;
    return this;
  }

   /**
   * Get errorHint
   * @return errorHint
  **/
  @ApiModelProperty(value = "")
  public String getErrorHint() {
    return errorHint;
  }

  public void setErrorHint(String errorHint) {
    this.errorHint = errorHint;
  }

  public RejectRequest statusCode(Long statusCode) {
    this.statusCode = statusCode;
    return this;
  }

   /**
   * Get statusCode
   * @return statusCode
  **/
  @ApiModelProperty(value = "")
  public Long getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Long statusCode) {
    this.statusCode = statusCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RejectRequest rejectRequest = (RejectRequest) o;
    return Objects.equals(this.error, rejectRequest.error) &&
        Objects.equals(this.errorDebug, rejectRequest.errorDebug) &&
        Objects.equals(this.errorDescription, rejectRequest.errorDescription) &&
        Objects.equals(this.errorHint, rejectRequest.errorHint) &&
        Objects.equals(this.statusCode, rejectRequest.statusCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(error, errorDebug, errorDescription, errorHint, statusCode);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RejectRequest {\n");
    
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    errorDebug: ").append(toIndentedString(errorDebug)).append("\n");
    sb.append("    errorDescription: ").append(toIndentedString(errorDescription)).append("\n");
    sb.append("    errorHint: ").append(toIndentedString(errorHint)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
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

