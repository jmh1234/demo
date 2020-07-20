package com.test.demo;

import java.util.Optional;

public class Container {
    private Optional<Integer> value = Optional.empty();

    Optional<Integer> getValue() {
        return value;
    }

    void setValue(Optional<Integer> value) {
        this.value = value;
    }
}
