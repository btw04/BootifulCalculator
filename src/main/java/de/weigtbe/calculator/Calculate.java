package de.weigtbe.calculator;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Calculate {


  @PostMapping(value = "/calculate")
  ResponseEntity<Response> calculate(@RequestBody CalculatorRequest request) {

    List<BigDecimal> values = request.getValues();

    BigDecimal result;


    //Checking every type

    switch(request.getType()){

      case "integer":

        //Going through all BigDecimals to check if they are valid integers

        for (BigDecimal decimalNumber : values) {


          //If the stripped decimals are larger than zero then the BigDecimal is no valid Integer

          if(decimalNumber.stripTrailingZeros().scale() > 0){

            return new ResponseEntity<>(new ErrorResponse("Expected integer-values"), HttpStatus.BAD_REQUEST);

          }

        }

        break;

      case "decimal":

      case "safe":
        break;

      default:

        return new ResponseEntity<>(new ErrorResponse("Invalid type"), HttpStatus.BAD_REQUEST);

    }

    //Checking the operator

    switch (request.getOperation()){

      case "add":
        result = addition(values);
        break;
      case "sub":
        result = subtract(values);
        break;
      case "mul":
        result = multiply(values);
        break;
      case "div":
        result = divide(values);
        break;

      default:
        return new ResponseEntity<>(new ErrorResponse("Invalid operator"), HttpStatus.BAD_REQUEST);
    }

    CalculatorResponse response = new CalculatorResponse(result);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private BigDecimal addition(List<BigDecimal> values){

    BigDecimal result = values.get(0);

    for (int i = 1; i < values.size(); i++) {

      result = result.add(values.get(i));

    }


    return result;

  }

  private BigDecimal subtract(List<BigDecimal> values){

    BigDecimal result = values.get(0);

    for (int i = 1; i < values.size(); i++) {

      result = result.subtract(values.get(i));

    }

    return result;

  }

  private BigDecimal multiply(List<BigDecimal> values){

    BigDecimal result = values.get(0);

    for (int i = 1; i < values.size(); i++) {

      result = result.multiply(values.get(i));

    }

    return result;

  }

  private BigDecimal divide(List<BigDecimal> values){

    BigDecimal result = values.get(0);

    for (int i = 1; i < values.size(); i++) {

      result = result.divide(values.get(i));

    }

    return result;

  }
}