package css.project.bigNumber;

import css.project.exception.custom.ArithmeticAppException;
import lombok.Getter;

@Getter
public class BigNumber {

    @Getter
    private static long BASE = 10;

    private static int nrDigitsPerPosition = 1;
    //IMPORTANT NOTE : IF BASE IS CHANGED, ALL NUMBERS ARE INVALIDATED
    //IMPORTANT NOTE 2 : FOR NOW IT ONLY WORKS WITH POWERS OF 10
    // Max value : 1 << 32 - 1 (ideally max should be 1e9, or even 1e8 for safe operations)
    // no, i can't guarantee that with (1 << 32) - 1 it won't go over (1 << 64) - 1 at any point on the program
    // or rather, it should not happen, but better not risk it
    // be serious, no 1 will even touch this constant beside the base value of 10

    public static void updateBASE(long newBASE) {
        if (newBASE < 10) {
            BASE = 10;
            System.out.println("WARNING!!! base must be a multiple of 10. Using BASE value of 10");
            nrDigitsPerPosition = 1;
            return;
        }
        BASE = newBASE;
        nrDigitsPerPosition = 0;
        for (long i = BASE; i > 0; i /= 10) {
            if (i % 10 != 0 && i != 1) {
                BASE = 10;
                System.out.println("WARNING!!! base must be a multiple of 10. Using BASE value of 10");
                nrDigitsPerPosition = 1;
                return;
            }
            nrDigitsPerPosition++;
        }
        nrDigitsPerPosition--;
    }

    private long[] number;
    private int length;

    public BigNumber(long x) {
        if (x == 0) {
            length = 1;
            number = new long[length];
            return;
        }
        length = 0;
        for (long i = x; i != 0; i /= BASE)
            length++;
        number = new long[length];
        for (int i = 0; x != 0; x /= BASE, i++)
            number[i] = x % BASE;
    }

    public BigNumber(BigNumber x) {
        length = x.length;
        number = x.number.clone();
    }

    public BigNumber(String s) {
        if (s.length() <= nrDigitsPerPosition) {
            length = 1;
            number = new long[length];
            number[0] = Long.parseLong(s);
            return;
        }
        for (int i = 0; i < s.length(); i++)
            if (!('0' <= s.charAt(i) && s.charAt(i) <= '9'))
                throw new ArithmeticAppException("Big Number Parse Failure");
        length = s.length() / nrDigitsPerPosition + 1;
        number = new long[length];
        int currentPosition = 0;
        long temp = 0, power10 = 1;
        for (int i = s.length() - 1; i >= 0; i--) {
            temp += (s.charAt(i) - '0') * power10;
            power10 *= 10;
            if (power10 > BASE) {
                number[currentPosition] = temp % BASE;
                currentPosition++;
                power10 = 10;
                temp /= BASE;
            }
        }
        while (temp > 0) {
            number[currentPosition] = temp % BASE;
            currentPosition++;
            temp /= BASE;
        }
        adjustLength();
    }

    public BigNumber() {
        this(0);
    }

    public void increaseLength(int newLength) {
        if (newLength <= length)
            return;
        long[] tmp = number.clone();
        number = new long[newLength];
        System.arraycopy(tmp, 0, number, 0, length);
        length = newLength;
    }

    public void adjustLength() {
        if (number[length - 1] != 0)
            return;
        long[] tmp = number.clone();
        while (length != 0 && tmp[length - 1] == 0)
            length--;
        if (length == 0) {
            number = new long[1];
            length = 1;
            return;
        }
        number = new long[length];
        System.arraycopy(tmp, 0, number, 0, length);
    }

    public void increment() {
        int position = 0;
        while (position < length) {
            if (number[position] != BASE - 1)
                break;
            position++;
        }
        if (position != length)
            number[position]++;
        else {
            //disclamer : the numbers are reversed (like are in the real)
            // meaning they are read (as "digits" for big number) from right to left
            // 123 456 is the real number 456123
            //in this case the number is like 9999 9999 9999 .... 9999
            //so + 1 would be 0000 0000 .....0000 1
            length++;
            number = new long[length];
            number[length - 1] = 1;
        }
    }

    public void decrement() {
        int position = 0;
        while (position < length) {
            if (number[position] != 0)
                break;
            position++;
        }
        if (position < length - 1) {
            number[position]--;
            position--;
            while (position >= 0) {
                number[position] = BASE - 1;
                position--;
            }
        }
        else {
            //disclamer : the numbers are reversed (like are in the real)
            // meaning they are read (as "digits" for big number) from right to left
            // 123 456 is the real number 456123
            //in this case the number is like 0000 0000 .....0000 1
            //so - 1 would be 9999 9999 9999 .... 9999

            if (number[length - 1] == 0)
                throw new ArithmeticAppException("Decrementation of number 0 detected. Error!");
            if (number[length - 1] == 1) {
                length--;
                if (length == 0) {
                    length = 1;
                    number[0] = 0;
                }
                else {
                    number = new long[length];
                    for (int i = 0; i < length; i++)
                        number[i] = BASE - 1;
                }
            }
            else {
                for (int i = 0; i < length - 1; i++)
                    number[i] = BASE - 1;
                number[length - 1]--;
            }
        }
    }

    public void multiplyByBase(int nr) {
        if (nr <= 0)
            return;
        boolean clear = true;
        for (int i = length - 1; i >= length - nr; i--)
            if (number[i] != 0) {
                clear = false;
                break;
            }
        if (clear) {
            //why? because  System.arraycopy creates a temp array in memory (so...it's bad)
            //this method is O(1) memory (nothing is created)
            //noinspection ManualArrayCopy
            for (int i = length - 1; i >= nr; i--)
                number[i] = number[i - nr];
            for (int i = nr - 1; i >= 0; i--)
                number[i] = 0;
        }
        else {
            long[] tmp = number.clone();
            number = new long[length + nr];
            System.arraycopy(tmp, 0, number, nr, length);
            for (int i = nr - 1; i >= 0; i--)
                number[i] = 0;
            length += nr;
        }
    }

    public void multiplyByBase() {
        multiplyByBase(1);
    }

    public void divideByBase(int nr) {
        if (nr <= 0)
            return;
        if (nr >= length) {
            length = 1;
            number = new long[length];
            return;
        }
        long[] tmp = number.clone();
        number = new long[length - nr];
        length -= nr;
        System.arraycopy(tmp, nr, number, 0, length);
    }

    public void divideByBase() {
        divideByBase(1);
    }

    public long toLong() {
        long result = 0;
        for (int i = length - 1; i >= 0; i--)
            result = result * BASE + number[i];
        return result;
    }

    @Override
    public String toString() {
        boolean is0 = true;
        for (int i = 0; i < length; i++)
            if (number[i] != 0) {
                is0 = false;
                break;
            }
        if (is0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        if (number[length - 1] != 0)
            sb.append(number[length - 1]);
        for (int i = length - 2; i >= 0; i--)
            if (number[i] != 0) {
                sb.append("0".repeat(nrDigitsPerPosition - ((int) (Math.log10(number[i]) + 1)))).append(number[i]);
            }
            else
                sb.append("0".repeat(nrDigitsPerPosition));

        return sb.toString();
    }
}
