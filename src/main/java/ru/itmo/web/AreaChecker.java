package ru.itmo.web;

import java.math.BigDecimal;

public class AreaChecker {
    public static boolean checkHit(BigDecimal x, BigDecimal y, BigDecimal r) {
        if (x.compareTo(BigDecimal.valueOf(5)) > 0 || x.compareTo(BigDecimal.valueOf(-5)) < 0) return false;
        if (y.compareTo(BigDecimal.valueOf(5)) > 0 || y.compareTo(BigDecimal.valueOf(-5)) < 0) return false;

        if (x.compareTo(BigDecimal.ZERO) >= 0 && y.compareTo(BigDecimal.ZERO) >= 0) {
            BigDecimal halfR = r.divide(BigDecimal.valueOf(2));
            BigDecimal vir = x.negate().add(halfR);
            return y.compareTo(vir) <= 0;
        }

        if (x.compareTo(BigDecimal.ZERO) <= 0 && y.compareTo(BigDecimal.ZERO) <= 0) {
            BigDecimal xSquared = x.multiply(x);
            BigDecimal ySquared = y.multiply(y);
            BigDecimal rSquared = r.multiply(r);
            return xSquared.add(ySquared).compareTo(rSquared) <= 0;
        }

        if (x.compareTo(BigDecimal.ZERO) >= 0 && y.compareTo(BigDecimal.ZERO) <= 0) {
            BigDecimal halfR = r.divide(BigDecimal.valueOf(2));
            return x.compareTo(r) <= 0 && y.compareTo(halfR.negate()) >= 0;
        }
        return false;
    }
}