package com.softserve.rms.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RangeIntegerPatternGenerator {

    /**
     * Method generates regex from a integer range.
     *
     * @param range integer range
     * @return String regex pattern
     * @author Andrii Bren
     */
    public String generateRangeIntegerRegex(String range) {
        String[] newRange = range.split("[ ]");
        String min = newRange[0];
        String max = newRange[1];
        StringBuilder regex = new StringBuilder("(");
        List<String> regexParts = getRegex(min, max);
        for (String part : regexParts) {
            regex.append(part).append("|");
        }
        regex = new StringBuilder(removeLastChar(regex.toString()) + ")");
        return regex.toString();
    }

    /**
     * Method removes redundant last character '|' from regex.
     *
     * @param regex formed regex
     * @return String proper regex pattern
     * @author Andrii Bren
     */
    private static String removeLastChar(String regex) {
        return regex.substring(0, regex.length() - 1);
    }

    /**
     * Method returns a list of regular expressions that match the numbers
     * that fall within the range of the given numbers, inclusive.
     *
     * @param beginStr min value of range
     * @param endStr max value of range
     * @return list of regular expressions
     * @author Andrii Bren
     */
    private List<String> getRegex(String beginStr, String endStr) {
        int start = Integer.parseInt(beginStr);
        int end = Integer.parseInt(endStr);
        List<Integer> pairs = getRegexPairs(start, end);
        return toRegex(pairs);
    }

    /**
     * Method returns the list of integers that are the paired integers
     * used to generate the regular expressions for the given
     * range. Each pair of integers in the list -- 0,1, then 2,3,
     * etc., represents a range for which a single regular expression
     * is generated.
     *
     * @param start min value of range
     * @param end max value of range
     * @return list of integers
     * @author Andrii Bren
     */
    private static List<Integer> getRegexPairs(int start, int end) {
        List<Integer> pairs = new ArrayList<>();
        if (start > end) return pairs; // empty range
        int firstEndingWith0 = start == 0 ? 10 * ((start + 10) / 10) : 10 * ((start + 9) / 10); // first number ending with 0
        if (firstEndingWith0 > end) // not in range?
        {
            // start and end differ only at last digit
            pairs.add(start);
            pairs.add(end);
            return pairs;
        }

        if (start < firstEndingWith0) // start is not ending in 0
        {
            pairs.add(start);
            pairs.add(firstEndingWith0 - 1);
        }

        int lastEndingWith9 = 10 * (end / 10) - 1; // last number in range ending with 9
        // all regex for the range [firstEndingWith0,lastEndingWith9] end with [0-9]
        List<Integer> pairsMiddle = getRegexPairs(firstEndingWith0 / 10, lastEndingWith9 / 10);
        for (int i = 0; i < pairsMiddle.size(); i += 2) {
            // blow up each pair by adding all possibilities for appended digit
            pairs.add(pairsMiddle.get(i) * 10);
            pairs.add(pairsMiddle.get(i + 1) * 10 + 9);
        }
        if (lastEndingWith9 < end) // end is not ending in 9
        {
            pairs.add(lastEndingWith9 + 1);
            pairs.add(end);
        }
        return pairs;
    }

    /**
     * Method returns the regular expressions that match the ranges in the given
     * list of integers. The list is in the form firstRangeStart, firstRangeEnd,
     * secondRangeStart, secondRangeEnd, etc.
     *
     * @param pairs list of integers
     * @return list of regular expressions
     * @author Andrii Bren
     */
    private List<String> toRegex(List<Integer> pairs) {
        List<String> list = new ArrayList<>();
        for (Iterator<Integer> iterator = pairs.iterator(); iterator.hasNext(); ) {
            String start = String.format("%d", iterator.next());
            String end = String.format("%d", iterator.next());
            list.add(toRegex(start, end));
        }
        return list;
    }

    /**
     * Method returns a regular expression string that matches the range
     * with the given start and end strings.
     *
     * @param start min value of range
     * @param end max value of range
     * @return String a regular expression
     * @author Andrii Bren
     */
    private String toRegex(String start, String end) {
        StringBuilder result = new StringBuilder();
        for (int pos = 0; pos < start.length(); pos++) {
            if (start.charAt(pos) == end.charAt(pos)) {
                result.append(start.charAt(pos));
            } else {
                result.append('[').append(start.charAt(pos)).append('-')
                        .append(end.charAt(pos)).append(']');
            }
        }
        return result.toString();
    }
}
