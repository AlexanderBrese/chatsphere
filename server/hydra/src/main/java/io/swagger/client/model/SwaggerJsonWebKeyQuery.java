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
 * SwaggerJsonWebKeyQuery
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-07-10T19:14:58.181+02:00")
public class SwaggerJsonWebKeyQuery {
  @SerializedName("kid")
  private String kid = null;

  @SerializedName("set")
  private String set = null;

  public SwaggerJsonWebKeyQuery kid(String kid) {
    this.kid = kid;
    return this;
  }

   /**
   * The kid of the desired key in: path
   * @return kid
  **/
  @ApiModelProperty(required = true, value = "The kid of the desired key in: path")
  public String getKid() {
    return kid;
  }

  public void setKid(String kid) {
    this.kid = kid;
  }

  public SwaggerJsonWebKeyQuery set(String set) {
    this.set = set;
    return this;
  }

   /**
   * The set in: path
   * @return set
  **/
  @ApiModelProperty(required = true, value = "The set in: path")
  public String getSet() {
    return set;
  }

  public void setSet(String set) {
    this.set = set;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SwaggerJsonWebKeyQuery swaggerJsonWebKeyQuery = (SwaggerJsonWebKeyQuery) o;
    return Objects.equals(this.kid, swaggerJsonWebKeyQuery.kid) &&
        Objects.equals(this.set, swaggerJsonWebKeyQuery.set);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kid, set);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SwaggerJsonWebKeyQuery {\n");
    
    sb.append("    kid: ").append(toIndentedString(kid)).append("\n");
    sb.append("    set: ").append(toIndentedString(set)).append("\n");
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

