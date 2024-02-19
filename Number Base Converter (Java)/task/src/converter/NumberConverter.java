package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class NumberConverter {

    private final String values = "0123456789abcdefghijklmnopqrstuvwxyz";

    public String convert(String sourceNumber, int sourceBase, int targetBase) {
        sourceNumber = sourceNumber.toLowerCase();
        if (targetBase == 10) {
            return convertToDecimal(sourceNumber, sourceBase);
        } else if (sourceBase == 10) {
            return convertFromDecimal(new BigDecimal(sourceNumber), targetBase);
        }

        String decimalNumber = convertToDecimal(sourceNumber, sourceBase);
        return convertFromDecimal(new BigDecimal(decimalNumber), targetBase);
    }

    private String convertToDecimal(String number, int sourceBase) {
        if (number.contains(".")) {
            String[] splitNumber = number.split("\\.");
            BigDecimal integer = new BigDecimal(convertIntegerToDecimal(splitNumber[0], sourceBase));
            BigDecimal fraction = convertFractionToDecimal(splitNumber[1], sourceBase);

            return integer.add(fraction).toString();
        }

        return convertIntegerToDecimal(number, sourceBase).toString();
    }

    private BigInteger convertIntegerToDecimal(String number, int sourceBase) {
        BigInteger integer = BigInteger.ZERO;
        int integerLength = number.length();
        for (int i = 0; i < integerLength; i++) {
            integer = integer.add(BigInteger.valueOf(values.indexOf(number.charAt(integerLength - i - 1)))
                    .multiply(BigDecimal.valueOf(Math.pow(sourceBase, i)).toBigInteger()));
        }

        return integer;
    }

    private BigDecimal convertFractionToDecimal(String number, int sourceBase) {
        BigDecimal decimalFraction = BigDecimal.ZERO;
        int fractionLength = number.length();
        for (int i = 0; i < fractionLength; i++) {
            decimalFraction = decimalFraction.add(BigDecimal.valueOf(values.indexOf(String.valueOf(number.charAt(i))))
                    .multiply(BigDecimal.valueOf(Math.pow(sourceBase, -(i+1)))));
        }

        return decimalFraction.setScale(5, RoundingMode.HALF_UP);
    }

    private String convertFromDecimal(BigDecimal number, int targetBase) {
        BigInteger intNumber = number.setScale(0, RoundingMode.DOWN).toBigInteger();
        if (number.toString().contains(".")) {
            String integer = convertIntegerFromDecimal(intNumber, targetBase);
            String fraction = convertFractionFromDecimal(number, targetBase);
            return integer.concat(fraction);
        }

        return convertIntegerFromDecimal(intNumber, targetBase);
    }

    private String convertIntegerFromDecimal(BigInteger number, int targetBase) {
        BigInteger base = BigInteger.valueOf(targetBase);
        StringBuilder newNumber = new StringBuilder();
        do {
            int remainder = number.remainder(base).intValue();
            number = number.divide(base);
            newNumber.insert(0, values.charAt(remainder));
        } while (number.compareTo(BigInteger.ZERO) > 0);

        return newNumber.toString();
    }

    private String convertFractionFromDecimal(BigDecimal number, int targetBase) {
        BigDecimal base = BigDecimal.valueOf(targetBase);
        BigDecimal fraction = number.remainder(BigDecimal.ONE);
        StringBuilder newFraction = new StringBuilder(".");

        int counter = 0;
        while (!fraction.equals(BigDecimal.ZERO) && counter < 5) {
            BigDecimal product = fraction.multiply(base);
            fraction = product.remainder(BigDecimal.ONE);
            newFraction.append(values.charAt(product.setScale(0, RoundingMode.DOWN).intValue()));
            counter++;
        }

        return newFraction.toString();
    }
}
