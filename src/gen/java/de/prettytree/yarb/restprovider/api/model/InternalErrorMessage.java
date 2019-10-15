package de.prettytree.yarb.restprovider.api.model;

import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public class InternalErrorMessage   {
  
  private @Valid String exceptionId;

  /**
   **/
  public InternalErrorMessage exceptionId(String exceptionId) {
    this.exceptionId = exceptionId;
    return this;
  }

  
  @JsonProperty("exceptionId")
  @NotNull
  public String getExceptionId() {
    return exceptionId;
  }
  public void setExceptionId(String exceptionId) {
    this.exceptionId = exceptionId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InternalErrorMessage internalErrorMessage = (InternalErrorMessage) o;
    return Objects.equals(this.exceptionId, internalErrorMessage.exceptionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(exceptionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InternalErrorMessage {\n");
    
    sb.append("    exceptionId: ").append(toIndentedString(exceptionId)).append("\n");
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

