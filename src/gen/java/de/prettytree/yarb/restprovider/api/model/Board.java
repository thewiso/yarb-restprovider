package de.prettytree.yarb.restprovider.api.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.prettytree.yarb.restprovider.api.model.BoardColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Board
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-17T19:14:37.694+01:00[Europe/Berlin]")

public class Board   {
  @JsonProperty("id")
  private Integer id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("creationDate")
  private OffsetDateTime creationDate;

  @JsonProperty("columns")
  @Valid
  private List<BoardColumn> columns = new ArrayList<>();

  public Board id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Board name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Board creationDate(OffsetDateTime creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  /**
   * Get creationDate
   * @return creationDate
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public OffsetDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(OffsetDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public Board columns(List<BoardColumn> columns) {
    this.columns = columns;
    return this;
  }

  public Board addColumnsItem(BoardColumn columnsItem) {
    this.columns.add(columnsItem);
    return this;
  }

  /**
   * Get columns
   * @return columns
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
@Size(min=2,max=5) 
  public List<BoardColumn> getColumns() {
    return columns;
  }

  public void setColumns(List<BoardColumn> columns) {
    this.columns = columns;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Board board = (Board) o;
    return Objects.equals(this.id, board.id) &&
        Objects.equals(this.name, board.name) &&
        Objects.equals(this.creationDate, board.creationDate) &&
        Objects.equals(this.columns, board.columns);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, creationDate, columns);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Board {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    columns: ").append(toIndentedString(columns)).append("\n");
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

