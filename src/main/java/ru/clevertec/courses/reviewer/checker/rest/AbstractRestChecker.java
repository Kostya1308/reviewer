package ru.clevertec.courses.reviewer.checker.rest;

import lombok.RequiredArgsConstructor;
import ru.clevertec.courses.reviewer.client.ReviewedAppClient;

@RequiredArgsConstructor
public abstract class AbstractRestChecker {

    private final ReviewedAppClient reviewedAppClient;

    public abstract void check();

}
