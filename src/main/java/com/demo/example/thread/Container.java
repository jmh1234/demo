package com.demo.example.thread;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * Container
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
@Getter
@Setter
public class Container {
    private Optional<Integer> value = Optional.empty();
}
