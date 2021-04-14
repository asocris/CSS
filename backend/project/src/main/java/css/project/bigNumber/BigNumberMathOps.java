package css.project.bigNumber;

import css.project.exception.custom.ArithmeticAppException;
import org.apache.commons.lang3.tuple.MutablePair;

import static java.lang.Integer.max;

public class BigNumberMathOps {
    public static int compare(BigNumber a, BigNumber b) {
        BigNumber tempa = new BigNumber(a);
        BigNumber tempb = new BigNumber(b);
        tempa.adjustLength();
        tempb.adjustLength();
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
        return result;
    }

    public static BigNumber substract(BigNumber a, BigNumber b) {
        if (compare(a, b) == -1)
            throw new ArithmeticAppException("Substract result Negative");

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
                    transport = 0;
                }
                else  {
                    result.getNumber()[i] += BigNumber.getBASE();
                    result.getNumber()[i] -= transport;
                    transport = 1;
                }
            }
        }
        result.adjustLength();
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
        return result;
    }

    public static BigNumber multiply(BigNumber a, long b) {
        BigNumber result = new BigNumber();
        int newLength = a.getLength() * 25 + 1;
        result.increaseLength(newLength);
        long transport = 0;
        for (int i = 0; i < a.getLength() || transport != 0; i++) {
            if (i < a.getLength())
                transport += a.getNumber()[i] * b;
            result.getNumber()[i] = transport % BigNumber.getBASE();
            transport /= BigNumber.getBASE();
        }
        result.adjustLength();
        return result;
    }



    public static MutablePair<BigNumber, BigNumber> divide(BigNumber a, BigNumber b) {
        BigNumber quotient = new BigNumber();
        BigNumber reminder = new BigNumber();
        MutablePair<BigNumber, BigNumber> result = new MutablePair<>(quotient, reminder);
        int newLength = a.getLength() + 1;
        quotient.increaseLength(newLength);
        //TODO: WARNING! NOT IMPLEMENTED!
        quotient.adjustLength();
        reminder.adjustLength();
        return result;
    }

    public static BigNumber divideQutient(BigNumber a, BigNumber b) {
        return divide(a, b).getLeft();
    }

    public static BigNumber divideReminder(BigNumber a, BigNumber b) {
        return divide(a, b).getRight();
    }

    public static BigNumber sqrt(BigNumber a) {
        return a;
    }

    public static BigNumber pow(BigNumber a, BigNumber b) {
       return a;
    }
}
