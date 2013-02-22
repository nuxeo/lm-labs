package com.leroymerlin.common.core.utils;

import java.util.HashMap;
import java.util.Map;

public final class BytesFormatUtils {
    
    private enum Notation {
        OCTET("", 1),
        KILO("K", 1024),
        MEGA("M", 1024*1024),
        GIGA("G", 1024*1024*1024);
        
        private String symbol;
        private long coefficient;
        
        private Notation(String symbol, long coefficient) {
            this.symbol = symbol;
            this.coefficient = coefficient;
        }

        private static final Map<String, Notation> stringToEnum = new HashMap<String, Notation>();
        static { // Initialize map from constant name to enum constant
            for (Notation op : values())
                stringToEnum.put(op.symbol(), op);
        }
        
        /**
         * @return the symbol
         */
        public String symbol() {
            return symbol;
        }

        /**
         * @return the coefficient
         */
        public long coefficient() {
            return coefficient;
        }
    }

    private BytesFormatUtils() {}
    
    public static String BytesInWord(long size, String format, String language) {
        Notation unit = Notation.stringToEnum.get(format);
        if (unit == null) {
            throw new IllegalArgumentException("Unkwown unit '" + format + "'");
        }
        return String.format("%d %s", Math.round(((double )size)/unit.coefficient()), geti18nUnit(unit, language));
    }

    private static String geti18nUnit(Notation unit, String language) {
        // TODO
        return unit.symbol() + "o";
    }

}
