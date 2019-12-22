package de.prettytree.yarb.restprovider.api.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CreateBoard
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-22T15:48:57.260+01:00[Europe/Berlin]")

public class CreateBoard   {
  @JsonProperty("name")
  private String name;

  @JsonProperty("columnNames")
  @Valid
  private List<String> columnNames = new ArrayList<>();

  public CreateBoard name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=3) 
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CreateBoard columnNames(List<String> columnNames) {
    this.columnNames = columnNames;
    return this;
  }

  public CreateBoard addColumnNamesItem(String columnNamesItem) {
    this.columnNames.add(columnNamesItem);
    return this;
  }

  /**
   * Get columnNames
   * @return columnNames
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=2,max=5) 
  public List<String> getColumnNames() {
    return columnNames;
  }

  public void setColumnNames(List<String> columnNames) {
    this.columnNames = columnNames;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateBoard createBoard = (CreateBoard) o;
    return Objects.equals(this.name, createBoard.name) &&
        Objects.equals(this.columnNames, createBoard.columnNames);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, columnNames);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateBoard {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    columnNames: ").append(toIndentedString(columnNames)).append("\n");
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

