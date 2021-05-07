package css.project;

import css.project.bigNumber.BigNumber;
import css.project.exception.custom.ArithmeticAppException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static css.project.bigNumber.BigNumberMathOps.*;
import static org.junit.jupiter.api.Assertions.*;

class BigNumberMathOpsTests {

	@ParameterizedTest
	@ValueSource(longs = {-1, 10, 100, 1000, 1000000000})
	void TestCompare(long base) {
		BigNumber.updateBASE(base);
		long[] n1 = 	{123, 100, 2, 0, 1, 122, 2};
		long[] n2 = 	{122, 2, 2, 0, 2, 123, 100};
		int[] result =  {1, 1, 0, 0, -1, -1, -1};
		for (int i = 0; i < result.length; i++)
			assertEquals(compare(new BigNumber(n1[i]), new BigNumber(n2[i])),
					result[i]);
	}

	@ParameterizedTest
	@ValueSource(longs = {-1, 10, 100, 1000, 1000000000})
	void TestAdd(long base) {
		BigNumber.updateBASE(base);
		long[] n1 = {0, 1000, 0, 99, 99, 12};
		long[] n2 = {0, 0, 1000, 99, 1, 2};
		for (int i = 0; i < n1.length; i++) {
			assertEquals(add(new BigNumber(n1[i]), new BigNumber(n2[i])).toLong(),
					n1[i] + n2[i]);
			assertEquals(add(new BigNumber(n1[i]), n2[i]).toLong(),
					n1[i] + n2[i]);
		}
	}

	@ParameterizedTest
	@ValueSource(longs = {-1, 10, 100, 1000, 1000000000})
	void TestMultiply(long base) {
		BigNumber.updateBASE(base);
		long[] n1 = {0, 1000, 0, 99, 99, 12};
		long[] n2 = {0, 0, 10000, 99, 1, 2};
		for (int i = 0; i < n1.length; i++)
			assertEquals(multiply(new BigNumber(n1[i]), new BigNumber(n2[i])).toLong(),
					n1[i] * n2[i]);
		for (int i = 0; i < n1.length; i++)
			assertEquals(multiply(new BigNumber(n1[i]), n2[i]).toLong(),
					n1[i] * n2[i]);
	}

	@ParameterizedTest
	@ValueSource(longs = {-1, 10, 100, 1000, 1000000000})
	void TestSubtract(long base) {
		BigNumber.updateBASE(base);
		long[] n1 = {0, 100, 15, 15, 100, 101, 101, 101};
		long[] n2 = {0, 0, 15, 14, 1, 100, 99, 10};
		for (int i = 0; i < n1.length; i++) {
			assertEquals(subtract(new BigNumber(n1[i]), new BigNumber(n2[i])).toLong(),
					n1[i] - n2[i]);
			assertEquals(subtract(new BigNumber(n1[i]), n2[i]).toLong(),
					n1[i] - n2[i]);
		}

		ArithmeticAppException exception = assertThrows(ArithmeticAppException.class,
				() -> subtract(new BigNumber(101), new BigNumber(10000)));
		assertTrue(exception.getMessage().contains("Substract result Negative"));
	}

	@ParameterizedTest
	@ValueSource(longs = {-1, 10, 100, 1000, 1000000000})
	void TestDivision(long base) {
		BigNumber.updateBASE(base);
		long[] n1 = {100, 0, 100, 30, 15, 100, 101, 10001};
		for (long l : n1)
			assertEquals(divideBy2(new BigNumber(l)).toLong(), l / 2);

		long[] n2 = {4, 10000, 1, 2, 3, 4, 5, 6, 7, 1000, 100};
		for (long xx : n1)
			for (long yy : n2) {
				assertEquals(divideQutient(new BigNumber(xx), new BigNumber(yy)).toLong(), xx / yy);
				assertEquals(divideReminder(new BigNumber(xx), new BigNumber(yy)).toLong(), xx % yy);
				assertEquals(divideQutient(new BigNumber(xx), yy).toLong(), xx / yy);
				assertEquals(divideReminder(new BigNumber(xx), yy).toLong(), xx % yy);
			}

		ArithmeticAppException exception = assertThrows(ArithmeticAppException.class,
				() -> divideQutient(new BigNumber(10), new BigNumber(0)));
		assertTrue(exception.getMessage().contains("Division by 0"));

		exception = assertThrows(ArithmeticAppException.class,
				() -> divideQutient(new BigNumber(0), new BigNumber(0)));
		assertTrue(exception.getMessage().contains("Division by 0"));
	}

	@ParameterizedTest
	@ValueSource(longs = {-1, 10, 100, 1000, 1000000000})
	void TestSqrt(long base) {
		BigNumber.updateBASE(base);
		long[] n1 = {0, 100, 30, 15, 100, 101, 121, 25, 100000000, 10001};
		for (long x : n1)
			assertEquals(sqrt(new BigNumber(x)).toLong(), (long) (Math.sqrt(x)));
	}

	@ParameterizedTest
	@ValueSource(longs = {-1, 10, 100, 1000, 1000000000})
	void TestPow(long base) {
		BigNumber.updateBASE(base);
		long[] n1 = {0, 1, 100, 30, 15, 100, 101};
		long[] n2 = {1, 1, 2, 5, 7};
		for (long xx : n1)
			for (long yy : n2) {
				assertEquals(pow(new BigNumber(xx), new BigNumber(yy)).toLong(), Math.pow(xx, yy));
				assertEquals(pow(new BigNumber(xx), yy).toLong(), Math.pow(xx, yy));
			}
		assertEquals(pow(new BigNumber(100), 0).toLong(), Math.pow(100, 0));
		assertEquals(pow(new BigNumber(100), new BigNumber(0)).toLong(), Math.pow(100, 0));

		ArithmeticAppException exception = assertThrows(ArithmeticAppException.class,
				() -> pow(new BigNumber(0), new BigNumber(0)));
		assertTrue(exception.getMessage().contains("0 ^ 0 is not defined"));
		exception = assertThrows(ArithmeticAppException.class,
				() -> pow(new BigNumber(0), 0));
		assertTrue(exception.getMessage().contains("0 ^ 0 is not defined"));
	}
}
