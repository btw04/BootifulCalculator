package de.weigtbe.calculator;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;

public class CalculatorRequest {

  @Getter
  private List<BigDecimal> values;

  @Getter
  private String operation;

  @Getter
  private String type;

  public CalculatorRequest(List<BigDecimal> values, String operation, String type) {
    this.values = values;
    this.operation = operation;
    this.type = type;
  }

}
