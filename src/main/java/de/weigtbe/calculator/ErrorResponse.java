package de.weigtbe.calculator;

import lombok.Getter;

public class ErrorResponse extends Response{

  @Getter
  private String error;

  public ErrorResponse(String error){

    this.error = error;

  }

}
