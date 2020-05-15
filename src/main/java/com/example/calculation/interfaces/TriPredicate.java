package com.example.calculation.interfaces;


/**
 * 3 args 를 받는 FunctionalInterface
 *
 * @author SungTae, Kang
 */
@FunctionalInterface
public interface TriPredicate<T, U, V> {
    boolean test(T t, U u, V v);
}