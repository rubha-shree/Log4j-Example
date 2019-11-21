package com.company;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    static Logger logger = Logger.getLogger(com.company.Main.class.getName() + ".mainLogger");
    static Logger fileLogger = Logger.getLogger(com.company.Main.class.getName() + ".fileLogger");
    static int countOfInputs = 1;
    static String previous = "";
    static String history = "";
    static String input = "";
    static float previousResult = 0;
    static String operator = "";
    static float result = 0;

    public static float convertInputStringToNumber(String input) throws NumberFormatException, NullPointerException {
        return Float.parseFloat(input);
    }

    public static boolean checkIfInputIsAnOperator (String input) {
        if (input.equalsIgnoreCase("+")
                || input.equalsIgnoreCase("-")
                || input.equalsIgnoreCase("*")
                || input.equalsIgnoreCase("/")
                || input.equalsIgnoreCase("%")) {
            return true;
        }
        return false;
    }

    public static void reset() {
        countOfInputs = 1;
        previous = "";
        history = "";
        input = "";
        result = 0;
    }

    public static void concatenateToHistory(String input) {
        history = history + " " + input;
    }

    public static void calculate(float num) throws ArithmeticException {
        if (previous != "") {
            switch(operator)
            {
                case "+":
                    result = previousResult + num;
                    break;

                case "-":
                    result = previousResult - num;
                    break;

                case "*":
                    result = previousResult * num;
                    break;

                case "/":
                    if (num == 0.0)
                        throw new ArithmeticException("Divide By Zero Exception");
                    result = previousResult / num;
                    break;

                case "%":
                    result = previousResult % num;
                    break;

                /* If user enters any other operator or char apart from
                 * +, -, * and /, then display an error message to user
                 *
                 */
                default:
                    System.out.println("You have entered wrong operator");
                    reset();
            }
        } else {
            previousResult = num;
        }

    }

    public static void main(String[] args) {
        // write your code here
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("calc>");
            input = reader.readLine();
            fileLogger.debug("Received input: " + input);
            while (!input.equalsIgnoreCase("E")) {
                if (!input.equalsIgnoreCase("C")) {
                    if (countOfInputs % 2 == 1) {
                        try {
                            float operand = convertInputStringToNumber(input);
                            concatenateToHistory(input);
                            calculate(operand);
                            logger.info("PREV-VALUE:" + previousResult + " INPUT:" + input + " HISTORY:" + history + " RESULT:" + result);
                            fileLogger.info("PREV-VALUE:" + previousResult + " INPUT:" + input + " HISTORY:" + history + " RESULT:" + result);
                            if (!previous.equalsIgnoreCase("")) previousResult = result;
                            countOfInputs++;
                        } catch (NumberFormatException | NullPointerException e) {
                            logger.error("NAN");
                            fileLogger.error("NAN");
                            logger.info("PREV-VALUE:" + previousResult + " INPUT:" + input + " HISTORY:" + history + " RESULT:NAN");
                            fileLogger.info("PREV-VALUE:" + previousResult + " INPUT:" + input + " HISTORY:" + history + " RESULT:NAN");
                            reset();
                        } catch (ArithmeticException e) {
                            logger.error("DBZ");
                            fileLogger.error("DBZ");
                            logger.info("PREV-VALUE:" + previousResult + " INPUT:" + input + " HISTORY:" + history + " RESULT:DBZ");
                            fileLogger.info("PREV-VALUE:" + previousResult + " INPUT:" + input + " HISTORY:" + history + " RESULT:DBZ");
                            reset();
                            logger.warn("ERASED HISTORY");
                            fileLogger.warn("ERASED HISTORY");
                        }
                    } else {
                        if (checkIfInputIsAnOperator(input)) {
                            operator = input;
                            concatenateToHistory(input);
                            logger.info("PREV-VALUE:" + previousResult + " INPUT:" + input + " HISTORY:" + history + " RESULT:" + result);
                            fileLogger.info("PREV-VALUE:" + previousResult + " INPUT:" + input + " HISTORY:" + history + " RESULT:" + result);
                            previous = input;
                            countOfInputs++;
                        }
                        else {
                            logger.error("NOPR");
                            fileLogger.error("NOPR");
                        }

                    }
                } else {
                    reset();
                    logger.info("CLEAR");
                    fileLogger.info("CLEAR");
                }
                System.out.print("calc>");
                input = reader.readLine();
                fileLogger.debug("Received input: " + input);
            }
            logger.info("EXIT");
            fileLogger.info("EXIT");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
