package css.project;

import css.project.bigNumber.BigNumber;
import css.project.exception.custom.ArithmeticAppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static css.project.bigNumber.BigNumberMathOps.*;
import static org.junit.jupiter.api.Assertions.*;

class BigNumberTests {
	@BeforeEach
	void setup() {
		BigNumber.updateBASE(10);
	}

	@Test
	void TestConstructor() {
		BigNumber.updateBASE(1000);
		String[] nr1 = {"123456789", "0", "9999", "00010000"};
		BigNumber nr;
		for (String s : nr1) {
			nr = new BigNumber(s);
			System.out.println(nr);
			assertEquals(nr.toLong(), Long.parseLong(s));
		}
	}

	@Test
	void TestUtils() {
		BigNumber.updateBASE(10);
		BigNumber nr = new BigNumber(0);
		nr.adjustLength();
		assertEquals(nr.toLong(),
				0);
		System.out.println(nr);

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
	}

	@Test
	void TestCompare() {
		long[] n1 = 	{123, 100, 2, 0, 1, 122, 2};
		long[] n2 = 	{122, 2, 2, 0, 2, 123, 100};
		int[] result =  {1, 1, 0, 0, -1, -1, -1};
		for (int i = 0; i < result.length; i++)
			assertEquals(compare(new BigNumber(n1[i]), new BigNumber(n2[i])),
					result[i]);
	}

	@Test
	void TestAdd() {
		long[] n1 = {0, 1000, 0, 99, 99, 12};
		long[] n2 = {0, 0, 1000, 99, 1, 2};
		for (int i = 0; i < n1.length; i++) {
			assertEquals(add(new BigNumber(n1[i]), new BigNumber(n2[i])).toLong(),
					n1[i] + n2[i]);
			assertEquals(add(new BigNumber(n1[i]), n2[i]).toLong(),
					n1[i] + n2[i]);
		}
	}

	@Test
	void TestMultiply() {
		long[] n1 = {0, 1000, 0, 99, 99, 12};
		long[] n2 = {0, 0, 10000, 99, 1, 2};
		for (int i = 0; i < n1.length; i++)
			assertEquals(multiply(new BigNumber(n1[i]), new BigNumber(n2[i])).toLong(),
					n1[i] * n2[i]);
		for (int i = 0; i < n1.length; i++)
			assertEquals(multiply(new BigNumber(n1[i]), n2[i]).toLong(),
					n1[i] * n2[i]);
	}

	@Test
	void TestSubstract() {
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

	@Test
	void TestDivision() {
		long[] n1 = {0, 100, 30, 15, 100, 101, 10001};
		for (long l : n1)
			assertEquals(divideBy2(new BigNumber(l)).toLong(), l / 2);

		long[] n2 = {10000, 1, 2, 3, 4, 5, 6, 7, 1000, 100};
		for (long xx : n1)
			for (long yy : n2) {
				assertEquals(divideQutient(new BigNumber(xx), new BigNumber(yy)).toLong(), xx / yy);
				assertEquals(divideReminder(new BigNumber(xx), new BigNumber(yy)).toLong(), xx % yy);
			}
	}

	@Test
	void TestSqrt() {
		long[] n1 = {0, 100, 30, 15, 100, 101, 10001};
		for (long x : n1)
			assertEquals(sqrt(new BigNumber(x)).toLong(), (long) (Math.sqrt(x)));
	}

	@Test
	void TestPow() {
		long[] n1 = {0, 1, 100, 30, 15, 100, 101};
		long[] n2 = {1, 1, 2, 5, 7};
		for (long xx : n1)
			for (long yy : n2) {
				assertEquals(pow(new BigNumber(xx), new BigNumber(yy)).toLong(), Math.pow(xx, yy));
				assertEquals(pow(new BigNumber(xx), yy).toLong(), Math.pow(xx, yy));
			}
		assertEquals(pow(new BigNumber(100), 0).toLong(), Math.pow(100, 0));
	}

	@Test
	@Disabled
	void StressTest() {
		BigNumber.updateBASE(1000000000);
		int numberSize = 100000;
		BigNumber n1 = new BigNumber("1".repeat(numberSize));
		long constant = 2;
		BigNumber n2 = new BigNumber(n1);
		n1 = multiply(n1, 3);
		for (int i = 1; i <= 10000; i++) {
			//divideBy2(n1);
			multiply(n1, BigNumber.getBASE() / 2);
			//divide(n1, constant);
			//substract(n1, constant);
			//add(n1, n2);
			//substract(n1, n2);
			//multiply(n1, n2);
			//multiply(n1, 15);
		}
	}

}
