package com.example.calculation;

import com.example.calculation.dto.CalcResult;
import com.example.calculation.interfaces.TriFunction;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static com.example.calculation.CharacterClassification.*;
import static com.example.calculation.Formulas.functionMap;
import static com.example.calculation.PostFixParser.*;
import static com.example.calculation.dto.CalcResult.Status.FAIL;
import static com.example.calculation.dto.CalcResult.Status.SUCCESS;
import static com.example.calculation.enums.Message.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Optional.ofNullable;

/**
 * 계산 Tag 연산
 * list 타입의 후위연산으로 파싱된 계산식 list loop 를 돌며 계산 수행
 * 특수 계산식의 변수명은 Diagnostics Function blocks for calculated tags 문서 기본으로 하여 작성 하였음
 *
 * @author SungTae, Kang
 */
@Slf4j
public final class CalculationCoreFunc {
    /**
     * "AVERAGE", "MEDIAN", "SPREAD", "MAXIMUM", "MINIMUM", "SUM" 의 function 들은
     * function([],min,max,boolean) 의 함수로 min max 값을 넘지 않는 data 들만 계산 하도록 하기 위해 임계치를 벗어 나지 않는 값만 return 한다.
     * 임계 값 범위 외부에 존재 할 때
     * ignorable == true or  ignorable == null 이면 값을 제외 하고 진행
     * ignorable == false 이면  NaN 으로 리턴
     *
     * @param stack 토큰 리스트
     * @return 임계치를 벗어 나지 않는 data 만  return
     */
    private static BiFunction<List<Object>, Map, List<Double>> thresholdCheck = (stack, testData) -> {
        boolean ignorable = TRUE;
        Double thLower = null, thUpper = null;
        List<Double> tagValue = new ArrayList<>();

        if ("FALSE".equals(getTopStack.apply(stack)) || "TRUE".equals(getTopStack.apply(stack))) { // ignorable 변수 저장
            ignorable = Boolean.valueOf((String) getTopStack.apply(stack));
            popStack.apply(stack);
        }

        if (!isSquareBracket.test(String.valueOf(getTopStack.apply(stack)))) { // 최대 최소 저장
            thUpper = getOperationParams(testData, popStack.apply(stack));
            thLower = getOperationParams(testData, popStack.apply(stack));
        }

        if ("]".equals(getTopStack.apply(stack))) { // 계산 배열
            popStack.apply(stack); // remove ']'
            while (getTopStack.apply(stack) instanceof Double || !("[".equals(getTopStack.apply(stack)))) {
                if ("NA".equals(getTopStack.apply(stack))) {
                    log.warn(String.valueOf(REFERENCE_VAULE_IS_NA));
                    return null;
                }
                Double value = (Double) popStack.apply(stack);
                // 임계 값 범위 외부에 존재 할 때 ignorable == true or  ignorable == null 이면 값을 제외 하고 진행 ignorable == false   NaN 으로 리턴
                if ((!isNumber.test(String.valueOf(value))) || ((thUpper != null && thLower != null) && (value > thUpper || value < thLower))) {
                    if (ignorable == FALSE) {
                        log.warn(" ignorable = FALSE  | 임계 값 범위 외부에 존재 하므로 null return");
                        return null;
                    }
                } else {
                    tagValue.add(value);
                }
            }
            popStack.apply(stack); // remove '['
        }
        Collections.sort(tagValue); // 오름차순 정렬
        log.debug(" thresholdCheck result = {} ", tagValue);
        return tagValue;
    };

