package org.freedomfinancestack.razorpay.cas.dao.utils;

public class Util {

    public static String incrementString(String input, int counter) {
        // Validate input
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input string is empty or null.");
        }
        // Convert string to integer
        int number = Integer.parseInt(input);

        // Increment the number based on the counter
        int result = number + counter;

        // Validate if the result is negative
        if (result < 0) {
            throw new IllegalArgumentException("Resulting number is negative.");
        }

        // Convert the result back to a formatted string
        String output = String.format("%0" + input.length() + "d", result);

        // Validate if the output length is greater than the input length
        if (output.length() != input.length()) {
            throw new IllegalArgumentException(
                    "Output string length is not the same as input string length.");
        }

        return output;
    }
}
