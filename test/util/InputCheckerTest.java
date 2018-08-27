package util;

import org.junit.Assert;
import org.junit.Test;

public class InputCheckerTest {

    @Test
    public void isSquareNumberTest() {
        Assert.assertTrue(isSquareNumberHelperMethod(1));
        Assert.assertTrue(isSquareNumberHelperMethod(4));
        Assert.assertTrue(isSquareNumberHelperMethod(9));
        Assert.assertTrue(isSquareNumberHelperMethod(16));
        Assert.assertTrue(isSquareNumberHelperMethod(25));


        Assert.assertFalse(isSquareNumberHelperMethod(2));
        Assert.assertFalse(isSquareNumberHelperMethod(3));
        Assert.assertFalse(isSquareNumberHelperMethod(6));
        Assert.assertFalse(isSquareNumberHelperMethod(12));
        Assert.assertFalse(isSquareNumberHelperMethod(20));
        Assert.assertFalse(isSquareNumberHelperMethod(24));
    }

    private boolean isSquareNumberHelperMethod(int length) {
        try {
            InputChecker.isSquareNumber(length);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @Test
    public void isValidEntryTest() {
        Assert.assertFalse(isValidEntryHelperMethod(-1, 4, InputChecker.EntryType.ENTRY));
        Assert.assertTrue(isValidEntryHelperMethod(0, 4, InputChecker.EntryType.ENTRY));
        Assert.assertTrue(isValidEntryHelperMethod(4, 4, InputChecker.EntryType.ENTRY));
        Assert.assertFalse(isValidEntryHelperMethod(5, 4, InputChecker.EntryType.ENTRY));


        Assert.assertFalse(isValidEntryHelperMethod(-1, 9, InputChecker.EntryType.ENTRY));
        Assert.assertTrue(isValidEntryHelperMethod(0, 9, InputChecker.EntryType.ENTRY));
        Assert.assertTrue(isValidEntryHelperMethod(9, 9, InputChecker.EntryType.ENTRY));
        Assert.assertFalse(isValidEntryHelperMethod(10, 9, InputChecker.EntryType.ENTRY));

        Assert.assertFalse(isValidEntryHelperMethod(-1, 16, InputChecker.EntryType.ENTRY));
        Assert.assertTrue(isValidEntryHelperMethod(0, 16, InputChecker.EntryType.ENTRY));
        Assert.assertTrue(isValidEntryHelperMethod(16, 16, InputChecker.EntryType.ENTRY));
        Assert.assertFalse(isValidEntryHelperMethod(17, 16, InputChecker.EntryType.ENTRY));

        Assert.assertFalse(isValidEntryHelperMethod(-1, 4, InputChecker.EntryType.INDEX));
        Assert.assertTrue(isValidEntryHelperMethod(0, 4, InputChecker.EntryType.INDEX));
        Assert.assertTrue(isValidEntryHelperMethod(3, 4, InputChecker.EntryType.INDEX));
        Assert.assertFalse(isValidEntryHelperMethod(4, 4, InputChecker.EntryType.INDEX));

        Assert.assertFalse(isValidEntryHelperMethod(-1, 9, InputChecker.EntryType.INDEX));
        Assert.assertTrue(isValidEntryHelperMethod(0, 9, InputChecker.EntryType.INDEX));
        Assert.assertTrue(isValidEntryHelperMethod(8, 9, InputChecker.EntryType.INDEX));
        Assert.assertFalse(isValidEntryHelperMethod(9, 9, InputChecker.EntryType.INDEX));

        Assert.assertFalse(isValidEntryHelperMethod(-1, 16, InputChecker.EntryType.INDEX));
        Assert.assertTrue(isValidEntryHelperMethod(0, 16, InputChecker.EntryType.INDEX));
        Assert.assertTrue(isValidEntryHelperMethod(15, 16, InputChecker.EntryType.INDEX));
        Assert.assertFalse(isValidEntryHelperMethod(16, 16, InputChecker.EntryType.INDEX));

    }

    private boolean isValidEntryHelperMethod(int input, int length, InputChecker.EntryType type) {
        try {
            InputChecker.isValidEntry(input, length, type);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