    /**
     * calculation function 을 호출하여 연산을 수행
     *
     * @param func  31개의 function 중의 수행해야 될 function
     * @param stack 수행될 data가 포함된 stack
     * @return calculation function 결과값
     */
    private static TriFunction<String, List<Object>, Map, CalcResult> callFunction = (func, stack, testData) -> {
        // null check
        if (stack.size() == 0) {
            return new CalcResult(FAIL, NO_DATA_MSG.getMsg(), null);
        }
        func = func.toUpperCase();
        List<Double> tagValue = new ArrayList<>();

        // TODO 계산식 의 args 타입 및 갯수 확인 필요 for(web validation check)
        if (isThresholdCheckFunc.test(func)) {  // thresholdCheck
            tagValue = thresholdCheck.apply(stack, testData);
            if (tagValue == null || tagValue.size() <= 0) {
                return new CalcResult(FAIL, THERE_ARE_NO_VALUES_WITHIN_THE_THRESHOLD.getMsg(), null);
            }
        }
        try {
            if (Arrays.asList("MEDIAN", "AVERAGE", "SPREAD", "MAXIMUM", "MINIMUM", "SUM").contains(func)) {
                if (tagValue.size() == 0) {
                    return new CalcResult(FAIL, THERE_ARE_NO_VALUES_WITHIN_THE_THRESHOLD.getMsg(), null);
                }
                return executeFunction(func, tagValue);
            } else {
                return executeFunction(func, stack);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("warn function = {} message = {}", func, e.getMessage());
            return new CalcResult(FAIL, e.getMessage(), null);
        }
    };

    // null 처리
    private static CalcResult executeFunction(String func, List list) {
        // TODO alias 의 값들을 다 넣어 줘야함
        return ofNullable(functionMap.get(func).apply(list))
                .map(v -> new CalcResult(SUCCESS, null, v))
                .orElse(new CalcResult(FAIL, REFERENCE_VAULE_IS_NA.getMsg(), null));
    }

    /**
     * 연산 수행
     *
     * @param stack 연산 대상
     * @param op    연산자 [+, -, *, /, ^(제곱근)]
     * @return 연산 결과 값
     */
    private static TriFunction<Double, Double, String, Double> executeOperation = (first, last, op) -> {
        if (first == null || last == null) {
            return null;
        }
        switch (op) {
            case "+":
                return (first + last);
            case "-":
                return (first) - (last);
            case "*":
                return first * last;
            case "/": {
                if (!(last == 0))
                    return first / last;
            }
            case "^":
                return Math.pow(first, last);
            default:
                return null;
        }
    };

    /**
     * postfixToken 를 loop 돌면서 값 대입, 연산 수행 으로 전체 계산식 postfixToken의 결과값 구함
     *
     * @param postfixToken   후위연산으로 List<String> 으로 parsing 된 계산식
     * @param selfPamValue   계산식에 대입 될 alias 들의 data 집합( key = alias, value = data )
     * @param parentPamValue 계산식에 대입 될 상위 asset 의 pam_mapping 에 해당하는 alias 들의 data 집합( key = alias, value = data )
     * @return postfixToken 를 loop 돌면서 값 대입, 연산 수행 으로 전체 계산식 postfixToken의 최종 결과값
     */
    public static BiFunction<String, Map, CalcResult> executeCalc = (calcExpression, testData) -> { //calcExp 계산 tag 계산식 수행, postfixToken (후위연산식 토큰), tagValues(Alias 에 대입할 값)

        try {
            List<String> postfixToken = toPostfix.apply(makeTextToToken.apply(calcExpression)); // 후위 연산 으로 parsing
            List<Object> stack = new ArrayList<>();
            if (postfixToken.size() == 0) {
                return new CalcResult(FAIL, CAN_NOT_PARSING_CALCULATION_EXPRESSION.getMsg(), null);
            }
            for (String token : postfixToken) {
                if (isOperation.test(token)) {                                       // 연산자 [+, -, *, /, ^(제곱근)]
                    Double last = getOperationParams(testData, popStack.apply(stack));
                    Double first = getOperationParams(testData, popStack.apply(stack));
                    pushStack.accept(stack, executeOperation.apply(first, last, token));
                    continue;
                } else if (isInActive.test(token)) {                                 // inactive
                    pushStack.accept(stack, null);
                    continue;
                } else if (Arrays.asList("LIST", "PREVIOUS").contains(token.toUpperCase())) {                                 // inactive
                    getAliasDataList(stack, testData);
                    continue;
                } else if (functionMap.containsKey(token.toUpperCase())) {                                 // inactive
                    setAliasData(stack, testData);
                    String tmp = token + stack;
                    CalcResult result = callFunction.apply(token, stack, testData);
                    if (result.getStatus().equals(SUCCESS)) {
                        pushStack.accept(stack, result.getResult());
                        continue;
                    } else {
                        log.warn(tmp);
                        return result;
                    }
                } else {
                    pushStack.accept(stack, token);
                    continue;
                }
            }
            Object r = popStack.apply(stack);
            return new CalcResult(SUCCESS, null, r);
        } catch (Exception e) {
            return new CalcResult(FAIL, UNEXPECTED_ERROR.getMsg(), null);
        }

    };

    private static Double getOperationParams(Map testData, Object obj) {
        if (isNumber.test(obj)) {
            return Double.parseDouble(String.valueOf(obj));
        } else {
            return (double) testData.get(String.valueOf(obj));
        }
    }

    private static void getAliasDataList(List<Object> stack, Map testData) {
        int size = Integer.parseInt(String.valueOf(popStack.apply(stack)));
        String alias = (String) popStack.apply(stack);
        List<Object> result = new ArrayList<>();
        result.add("[");
        IntStream.range(0, size).forEach(v -> {
            result.add(testData.get(alias));
        });
        result.add("]");
        stack.addAll(result);
    }

    private static void setAliasData(List<Object> stack, Map testData) {
        IntStream.range(0, stack.size()).forEach(i -> {
            String str = String.valueOf(stack.get(i));
            if (isNumber.test(str)) {
                stack.set(i, Double.parseDouble(str));
            } else if ("NA".equalsIgnoreCase(str)) {
                stack.set(i, null);
            } else if (!functionMap.containsKey(str) && !isBoolean.test(str) && !isInActive.test(str) && !isNumber.test(str) && !isSquareBracket.test(str) && !isComma.test(str)) {
                if (Optional.ofNullable(testData).isPresent()) {
                    stack.set(i, testData.get(str));
                }
            }
        });
    }

}