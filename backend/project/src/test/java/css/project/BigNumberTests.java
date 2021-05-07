package css.project;

import css.project.bigNumber.BigNumber;
import css.project.exception.custom.ArithmeticAppException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BigNumberTests {
    @Test
    void Test_StringConstructor_And_LongConstructor_Then_ToLong() {
        BigNumber.updateBASE(2);
        BigNumber.updateBASE(15);
        BigNumber.updateBASE(1000);
        String[] nr1 = {"31600", "123456789", "210", "0", "9999", "00010000", "32769", "100", "10", "50"};
        BigNumber nr;
        for (String s : nr1) {
            nr = new BigNumber(s);
            assertEquals(nr.toLong(), Long.parseLong(s));
            nr = new BigNumber(Long.parseLong(s));
            assertEquals(nr.toLong(), Long.parseLong(s));
        }

        ArithmeticAppException exception = assertThrows(ArithmeticAppException.class,
                () -> new BigNumber("123a"));
        assertTrue(exception.getMessage().contains("Big Number Parse Failure"));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 10, 100, 1000, 1000000000})
    void Test_StringConstructor_And_LongConstructor_Then_ToString(long base) {
        BigNumber.updateBASE(base);
        String[] nr1 = {"32769", "31600", "123456789", "210", "0", "9999", "10000", "32769", "10010010000001", "10", "50"};
        BigNumber nr;
        for (String s : nr1) {
            nr = new BigNumber(s);
            assertEquals(nr.toString().compareTo(s), 0);
            nr = new BigNumber(Long.parseLong(s));
            assertEquals(nr.toString().compareTo(s), 0);
        }
    }

    @Test
    void Test_Increment_Decrement_UpdateBase_IncreaseLength_MultiplyByBase_DivideByBase() {
        BigNumber.updateBASE(10);
        BigNumber nr = new BigNumber(0);
        nr.adjustLength();
        assertEquals(nr.toLong(),
                0);

        nr.increaseLength(15);
        nr.increaseLength(10);
        assertEquals(nr.getLength(), 15);

        nr = new BigNumber(15);
        int[] nrs = {2, 0, -1};
        long[] result =  {15000, 15000, 15000};
        nr.multiplyByBase();
        assertEquals(nr.toLong(), 150);
        for (int i = 0; i < result.length; i++) {
            nr.multiplyByBase(nrs[i]);
            assertEquals(nr.toLong(), result[i]);
        }

        nr = new BigNumber(15);
        nr.increaseLength(10);
        nr.multiplyByBase();
        assertEquals(nr.toLong(), 150);
        for (int i = 0; i < result.length; i++) {
            nr.multiplyByBase(nrs[i]);
            assertEquals(nr.toLong(), result[i]);
        }

        nr = new BigNumber(15000);
        nrs = new int[]{2, 0, -1, 1};
        result = new long[]{15, 15, 15, 1};
        nr.divideByBase();
        assertEquals(nr.toLong(), 1500);
        for (int i = 0; i < result.length; i++) {
            nr.divideByBase(nrs[i]);
            assertEquals(nr.toLong(), result[i]);
        }

        long[] n1 = 	{9, 99, 999, 9999, 123, 100, 2, 0, 1, 122, 2};
        for (long it : n1)  {
            BigNumber x = new BigNumber(it);
            x.increment();
            assertEquals(x.toLong(), it + 1);
        }

        n1 = new long[]{20, 1, 10, 200, 999, 100, 1000, 10000000000L, 9, 99, 999, 9999, 123, 100, 2, 1, 1, 122, 2};
        for (long it : n1)  {
            BigNumber x = new BigNumber(it);
            x.decrement();
            assertEquals(x.toLong(), it - 1);
        }

        ArithmeticAppException exception = assertThrows(ArithmeticAppException.class,
                () -> new BigNumber(0).decrement());
        assertTrue(exception.getMessage().contains("Decrementation of number 0 detected. Error!"));

    }
}
