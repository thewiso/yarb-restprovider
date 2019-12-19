package de.prettytree.yarb.restprovider.api.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * InternalErrorMessage
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-17T19:14:37.694+01:00[Europe/Berlin]")

public class InternalErrorMessage   {
  @JsonProperty("exceptionId")
  private String exceptionId;

  public InternalErrorMessage exceptionId(String exceptionId) {
    this.exceptionId = exceptionId;
    return this;
  }

  /**
   * Get exceptionId
   * @return exceptionId
  */
  @ApiModelProperty(required = true, value = "")
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

