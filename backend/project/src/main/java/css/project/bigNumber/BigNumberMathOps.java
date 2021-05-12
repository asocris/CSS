package css.project.bigNumber;

import css.project.exception.custom.ArithmeticAppException;
import org.apache.commons.lang3.tuple.MutablePair;

import static java.lang.Integer.max;

public class BigNumberMathOps {

    public static int compare(BigNumber a, BigNumber b) {
        BigNumber tempa, tempb;
        if (a.getNumber()[a.getLength() - 1] != 0)
            tempa = a;
        else {
            tempa = new BigNumber(a);
            tempa.adjustLength();
        }
        if (b.getNumber()[b.getLength() - 1] != 0)
            tempb = b;
        else {
            tempb = new BigNumber(b);
            tempb.adjustLength();
        }
        if (tempa.getLength() != tempb.getLength()) {
            if (tempa.getLength() > tempb.getLength())
                return 1;
            return -1;
        }
        for (int i = tempa.getLength() - 1; i >= 0; i--)
            if (tempa.getNumber()[i] > tempb.getNumber()[i])
                return 1;
            else if  (tempa.getNumber()[i] < tempb.getNumber()[i])
                return -1;
        return 0;
    }

    public static int compare(BigNumber a, long b) {
        return compare(a, new BigNumber(b));
    }

    public static boolean isImpar(BigNumber a) {
        //assuming that the base is pow(10, something)
        return ((a.getNumber()[0] % 2) == 1);
    }

    public static boolean isImpar(long a) {
        //don't ask why this is here...I just need it
        return ((a % 2) == 1);
    }

    public static BigNumber add(BigNumber a, long b) {
        BigNumber result = new BigNumber();
        int newLength = a.getLength() + 20; //20 = max size of a scalar (1 << 64 ~ 1e19, but 20 for safety)
        result.increaseLength(newLength);
        long transport = b;
        for (int i = 0; i < a.getLength() || transport > 0; i++) {
            if (i < a.getLength())
                transport += a.getNumber()[i];
            result.getNumber()[i] = transport % BigNumber.getBASE();
            transport /= BigNumber.getBASE();
        }
        result.adjustLength();
        assert compare(result, a) >= 0 && compare(result, b) >= 0;
        return result;
    }

    public static BigNumber add(BigNumber a, BigNumber b) {
        BigNumber result = new BigNumber();
        int newLength = max(a.getLength(), b.getLength()) + 1;
        result.increaseLength(newLength);
        long transport = 0;
        for (int i = 0; i < max(a.getLength(), b.getLength()) || transport != 0; i++) {
            if (i < a.getLength())
                transport += a.getNumber()[i];
            if (i < b.getLength())
                transport += b.getNumber()[i];
            result.getNumber()[i] = transport % BigNumber.getBASE();
            transport /= BigNumber.getBASE();
        }
        result.adjustLength();
        assert compare(result, a) >= 0 && compare(result, b) >= 0;
        return result;
    }

    public static BigNumber subtract(BigNumber a, long b) {
        return subtract(a, new BigNumber(b));
    }

    public static BigNumber subtract(BigNumber a, BigNumber b) {
        if (compare(a, b) == -1)
            throw new ArithmeticAppException("Substract result Negative");
        assert compare(a, b) >= 0;
        BigNumber result = new BigNumber(a);
        long transport = 0;
        for (int i = 0; i < a.getLength(); i++) {
            if (i < b.getLength()) {
                if (result.getNumber()[i] >= b.getNumber()[i] + transport) {
                    result.getNumber()[i] -= b.getNumber()[i] + transport;
                    transport = 0;
                }
                else  {
                    result.getNumber()[i] += BigNumber.getBASE();
                    result.getNumber()[i] -= b.getNumber()[i] + transport;
                    transport = 1;
                }
            }
            else {
                if (result.getNumber()[i] >= transport) {
                    result.getNumber()[i] -= transport;
                    break;
                }
                else  {
                    result.getNumber()[i] += BigNumber.getBASE();
                    result.getNumber()[i] -= transport;
                    transport = 1;
                }
            }
        }
        result.adjustLength();
        assert compare(a, add(result, b)) == 0;
        return result;
    }

    public static BigNumber multiply(BigNumber a, long b) {
        BigNumber result = new BigNumber();
        int newLength = a.getLength() * 20 + 1; //20 = max size of a scalar (1 << 64 ~ 1e19, but 20 for safety)
        result.increaseLength(newLength);
        long transport = 0;
        for (int i = 0; i < a.getLength() || transport != 0; i++) {
            if (i < a.getLength())
                transport += a.getNumber()[i] * b;
            result.getNumber()[i] = transport % BigNumber.getBASE();
            transport /= BigNumber.getBASE();
        }
        result.adjustLength();
        assert compare(result, a) >= 0 || compare(result, b) >= 0;
        return result;
    }

    public static BigNumber multiply(BigNumber a, BigNumber b) {
        BigNumber result = new BigNumber();
        int newLength = a.getLength() * b.getLength() + 1;
        result.increaseLength(newLength);
        long transport = 0;
        for (int i = 0; i < a.getLength(); i++)
            for (int i1 = 0; i1 < b.getLength() || transport != 0; i1++) {
                if (i1 < b.getLength())
                    transport += result.getNumber()[i + i1] + a.getNumber()[i] * b.getNumber()[i1];
                else
                    transport += result.getNumber()[i + i1];
                result.getNumber()[i + i1] = transport % BigNumber.getBASE();
                transport /= BigNumber.getBASE();
            }
        result.adjustLength();
        assert compare(result, a) >= 0 || compare(result, b) >= 0;
        return result;
    }

