package de.prettytree.yarb.restprovider.authentication.model;

import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public class Token   {
  
  private @Valid String tokenString;

  /**
   **/
  public Token tokenString(String tokenString) {
    this.tokenString = tokenString;
    return this;
  }

  
  @JsonProperty("tokenString")
  public String getTokenString() {
    return tokenString;
  }
  public void setTokenString(String tokenString) {
    this.tokenString = tokenString;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Token token = (Token) o;
    return Objects.equals(this.tokenString, token.tokenString);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tokenString);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Token {\n");
    
    sb.append("    tokenString: ").append(toIndentedString(tokenString)).append("\n");
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

