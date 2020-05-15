package com.example.calculation.enums;


import com.example.calculation.interfaces.TriPredicate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * 비교연산자 집합
 *
 * @author SungTae, Kang
 */
public class Comparison {

    public static final Map<String, BiPredicate<Double, Double>> comparisonMap = Collections.unmodifiableMap(new HashMap<String, BiPredicate<Double, Double>>() {{
        put("greater",      (value, comparisonValue) -> value  >  comparisonValue);
        put("greaterEqual", (value, comparisonValue) -> value  >= comparisonValue);
        put("equal",        (value, comparisonValue) -> value.equals(comparisonValue));
        put("lesserEqual",  (value, comparisonValue) -> value  <= comparisonValue);
        put("lesser",       (value, comparisonValue) -> value  <  comparisonValue);
    }});

    public static TriPredicate<String, Double, Double> isOutOfComparisonValue = (operator, a, b) ->
            Optional.ofNullable(comparisonMap.get(operator))
                    .map(v -> v.test(a, b)).orElse(false);
//                    .orElseThrow(() -> new IllegalArgumentException("잘못된 연산자 입력입니다."));
}
