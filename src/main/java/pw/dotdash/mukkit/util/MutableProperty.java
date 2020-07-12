package pw.dotdash.mukkit.util;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class MutableProperty<T, R> {

    private final Function<T, R> getter;
    private final BiConsumer<T, R> setter;

    public MutableProperty(Function<T, R> getter, BiConsumer<T, R> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public final R get(T obj) {
        return this.getter.apply(obj);
    }

    public final void set(T obj, R value) {
        this.setter.accept(obj, value);
    }
}