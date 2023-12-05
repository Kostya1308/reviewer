package ru.clevertec.courses.reviewer.chain;

public interface CheckerChain<T> {

    void runChain(T t);

}
