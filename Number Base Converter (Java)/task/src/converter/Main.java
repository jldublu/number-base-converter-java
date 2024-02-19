package converter;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean firstPrompt = true;
        while (firstPrompt) {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            String firstInput = scanner.next();
            if (firstInput.equals("/exit")) {
                firstPrompt = false;
            } else {
                int sourceBase = Integer.parseInt(firstInput);
                int targetBase = scanner.nextInt();
                boolean secondPrompt = true;
                while (secondPrompt) {
                    System.out.printf("Enter number in base %d to convert to base %d (To go back type /back) ", sourceBase, targetBase);
                    String sourceNumber = scanner.next();
                    if (sourceNumber.equals("/back")) {
                        secondPrompt = false;
                    } else {
                        System.out.println("Conversion result: " + new NumberConverter().convert(sourceNumber, sourceBase, targetBase));
                    }
                }
            }
        }
    }
}
