package takeaway.server.gameofthree.dto;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Error
 */
@Validated

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Error  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("code")
  private Integer code = null;


  @JsonProperty("message")
  private String message = null;

  @JsonProperty("status")
  private Integer status = null;


}

