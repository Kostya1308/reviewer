package ru.clevertec.courses.reviewer.checker.rest;

import lombok.RequiredArgsConstructor;
import ru.clevertec.courses.reviewer.client.ReviewedAppClient;

@RequiredArgsConstructor
public abstract class AbstractRestChecker {

    protected final ReviewedAppClient reviewedAppClient;

    public abstract void check();

}
