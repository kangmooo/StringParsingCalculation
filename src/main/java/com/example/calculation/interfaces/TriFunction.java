package com.example.calculation.interfaces;


/**
 * 3 args 를 받는 FunctionalInterface
 *
 * @author SungTae, Kang
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}