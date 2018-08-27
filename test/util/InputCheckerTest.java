package util;

import org.junit.Assert;
import org.junit.Test;

public class InputCheckerTest {

    @Test
    public void checkIfValidLengthTest() {
        Assert.assertTrue(checkIfValidLengthHelper(1));
        Assert.assertTrue(checkIfValidLengthHelper(4));
        Assert.assertTrue(checkIfValidLengthHelper(9));
        Assert.assertTrue(checkIfValidLengthHelper(16));
        Assert.assertTrue(checkIfValidLengthHelper(25));


        Assert.assertFalse(checkIfValidLengthHelper(2));
        Assert.assertFalse(checkIfValidLengthHelper(3));
        Assert.assertFalse(checkIfValidLengthHelper(6));
        Assert.assertFalse(checkIfValidLengthHelper(12));
        Assert.assertFalse(checkIfValidLengthHelper(20));
        Assert.assertFalse(checkIfValidLengthHelper(24));

        Assert.assertFalse(checkIfValidLengthHelper(0));
        Assert.assertFalse(checkIfValidLengthHelper(-1));
    }

    private boolean checkIfValidLengthHelper(int length) {
        try {
            InputChecker.checkIfValidLength(length);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @Test
    public void checkIfValidEntryTest() {
        Assert.assertFalse(checkIfValidEntryHelper(-1, 4, InputChecker.EntryType.ENTRY));
        Assert.assertTrue(checkIfValidEntryHelper(0, 4, InputChecker.EntryType.ENTRY));
        Assert.assertTrue(checkIfValidEntryHelper(4, 4, InputChecker.EntryType.ENTRY));
        Assert.assertFalse(checkIfValidEntryHelper(5, 4, InputChecker.EntryType.ENTRY));


        Assert.assertFalse(checkIfValidEntryHelper(-1, 9, InputChecker.EntryType.ENTRY));
        Assert.assertTrue(checkIfValidEntryHelper(0, 9, InputChecker.EntryType.ENTRY));
        Assert.assertTrue(checkIfValidEntryHelper(9, 9, InputChecker.EntryType.ENTRY));
        Assert.assertFalse(checkIfValidEntryHelper(10, 9, InputChecker.EntryType.ENTRY));

        Assert.assertFalse(checkIfValidEntryHelper(-1, 16, InputChecker.EntryType.ENTRY));
        Assert.assertTrue(checkIfValidEntryHelper(0, 16, InputChecker.EntryType.ENTRY));
        Assert.assertTrue(checkIfValidEntryHelper(16, 16, InputChecker.EntryType.ENTRY));
        Assert.assertFalse(checkIfValidEntryHelper(17, 16, InputChecker.EntryType.ENTRY));

        Assert.assertFalse(checkIfValidEntryHelper(-1, 4, InputChecker.EntryType.INDEX));
        Assert.assertTrue(checkIfValidEntryHelper(0, 4, InputChecker.EntryType.INDEX));
        Assert.assertTrue(checkIfValidEntryHelper(3, 4, InputChecker.EntryType.INDEX));
        Assert.assertFalse(checkIfValidEntryHelper(4, 4, InputChecker.EntryType.INDEX));

        Assert.assertFalse(checkIfValidEntryHelper(-1, 9, InputChecker.EntryType.INDEX));
        Assert.assertTrue(checkIfValidEntryHelper(0, 9, InputChecker.EntryType.INDEX));
        Assert.assertTrue(checkIfValidEntryHelper(8, 9, InputChecker.EntryType.INDEX));
        Assert.assertFalse(checkIfValidEntryHelper(9, 9, InputChecker.EntryType.INDEX));

        Assert.assertFalse(checkIfValidEntryHelper(-1, 16, InputChecker.EntryType.INDEX));
        Assert.assertTrue(checkIfValidEntryHelper(0, 16, InputChecker.EntryType.INDEX));
        Assert.assertTrue(checkIfValidEntryHelper(15, 16, InputChecker.EntryType.INDEX));
        Assert.assertFalse(checkIfValidEntryHelper(16, 16, InputChecker.EntryType.INDEX));

    }

    private boolean checkIfValidEntryHelper(int input, int length, InputChecker.EntryType type) {
        try {
            InputChecker.checkIfValidEntry(input, length, type);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
