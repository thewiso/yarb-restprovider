package de.prettytree.yarb.restprovider.api.model;

import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public class UserCredentials   {
  
  private @Valid String username;
  private @Valid String password;

  /**
   **/
  public UserCredentials username(String username) {
    this.username = username;
    return this;
  }

  
  @JsonProperty("username")
  @NotNull
 @Pattern(regexp="^[^\\sA-Z]+$") @Size(min=4,max=20)  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   **/
  public UserCredentials password(String password) {
    this.password = password;
    return this;
  }

  
  @JsonProperty("password")
  @NotNull
 @Size(min=6,max=20)  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserCredentials userCredentials = (UserCredentials) o;
    return Objects.equals(this.username, userCredentials.username) &&
        Objects.equals(this.password, userCredentials.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserCredentials {\n");
    
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
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

