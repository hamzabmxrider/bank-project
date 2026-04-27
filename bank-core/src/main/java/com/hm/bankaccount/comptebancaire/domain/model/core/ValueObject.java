package com.hm.bankaccount.comptebancaire.domain.model.core;

public class ValueObject<T> {

    private T value;

    protected ValueObject(final T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
