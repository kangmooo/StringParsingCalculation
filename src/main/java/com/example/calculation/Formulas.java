package com.example.calculation;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.example.calculation.PostFixParser.popStack;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * 계산 Tag 연산
 * list 타입의 후위연산으로 파싱된 계산식 list loop 를 돌며 계산 수행
 * 특수 계산식의 변수명은 Diagnostics Function blocks for calculated tags 문서 기본으로 하여 작성 하였음
 * C:\\UniSteam97_JNI.dll" 있어야 함
 *
 * @author SungTae, Kang
 */
@Slf4j
public final class Formulas {

    /* 01 절대값 */
    private static Function<List<Object>, Double> abs = stack -> {
        if (nullExist(stack, 1)) {
            return null;
        } else {
            Double value = (Double) popStack.apply(stack);
            return Math.abs(value);
        }
    };

    /* 02 제곱근 */
    private static Function<List<Object>, Double> sqrt = stack -> {
        if (nullExist(stack, 1)) {
            return null;
        } else {
            Double value = (Double) popStack.apply(stack);
            return Math.sqrt(value);
        }
    };

    /* 03 평균 */
    private static Function<List<Double>, Double> average = dList -> {
        double sum = 0.0;
        for (double d : dList) {
            sum += d;
        }
        return sum / dList.size();
    };

    /* 04 중앙값 */
    private static Function<List<Double>, Double> median = dList -> {
        int index = dList.size() / 2;                               // 배열의 중간 index 를 찾음
        return (dList.size() % 2 == 1)
                ? dList.get(index)                                  // 배열의 인자 갯수가 홀수일 경우
                : (dList.get(index - 1) + dList.get(index)) / 2.0;  // 배열의 인자 갯수가 짝수일 경우
    };

    /* 05 분포값 */
    private static Function<List<Double>, Double> spread = tagValue -> { // 배열 내에서 high, low 벗어나는 값을 제외 하고 | 최대 - 최소 = result |
        return tagValue.get(tagValue.size() - 1) - tagValue.get(0);
    };

    /* 06 최대값 */
    private static Function<List<Double>, Double> maximum = tagValue -> { // 배열 내에서 high, low 벗어나는 값을 제외 하고 | 최대값 = result |
        return tagValue.get(tagValue.size() - 1);
    };

    /* 07 합계 */
    private static Function<List<Double>, Double> sum = tagValue -> {
        double result = 0.0;
        for (double i : tagValue) {
            result += i;
        }
        return result;
    };

    /* 32 최소값 */
    private static Function<List<Double>, Double> minimum = tagValue -> { // 배열 내에서 high, low 벗어나는 값을 제외 하고 | 최소값 = result |
        Double max = tagValue.get(tagValue.size() - 1);
        Double min = tagValue.get(0);
        System.out.println("minimum = " + min + "  maximum = " + max);
        return min;
    };
    /* 34 select */
    private static Function<List<Object>, Double> select = stack -> {
        Double input2 = (Double) popStack.apply(stack), input1 = (Double) popStack.apply(stack);
        boolean condition = (boolean) popStack.apply(stack);
        System.out.println(" select(" + condition + ", " + input1 + ", " + input2 + ") = " + (condition == TRUE ? input1 : input2));
        return condition == TRUE ? input1 : input2;
    };

    /* 35 or_exist */
    private static Function<List<Object>, Boolean> or_exist = stack -> {
        Object obj2 = popStack.apply(stack), obj1 = popStack.apply(stack);
        Boolean result = (!(null == obj1) || !(null == obj2)) ? TRUE : FALSE;
        System.out.println(" or_exist(" + obj1 + ", " + obj2 + ") = " + result);
        return result;
    };

    /* 36 and_exist */
    private static Function<List<Object>, Boolean> and_exist = stack -> {
        Object obj2 = popStack.apply(stack), obj1 = popStack.apply(stack);
        Boolean result = (!(null == obj1) && !(null == obj2)) ? TRUE : FALSE;
        System.out.println(" and_exist(" + obj1 + ", " + obj2 + ") = " + result);
        return result;
    };

    /* 37 is_exist */
    private static Function<List<Object>, Boolean> is_exist = stack -> {
        Object obj1 = popStack.apply(stack);
        Boolean result = !(null == obj1);
        System.out.println(" is_exist(" + obj1 + ") = " + result);
        return result;

    };

    private static Boolean nullExist(List<Object> stack, int cnt) {
        int size = stack.size();
        int first = stack.size() - cnt;
        for (int i = first; i < size; i++) {
            Object obj = stack.get(i);
            if (null == obj) {
                return TRUE;
            }
        }
        return FALSE;
    }

    /**
     * 계산식 함수 객체
     */
    public static final Map<String, Function> functionMap = Collections.unmodifiableMap(new HashMap<String, Function>() {{
        put("ABS", abs);
        put("SQRT", sqrt);
        put("AVERAGE", average);
        put("MEDIAN", median);
        put("SPREAD", spread);
        put("MAXIMUM", maximum);
        put("SUM", sum);
        put("MINIMUM", minimum);
        put("SELECT", select);
        put("OR_EXIST", or_exist);
        put("AND_EXIST", and_exist);
        put("IS_EXIST", is_exist);
    }});
}