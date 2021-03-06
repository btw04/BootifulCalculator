package de.weigtbe.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class CalculateTest {

  private Calculate calculateClass = new Calculate();
  private Random random = new Random();


  @Test
  public void staticTests(){

    List<BigDecimal> values;
    CalculatorRequest request;
    CalculatorResponse response;


    //simple addition

    values = new ArrayList<>();
    values.add(new BigDecimal("1"));
    values.add(new BigDecimal("2"));
    values.add(new BigDecimal("3"));
    values.add(new BigDecimal("4"));
    values.add(new BigDecimal("5"));
    values.add(new BigDecimal("6"));


    request = new CalculatorRequest(values, "add", "integer");

    response = execute(request);

    assertEquals(new BigDecimal("21"), response.getResult());

    //simple subtraction

    values = new ArrayList<>();
    values.add(new BigDecimal("1"));
    values.add(new BigDecimal("2"));
    values.add(new BigDecimal("3"));
    values.add(new BigDecimal("4"));
    values.add(new BigDecimal("5"));
    values.add(new BigDecimal("6"));


    request = new CalculatorRequest(values, "sub", "integer");

    response = execute(request);

    assertEquals(new BigDecimal("-19"), response.getResult());

    //simple multiplication

    values = new ArrayList<>();
    values.add(new BigDecimal("1"));
    values.add(new BigDecimal("2"));
    values.add(new BigDecimal("-3"));
    values.add(new BigDecimal("4"));
    values.add(new BigDecimal("5"));
    values.add(new BigDecimal("6"));


    request = new CalculatorRequest(values, "mul", "integer");

    response = execute(request);

    assertEquals(new BigDecimal("-720"), response.getResult());

    //simple division

    values = new ArrayList<>();
    values.add(new BigDecimal("1"));
    values.add(new BigDecimal("2"));


    request = new CalculatorRequest(values, "div", "integer");

    response = execute(request);

    assertEquals(new BigDecimal("0.5"), response.getResult());

    //floating-point precision multiplication

    values = new ArrayList<>();
    values.add(new BigDecimal("2.2525"));
    values.add(new BigDecimal("2.2525"));



    request = new CalculatorRequest(values, "mul", "safe");

    response = execute(request);

    assertEquals(new BigDecimal("5.07375625"), response.getResult());

    //floating-point precision subtraction

    values = new ArrayList<>();
    values.add(new BigDecimal("0.1"));
    values.add(new BigDecimal("0.2"));
    values.add(new BigDecimal("0.3"));
    values.add(new BigDecimal("0.4"));
    values.add(new BigDecimal("0.5"));

    request = new CalculatorRequest(values, "sub", "safe");

    response = execute(request);

    assertEquals(new BigDecimal("-1.3"), response.getResult());
  }

  @Test
  public void randomIntegerAddition() {

    List<BigDecimal> randomValues = getRandomValues(100, false);

    CalculatorRequest request = new CalculatorRequest(randomValues, "add", "integer");

    CalculatorResponse response = execute(request);


    BigDecimal correctValue = new BigDecimal(0);

    for (BigDecimal number : randomValues) {

      correctValue = correctValue.add(number);

    }


    assertEquals(correctValue, response.getResult());

  }

  @Test
  public void randomIntegerSubtraction() {

    List<BigDecimal> randomValues = getRandomValues(100, false);

    CalculatorRequest request = new CalculatorRequest(randomValues, "sub", "integer");

    CalculatorResponse response = execute(request);


    BigDecimal correctValue = randomValues.get(0);

    for (int i = 1; i < randomValues.size(); i++) {

      correctValue = correctValue.subtract(randomValues.get(i));

    }


    assertEquals(correctValue, response.getResult());

  }

  @Test
  public void randomIntegerMultiplication() {

    List<BigDecimal> randomValues = getRandomValues(100, false);

    CalculatorRequest request = new CalculatorRequest(randomValues, "mul", "integer");

    CalculatorResponse response = execute(request);


    BigDecimal correctValue = randomValues.get(0);

    for (int i = 1; i < randomValues.size(); i++) {

      correctValue = correctValue.multiply(randomValues.get(i));

    }


    assertEquals(correctValue, response.getResult());

  }

  @Test
  public void randomDecimalAddition() {

    List<BigDecimal> randomValues = getRandomValues(100, true);

    CalculatorRequest request = new CalculatorRequest(randomValues, "add", "decimal");

    CalculatorResponse response = execute(request);


    BigDecimal correctValue = new BigDecimal(0);

    for (BigDecimal number : randomValues) {

      correctValue = correctValue.add(number);

    }


    assertEquals(correctValue, response.getResult());

  }

  @Test
  public void randomDecimalSubtraction() {

    List<BigDecimal> randomValues = getRandomValues(100, true);

    CalculatorRequest request = new CalculatorRequest(randomValues, "sub", "decimal");

    CalculatorResponse response = execute(request);


    BigDecimal correctValue = randomValues.get(0);

    for (int i = 1; i < randomValues.size(); i++) {

      correctValue = correctValue.subtract(randomValues.get(i));

    }


    assertEquals(correctValue, response.getResult());

  }

  @Test
  public void randomDecimalMultiplication() {

    List<BigDecimal> randomValues = getRandomValues(100, true);

    CalculatorRequest request = new CalculatorRequest(randomValues, "mul", "decimal");

    CalculatorResponse response = execute(request);


    BigDecimal correctValue = randomValues.get(0);

    for (int i = 1; i < randomValues.size(); i++) {

      correctValue = correctValue.multiply(randomValues.get(i));

    }


    assertEquals(correctValue, response.getResult());

  }

  @Test
  public void staticIntegrationTest() throws IOException {

    ObjectMapper mapper = new ObjectMapper();

    URL url = new URL("http://localhost:8080/calculate");

    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json; utf-8");
    connection.setRequestProperty("Accept", "application/json");
    connection.setDoOutput(true);

    ArrayList<BigDecimal> values = new ArrayList<>();
    values.add(new BigDecimal("25"));
    values.add(new BigDecimal("37"));



    CalculatorRequest request = new CalculatorRequest(values, "add", "integer");
    String jsonInput = mapper.writeValueAsString(request);


    try(OutputStream outputStream = connection.getOutputStream()) {
      byte[] input = jsonInput.getBytes("utf-8");
      outputStream.write(input, 0, input.length);
    }

    String jsonOutput;

    try(BufferedReader reader = new BufferedReader(
        new InputStreamReader(connection.getInputStream(), "utf-8"))) {
      StringBuilder responseString = new StringBuilder();
      String responseLine;
      while ((responseLine = reader.readLine()) != null) {
        responseString.append(responseLine.trim());
      }

      jsonOutput = responseString.toString();

    }


    CalculatorResponse response = mapper.readValue(jsonOutput, CalculatorResponse.class);

    assertEquals(new BigDecimal(62), response.getResult());



  }

  private List<BigDecimal> getRandomValues(int length, boolean decimal){

    List<BigDecimal> randomValues = new ArrayList<>();

    for (int i = 0; i < length; i++) {

      BigDecimal randomBigDecimal = new BigDecimal(random.nextInt(100));

      if(decimal) {

        randomBigDecimal = randomBigDecimal.add(new BigDecimal(random.nextDouble()));

      }

      randomValues.add(randomBigDecimal);

    }

    return randomValues;

  }


  private CalculatorResponse execute(CalculatorRequest request){

    ResponseEntity responseEntity = calculateClass.calculate(request);

    return (CalculatorResponse) responseEntity.getBody();

  }

}