    public static BigNumber divideBy2(BigNumber a) {
        //assuming that the base is multiple of 2
        BigNumber result = multiply(a, BigNumber.getBASE() / 2);
        result.divideByBase();
        result.adjustLength();
        assert compare(subtract(a, multiply(result, 2)), 1) <= 0;
        return result;
    }

    public static MutablePair<BigNumber, BigNumber> divide(BigNumber a, BigNumber b) {
        if (compare(b, 0) == 0)
            throw new ArithmeticAppException("Division by 0");
        if (compare(a, b) == 0)
            return new MutablePair<>(new BigNumber(1), new BigNumber());
        if (compare(a, b) < 0)
            return new MutablePair<>(new BigNumber(0), a);
        assert compare(b, 0) > 0;
        BigNumber l = new BigNumber(1);
        BigNumber r = new BigNumber(a);
        while(compare(l, r) < 0) {
            BigNumber mid = divideBy2(add(l, r));
            if (compare(multiply(mid, b), a) == 0)
                return new MutablePair<>(mid, new BigNumber(0));
            if (compare(multiply(mid, b), a) > 0) {
                mid.decrement();
                r = new BigNumber(mid);
            }
            else {
                mid.increment();
                l = new BigNumber(mid);
            }
        }
        while (compare(multiply(l, b), a) > 0)
            l.decrement();
        BigNumber quotient = new BigNumber(l);
        BigNumber reminder = new BigNumber(subtract(a, multiply(l, b)));
        quotient.adjustLength();
        reminder.adjustLength();
        assert compare(add(reminder, multiply(quotient, b)), a) == 0;

        return new MutablePair<>(quotient, reminder);
    }

    public static MutablePair<BigNumber, BigNumber> divide(BigNumber a, long b) {
        //TODO: if needed, implement a better version
        return divide(a, new BigNumber(b));
    }

    public static BigNumber divideQutient(BigNumber a, BigNumber b) {
        return divide(a, b).getLeft();
    }

    public static BigNumber divideReminder(BigNumber a, BigNumber b) {
        return divide(a, b).getRight();
    }

    public static BigNumber divideQutient(BigNumber a, long b) {
        return divide(a, b).getLeft();
    }

    public static BigNumber divideReminder(BigNumber a, long b) {
        return divide(a, b).getRight();
    }

    public static BigNumber sqrt(BigNumber a) {
        if (compare(a, 0) == 0)
            return a;
        BigNumber l = new BigNumber(1);
        BigNumber r = new BigNumber(a);
        BigNumber tmp;
        while(compare(l, r) < 0) {
            BigNumber mid = divideBy2(add(l, r));
            tmp = multiply(mid, mid);
            if (compare(tmp, a) == 0)
                return mid;
            if (compare(tmp, a) > 0) {
                mid.decrement();
                r = new BigNumber(mid);
            }
            else {
                mid.increment();
                l = new BigNumber(mid);
            }
        }
        while (compare(multiply(l, l), a) > 0)
            l = subtract(l, 1);
        l.adjustLength();
        assert compare(a, multiply(l, l)) == 0 || (
                    compare(a, multiply(l, l)) > 0 &&
                    compare(a, multiply(add(l, 1), add(l, 1))) < 0
                );
        return l;
    }

    public static BigNumber pow(BigNumber a, BigNumber b) {
        if (compare(b, 0) == 0)
            if (compare(a, 0) == 0)
                throw new ArithmeticAppException("0 ^ 0 is not defined");
            else
                return new BigNumber(1);
        assert compare(a, 0) != 0 || compare(b, 0) != 0;
        if (compare(a, 2) < 0)
            return new BigNumber(a);
        // a ^ b = a if ({a = 0 (and b != 0))} or {a = 1})

        if (compare(b, 1) == 0)
            return new BigNumber(a);
        // a ^ 1 = a

        BigNumber tmp = new BigNumber(a);
        BigNumber result = new BigNumber(1);
        BigNumber bTemp = new BigNumber(b);
        while(compare(bTemp, 0) > 0) {
            if (isImpar(bTemp)) {
                result = multiply(result, tmp);
            }
            bTemp = divideBy2(bTemp);
            tmp = multiply(tmp, tmp);
        }
       return result;
    }

    public static BigNumber pow(BigNumber a, long b) {
        if (b == 0)
            if (compare(a, 0) == 0)
                throw new ArithmeticAppException("0 ^ 0 is not defined");
            else
                return new BigNumber(1);
        // 0 ^ 0 = anything ^ 0 = 1
        assert compare(a, 0) != 0 || b != 0;

        if (compare(a, 2) < 0)
            return new BigNumber(a);
        // a ^ b = a if {a = 0 (and b != 0))} or {a = 1}

        if (b == 1)
            return new BigNumber(a);
        // a ^ 1 = a

        BigNumber tmp = new BigNumber(a);
        BigNumber result = new BigNumber(1);
        while (b > 0) {
            if (isImpar(b)) {
                result = multiply(result, tmp);
            }
            b >>= 1;
            tmp = multiply(tmp, tmp);
        }
        return result;
    }
}
