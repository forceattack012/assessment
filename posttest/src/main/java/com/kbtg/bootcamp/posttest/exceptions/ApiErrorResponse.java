package com.kbtg.bootcamp.posttest.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiErrorResponse {
  private LocalDateTime timestamp;
  private int status;
  private String error;

  @JsonProperty("error_message")
  private String errorMessage;

  private String path;

  public ApiErrorResponse(
      LocalDateTime timestamp, int status, String error, String errorMessage, String path) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.errorMessage = errorMessage;
    this.path = path;
  }
}
