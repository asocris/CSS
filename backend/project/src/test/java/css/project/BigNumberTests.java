package css.project;

import css.project.bigNumber.BigNumber;
import css.project.exception.custom.ArithmeticAppException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static css.project.bigNumber.BigNumberMathOps.*;
import static org.junit.jupiter.api.Assertions.*;

class BigNumberTests {
	@AfterEach
	void setup() {
		BigNumber.updateBASE(10);
	}

	@Test
	void TestConstructor() {
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
	}

	@ParameterizedTest
	@ValueSource(longs = {10, 100, 1000, 1000000000})
	void TestToString(long base) {
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
	void TestUtils() {
		BigNumber.updateBASE(10);
		BigNumber nr = new BigNumber(0);
		nr.adjustLength();
		assertEquals(nr.toLong(),
				0);

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
			//System.out.println(it);
			BigNumber x = new BigNumber(it);
			x.decrement();
			assertEquals(x.toLong(), it - 1);
		}
	}

	@ParameterizedTest
	@ValueSource(longs = {10, 100, 1000, 1000000000})
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
	@ValueSource(longs = {10, 100, 1000, 1000000000})
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
	@ValueSource(longs = {10, 100, 1000, 1000000000})
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
	@ValueSource(longs = {10, 100, 1000, 1000000000})
	void TestSubstract(long base) {
		BigNumber.updateBASE(base);
		long[] n1 = {0, 100, 15, 15, 100, 101, 101, 101};
		long[] n2 = {0, 0, 15, 14, 1, 100, 99, 10};
		for (int i = 0; i < n1.length; i++) {
			assertEquals(substract(new BigNumber(n1[i]), new BigNumber(n2[i])).toLong(),
					n1[i] - n2[i]);
			assertEquals(substract(new BigNumber(n1[i]), n2[i]).toLong(),
					n1[i] - n2[i]);
		}

		ArithmeticAppException exception = assertThrows(ArithmeticAppException.class,
				() -> substract(new BigNumber(101), new BigNumber(10000)));
		String expectedMessage = "Substract result Negative";
		assertTrue(exception.getMessage().contains(expectedMessage));
	}

	@ParameterizedTest
	@ValueSource(longs = {10, 100, 1000, 1000000000})
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
	}

	@ParameterizedTest
	@ValueSource(longs = {10, 100, 1000, 1000000000})
	void TestSqrt(long base) {
		BigNumber.updateBASE(base);
		long[] n1 = {0, 100, 30, 15, 100, 101, 121, 25, 100000000, 10001};
		for (long x : n1)
			assertEquals(sqrt(new BigNumber(x)).toLong(), (long) (Math.sqrt(x)));
	}

	@ParameterizedTest
	@ValueSource(longs = {10, 100, 1000, 1000000000})
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
	}

	@Test
	@Disabled
	void StressTest() {

		BigNumber.updateBASE(1000000000);
		int numberSize = 1000;
		BigNumber n1 = new BigNumber("1".repeat(numberSize));
		long constant = 2;
		BigNumber n2 = new BigNumber(n1);
		n1 = multiply(n1, 300);
		for (int i = 1; i <= 20; i++) {
			//divideBy2(n1);
			//multiply(n1, BigNumber.getBASE() / 2);
			divide(n1, n2);
			//substract(n1, constant);
			//add(n1, n2);
			//substract(n1, n2);
			//multiply(n1, n2);
			//multiply(n1, 15);
		}
	}

	@Test
	@Disabled
	void StressTest2() {

		BigNumber.updateBASE(1000000000);
		int numberSize = 1;
		BigNumber n1 = new BigNumber("1".repeat(numberSize));
		long constant = 50000;
		BigNumber n2 = new BigNumber(constant);
		n1 = multiply(n1, 300);
		for (int i = 1; i <= 5; i++) {
			System.out.println(pow(n1, n2).getLength());
		}
	}

	@Test
	@Disabled
	void StressTest3() {

		BigNumber.updateBASE(1000000000);
		int numberSize = 1;
		BigNumber n1 = new BigNumber("1".repeat(numberSize));
		long constant = 50000;
		n1 = multiply(n1, 300);
		for (int i = 1; i <= 5; i++) {
			System.out.println(pow(n1, constant).getLength());
		}
	}

	@Test
	@Disabled
	void StressTest4()
	{
		BigNumber.updateBASE(1000000000);
		int numberSize = 1000;
		BigNumber n1 = new BigNumber("1".repeat(numberSize));
		long constant = 2;
		BigNumber n2 = new BigNumber(n1);
		n1 = multiply(n1,300);
		for(int i = 1; i<=20; i++)
		{
			divide(n1,n2);
		}
	}

}
