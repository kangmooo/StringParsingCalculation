package com.example.calculation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.example.calculation.CharacterClassification.*;
import static com.example.calculation.Formulas.functionMap;


/**
 * 계산 tag 계산식 -> 후위 연산식으로 변환
 * 계산식 String 타입의 계산식을  후위연산으로 list<String> 타입 파싱
 *
 * @author SungTae, Kang
 */
public class PostFixParser {

    // 스택에 값을 넣음.
    static BiConsumer<List<Object>, Object> pushStack = (stack, o) -> stack.add(o);

    // 스택의 최상위 값을 조회함.
    static Function<List<Object>, Object> getTopStack =  stack -> stack.size() == 0 ? null : stack.get(stack.size() - 1);

    // 스택의 최상위 값을 반환함.
    static Function<List<Object>, Object> popStack = stack -> stack.remove(stack.size() - 1);

    // 연산자 우선순위 반환
    private static Function<String, Integer> precedence = op -> {
        switch (op) {
            case "(":
                return 0;
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 4;
        }
    };

    // 줄바꿈 제거 (trim), 대문자
    private static Function<String, String> trimAndUpperCase = calcExp -> calcExp.replaceAll(" |\t|\n|\r|System.getProperty(\"line.separator\")", "").toUpperCase();

    // string buffer에 값이 있으면 토큰으로 추가하고, string buffer를 비운다.
    private static BiFunction<String, List<String>, String> emptyStringBuffer = (stringBuffer, tokenList) -> {
        if (stringBuffer.length() > 0) {
            tokenList.add(stringBuffer);
        }
        return "";
    };

    /**
     * 계산식 String을 토큰화하여 리스트로 반환함.
     *
     * @param calcExp 계산식
     * @return 계산식 String을 토큰화하 된 리스트
     */
    static Function<String, List<String>> makeTextToToken = calcExp -> {

        calcExp = trimAndUpperCase.apply(calcExp);

        String sb = "";
        int length = calcExp.length();
        List<String> tokenList = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            String beforeC  =   (i>0)        ? String.valueOf(calcExp.charAt(i-1)) : "";
            String afterC   =   (i<length-1) ? String.valueOf(calcExp.charAt(i+1)) : "";
            String c = String.valueOf(calcExp.charAt(i));

            if (isLeftBracket.test(c)) {
                sb = emptyStringBuffer.apply(sb, tokenList);
                tokenList.add(c);
                if(!isLeftSquareBracket.test(afterC)) {
                    tokenList.add("(");
                }
            } else if (isRightBracket.test(c)) {
                sb = emptyStringBuffer.apply(sb, tokenList);
                if(!isRightSquareBracket.test(beforeC)) {
                    tokenList.add(")");
                }
                tokenList.add(c);
            } else if (isLeftSquareBracket.test(c)) {
                sb = emptyStringBuffer.apply(sb, tokenList);
                tokenList.add(c);
                tokenList.add("(");
            } else if (isRightSquareBracket.test(c)) {
                sb = emptyStringBuffer.apply(sb, tokenList);
                tokenList.add(")");
                tokenList.add(c);
            } else if (isComma.test(c)) {
                sb = emptyStringBuffer.apply(sb, tokenList);
                if(!"]".equals(beforeC)) {
                    tokenList.add(")");
                }
                tokenList.add("(");
            } else if (isMinus.test(calcExp, i)) {
                sb = sb.concat(c);
            } else if (isOperation.test(c)) {
                sb = emptyStringBuffer.apply(sb, tokenList);
                tokenList.add(c);
            } else {
                sb = sb.concat(c);
            }
        }
        if(!"".equals(sb)
                && !isLeftBracket.test(sb)
                && !isRightBracket.test(sb)
                && !isLeftSquareBracket.test(sb)
                && !isRightSquareBracket.test(sb)
                && !isComma.test(sb)
                && !isOperation.test(sb)){
            tokenList.add(sb);
        }
        return tokenList;
    };

    /**
     * 중위연산식의 토큰리스트를 후위연산식으로 변환
     *
     * @param tokenList String 계산식-> 토큰화된 리스트
     * @return 후위연산식으로 변환된 리스트
     */
    static Function<List<String>, List<String>> toPostfix = tokenList -> {
        List<String> postfixToken = new ArrayList<>();
        List<Object> stack = new ArrayList<>();

        for (String token : tokenList) {
            if (isLeftBracket.test(token)) { // "("
                pushStack.accept(stack, token);
            } else if (isRightBracket.test(token)) { // ")"
                while (!isLeftBracket.test((String) getTopStack.apply(stack))) {
                    postfixToken.add((String) popStack.apply(stack));
                }
                popStack.apply(stack); // remove '('
            } else if (isOperation.test(token) || functionMap.containsKey(token.toUpperCase())|| Arrays.asList("LIST","PREVIOUS").contains(token.toUpperCase())) {   // operation
                while (getTopStack.apply(stack) != null && precedence.apply((String) getTopStack.apply(stack)) >= precedence.apply(token)) {    // stack에 들어있는 것이 우선순위가 높으면 pop
                    postfixToken.add((String) popStack.apply(stack));
                }
                pushStack.accept(stack, token);
            } else {    // Other (Tag Alias, '[', ']' etc...)
                postfixToken.add(token);
            }
        }
        while (getTopStack.apply(stack) != null) {
            postfixToken.add((String) popStack.apply(stack));
        }
        return postfixToken;
    };
}