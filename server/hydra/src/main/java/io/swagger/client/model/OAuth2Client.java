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
import java.util.ArrayList;
import java.util.List;

/**
 * OAuth2Client
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-07-10T19:14:58.181+02:00")
public class OAuth2Client {
  @SerializedName("client_name")
  private String clientName = null;

  @SerializedName("client_secret")
  private String clientSecret = null;

  @SerializedName("client_secret_expires_at")
  private Long clientSecretExpiresAt = null;

  @SerializedName("client_uri")
  private String clientUri = null;

  @SerializedName("contacts")
  private List<String> contacts = null;

  @SerializedName("grant_types")
  private List<String> grantTypes = null;

  @SerializedName("id")
  private String id = null;

  @SerializedName("logo_uri")
  private String logoUri = null;

  @SerializedName("owner")
  private String owner = null;

  @SerializedName("policy_uri")
  private String policyUri = null;

  @SerializedName("public")
  private Boolean _public = null;

  @SerializedName("redirect_uris")
  private List<String> redirectUris = null;

  @SerializedName("response_types")
  private List<String> responseTypes = null;

  @SerializedName("scope")
  private String scope = null;

  @SerializedName("tos_uri")
  private String tosUri = null;

  public OAuth2Client clientName(String clientName) {
    this.clientName = clientName;
    return this;
  }

   /**
   * Name is the human-readable string name of the client to be presented to the end-user during authorization.
   * @return clientName
  **/
  @ApiModelProperty(value = "Name is the human-readable string name of the client to be presented to the end-user during authorization.")
  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public OAuth2Client clientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
    return this;
  }

   /**
   * Secret is the client&#39;s secret. The secret will be included in the create request as cleartext, and then never again. The secret is stored using BCrypt so it is impossible to recover it. Tell your users that they need to write the secret down as it will not be made available again.
   * @return clientSecret
  **/
  @ApiModelProperty(value = "Secret is the client's secret. The secret will be included in the create request as cleartext, and then never again. The secret is stored using BCrypt so it is impossible to recover it. Tell your users that they need to write the secret down as it will not be made available again.")
  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public OAuth2Client clientSecretExpiresAt(Long clientSecretExpiresAt) {
    this.clientSecretExpiresAt = clientSecretExpiresAt;
    return this;
  }

   /**
   * SecretExpiresAt is an integer holding the time at which the client secret will expire or 0 if it will not expire. The time is represented as the number of seconds from 1970-01-01T00:00:00Z as measured in UTC until the date/time of expiration.
   * @return clientSecretExpiresAt
  **/
  @ApiModelProperty(value = "SecretExpiresAt is an integer holding the time at which the client secret will expire or 0 if it will not expire. The time is represented as the number of seconds from 1970-01-01T00:00:00Z as measured in UTC until the date/time of expiration.")
  public Long getClientSecretExpiresAt() {
    return clientSecretExpiresAt;
  }

  public void setClientSecretExpiresAt(Long clientSecretExpiresAt) {
    this.clientSecretExpiresAt = clientSecretExpiresAt;
  }

  public OAuth2Client clientUri(String clientUri) {
    this.clientUri = clientUri;
    return this;
  }

   /**
   * ClientURI is an URL string of a web page providing information about the client. If present, the server SHOULD display this URL to the end-user in a clickable fashion.
   * @return clientUri
  **/
  @ApiModelProperty(value = "ClientURI is an URL string of a web page providing information about the client. If present, the server SHOULD display this URL to the end-user in a clickable fashion.")
  public String getClientUri() {
    return clientUri;
  }

  public void setClientUri(String clientUri) {
    this.clientUri = clientUri;
  }

  public OAuth2Client contacts(List<String> contacts) {
    this.contacts = contacts;
    return this;
  }

  public OAuth2Client addContactsItem(String contactsItem) {
    if (this.contacts == null) {
      this.contacts = new ArrayList<String>();
    }
    this.contacts.add(contactsItem);
    return this;
  }

   /**
   * Contacts is a array of strings representing ways to contact people responsible for this client, typically email addresses.
   * @return contacts
  **/
  @ApiModelProperty(value = "Contacts is a array of strings representing ways to contact people responsible for this client, typically email addresses.")
  public List<String> getContacts() {
    return contacts;
  }

  public void setContacts(List<String> contacts) {
    this.contacts = contacts;
  }

  public OAuth2Client grantTypes(List<String> grantTypes) {
    this.grantTypes = grantTypes;
    return this;
  }

  public OAuth2Client addGrantTypesItem(String grantTypesItem) {
    if (this.grantTypes == null) {
      this.grantTypes = new ArrayList<String>();
    }
    this.grantTypes.add(grantTypesItem);
    return this;
  }

   /**
   * GrantTypes is an array of grant types the client is allowed to use.
   * @return grantTypes
  **/
  @ApiModelProperty(value = "GrantTypes is an array of grant types the client is allowed to use.")
  public List<String> getGrantTypes() {
    return grantTypes;
  }

  public void setGrantTypes(List<String> grantTypes) {
    this.grantTypes = grantTypes;
  }

  public OAuth2Client id(String id) {
    this.id = id;
    return this;
  }

   /**
   * ID is the id for this client.
   * @return id
  **/
  @ApiModelProperty(value = "ID is the id for this client.")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public OAuth2Client logoUri(String logoUri) {
    this.logoUri = logoUri;
    return this;
  }

   /**
   * LogoURI is an URL string that references a logo for the client.
   * @return logoUri
  **/
  @ApiModelProperty(value = "LogoURI is an URL string that references a logo for the client.")
  public String getLogoUri() {
    return logoUri;
  }

  public void setLogoUri(String logoUri) {
    this.logoUri = logoUri;
  }

  public OAuth2Client owner(String owner) {
    this.owner = owner;
    return this;
  }

   /**
   * Owner is a string identifying the owner of the OAuth 2.0 Client.
   * @return owner
  **/
  @ApiModelProperty(value = "Owner is a string identifying the owner of the OAuth 2.0 Client.")
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public OAuth2Client policyUri(String policyUri) {
    this.policyUri = policyUri;
    return this;
  }

   /**
   * PolicyURI is a URL string that points to a human-readable privacy policy document that describes how the deployment organization collects, uses, retains, and discloses personal data.
   * @return policyUri
  **/
  @ApiModelProperty(value = "PolicyURI is a URL string that points to a human-readable privacy policy document that describes how the deployment organization collects, uses, retains, and discloses personal data.")
  public String getPolicyUri() {
    return policyUri;
  }

  public void setPolicyUri(String policyUri) {
    this.policyUri = policyUri;
  }

  public OAuth2Client _public(Boolean _public) {
    this._public = _public;
    return this;
  }

   /**
   * Public is a boolean that identifies this client as public, meaning that it does not have a secret. It will disable the client_credentials grant type for this client if set.
   * @return _public
  **/
  @ApiModelProperty(value = "Public is a boolean that identifies this client as public, meaning that it does not have a secret. It will disable the client_credentials grant type for this client if set.")
  public Boolean isPublic() {
    return _public;
  }

  public void setPublic(Boolean _public) {
    this._public = _public;
  }

  public OAuth2Client redirectUris(List<String> redirectUris) {
    this.redirectUris = redirectUris;
    return this;
  }

  public OAuth2Client addRedirectUrisItem(String redirectUrisItem) {
    if (this.redirectUris == null) {
      this.redirectUris = new ArrayList<String>();
    }
    this.redirectUris.add(redirectUrisItem);
    return this;
  }

   /**
   * RedirectURIs is an array of allowed redirect urls for the client, for example http://mydomain/oauth/callback .
   * @return redirectUris
  **/
  @ApiModelProperty(value = "RedirectURIs is an array of allowed redirect urls for the client, for example http://mydomain/oauth/callback .")
  public List<String> getRedirectUris() {
    return redirectUris;
  }

  public void setRedirectUris(List<String> redirectUris) {
    this.redirectUris = redirectUris;
  }

  public OAuth2Client responseTypes(List<String> responseTypes) {
    this.responseTypes = responseTypes;
    return this;
  }

  public OAuth2Client addResponseTypesItem(String responseTypesItem) {
    if (this.responseTypes == null) {
      this.responseTypes = new ArrayList<String>();
    }
    this.responseTypes.add(responseTypesItem);
    return this;
  }

   /**
   * ResponseTypes is an array of the OAuth 2.0 response type strings that the client can use at the authorization endpoint.
   * @return responseTypes
  **/
  @ApiModelProperty(value = "ResponseTypes is an array of the OAuth 2.0 response type strings that the client can use at the authorization endpoint.")
  public List<String> getResponseTypes() {
    return responseTypes;
  }

  public void setResponseTypes(List<String> responseTypes) {
    this.responseTypes = responseTypes;
  }

  public OAuth2Client scope(String scope) {
    this.scope = scope;
    return this;
  }

   /**
   * Scope is a string containing a space-separated list of scope values (as described in Section 3.3 of OAuth 2.0 [RFC6749]) that the client can use when requesting access tokens.
   * @return scope
  **/
  @ApiModelProperty(value = "Scope is a string containing a space-separated list of scope values (as described in Section 3.3 of OAuth 2.0 [RFC6749]) that the client can use when requesting access tokens.")
  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public OAuth2Client tosUri(String tosUri) {
    this.tosUri = tosUri;
    return this;
  }

   /**
   * TermsOfServiceURI is a URL string that points to a human-readable terms of service document for the client that describes a contractual relationship between the end-user and the client that the end-user accepts when authorizing the client.
   * @return tosUri
  **/
  @ApiModelProperty(value = "TermsOfServiceURI is a URL string that points to a human-readable terms of service document for the client that describes a contractual relationship between the end-user and the client that the end-user accepts when authorizing the client.")
  public String getTosUri() {
    return tosUri;
  }

  public void setTosUri(String tosUri) {
    this.tosUri = tosUri;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OAuth2Client oAuth2Client = (OAuth2Client) o;
    return Objects.equals(this.clientName, oAuth2Client.clientName) &&
        Objects.equals(this.clientSecret, oAuth2Client.clientSecret) &&
        Objects.equals(this.clientSecretExpiresAt, oAuth2Client.clientSecretExpiresAt) &&
        Objects.equals(this.clientUri, oAuth2Client.clientUri) &&
        Objects.equals(this.contacts, oAuth2Client.contacts) &&
        Objects.equals(this.grantTypes, oAuth2Client.grantTypes) &&
        Objects.equals(this.id, oAuth2Client.id) &&
        Objects.equals(this.logoUri, oAuth2Client.logoUri) &&
        Objects.equals(this.owner, oAuth2Client.owner) &&
        Objects.equals(this.policyUri, oAuth2Client.policyUri) &&
        Objects.equals(this._public, oAuth2Client._public) &&
        Objects.equals(this.redirectUris, oAuth2Client.redirectUris) &&
        Objects.equals(this.responseTypes, oAuth2Client.responseTypes) &&
        Objects.equals(this.scope, oAuth2Client.scope) &&
        Objects.equals(this.tosUri, oAuth2Client.tosUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientName, clientSecret, clientSecretExpiresAt, clientUri, contacts, grantTypes, id, logoUri, owner, policyUri, _public, redirectUris, responseTypes, scope, tosUri);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OAuth2Client {\n");
    
    sb.append("    clientName: ").append(toIndentedString(clientName)).append("\n");
    sb.append("    clientSecret: ").append(toIndentedString(clientSecret)).append("\n");
    sb.append("    clientSecretExpiresAt: ").append(toIndentedString(clientSecretExpiresAt)).append("\n");
    sb.append("    clientUri: ").append(toIndentedString(clientUri)).append("\n");
    sb.append("    contacts: ").append(toIndentedString(contacts)).append("\n");
    sb.append("    grantTypes: ").append(toIndentedString(grantTypes)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    logoUri: ").append(toIndentedString(logoUri)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    policyUri: ").append(toIndentedString(policyUri)).append("\n");
    sb.append("    _public: ").append(toIndentedString(_public)).append("\n");
    sb.append("    redirectUris: ").append(toIndentedString(redirectUris)).append("\n");
    sb.append("    responseTypes: ").append(toIndentedString(responseTypes)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    tosUri: ").append(toIndentedString(tosUri)).append("\n");
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

