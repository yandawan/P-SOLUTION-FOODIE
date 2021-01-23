package com.bfxy.rabbit.producer;

import java.util.Spliterator;
import java.util.function.Consumer;

public class NumCounterSpliterator implements Spliterator<Parent> {

    @Override
    public boolean tryAdvance(Consumer<? super Parent> action) {
        return false;
    }

    @Override
    public Spliterator<Parent> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return 0;
    }

    @Override
    public int characteristics() {
        return 0;
    }
}
