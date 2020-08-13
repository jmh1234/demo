package com.demo.example.thread;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class Container {
    private Optional<Integer> value = Optional.empty();
}
