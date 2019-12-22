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
 * CreateBoardNote
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-22T15:48:57.260+01:00[Europe/Berlin]")

public class CreateBoardNote   {
  @JsonProperty("content")
  private String content;

  @JsonProperty("boardColumnId")
  private Integer boardColumnId;

  public CreateBoardNote content(String content) {
    this.content = content;
    return this;
  }

  /**
   * Get content
   * @return content
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public CreateBoardNote boardColumnId(Integer boardColumnId) {
    this.boardColumnId = boardColumnId;
    return this;
  }

  /**
   * Get boardColumnId
   * @return boardColumnId
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public Integer getBoardColumnId() {
    return boardColumnId;
  }

  public void setBoardColumnId(Integer boardColumnId) {
    this.boardColumnId = boardColumnId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateBoardNote createBoardNote = (CreateBoardNote) o;
    return Objects.equals(this.content, createBoardNote.content) &&
        Objects.equals(this.boardColumnId, createBoardNote.boardColumnId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, boardColumnId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateBoardNote {\n");
    
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    boardColumnId: ").append(toIndentedString(boardColumnId)).append("\n");
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

