package com.example.calculation;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Character Classification 문자 분류
 * (* 계산식의 string의 유형 구분 하는 용도로 쓰임)
 *
 * @author SungTae, Kang
 */
final class CharacterClassification {

    // 사칙연산 및 제곱근 '+' '-' '*' '/' '^'
    static Predicate<String> isOperation = str -> Arrays.asList("+", "-", "*", "/", "^").contains(str);
    // boolean 값 체크
    static Predicate<String> isBoolean = str -> str.equalsIgnoreCase("false") || str.equalsIgnoreCase("true");
    // inactive 상태 확인
    static Predicate<String> isInActive = str -> "INACTIVE".equals(str);
    // 숫자 체크
    static Predicate<Object> isNumber = str ->{
        try {
            Double.parseDouble(String.valueOf(str));
            return true;
        } catch (Exception e) {
            return false;
        }
    };
    // 대괄호 '[', ']'
    static Predicate<String> isSquareBracket = str -> "[".equals(str) || "]".equals(str);
    // 왼쪽 대괄호 '['
    static Predicate<String> isLeftSquareBracket = str -> "[".equals(str);
    // 오른쪽 대괄호 ']'
    static Predicate<String> isRightSquareBracket = str -> "]".equals(str);
    // 쉼표     ','
    static Predicate<String> isComma = str -> ",".equals(str);
    // 왼쪽괄호 '('
    static Predicate<String> isLeftBracket = str -> "(".equals(str);
    // 오른쪽괄호 ')'
    static Predicate<String> isRightBracket = str -> ")".equals(str);

    // threshold check 를 하는 함수 인지 확인
    static Predicate<String> isThresholdCheckFunc = str -> Arrays.asList("AVERAGE", "MEDIAN", "SPREAD", "MAXIMUM", "MINIMUM", "SUM").contains(str);
    // 마이너스 체크(단항의 음수 or 이항 연산의 "-" 에 따라 체크)
    static BiPredicate<String, Integer> isMinus = (str, i) -> {
        if (str.charAt(i) == '-' && i == 0) {                       // 단항 연산 음수
            return true;
        } else if (str.charAt(i) == '-' && str.length() - 1 == i) {     // '-' 가 마지막 일때
            return false;                                           // 이항 연산 "-"
        } else if (str.charAt(i) == '-' && isNumber.test(str.substring(i + 1, i + 2))) {
            return (isOperation.test(String.valueOf(str.charAt(i - 1))) || isComma.test(String.valueOf(str.charAt(i - 1))) || str.charAt(i - 1) == '(' || str.charAt(i - 1) == '[');
        } else {
            return false;
        }
    };
}
