package de.weigtbe.calculator;

import java.math.BigDecimal;
import lombok.Getter;

public class CalculatorResponse extends Response{

  @Getter
  private BigDecimal result;

  public CalculatorResponse(BigDecimal result){

    this.result = result;

  }

  public CalculatorResponse() {

  }

}
