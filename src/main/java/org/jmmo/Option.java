package org.jmmo;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Класс обрамляющий значения, которые могут быть пусты, но использовать null нежелательно.
 * За основу взят Option из Scala
 * @author Tomas Shestakov
 *
 * @param <T> тип обрамляемого значения
 */
public abstract class Option<T> implements Serializable, Iterable<T> {

    /**
     * Означает, что ничего нет
     */
    public static final Option<?> None = new None();

    /**
     * Возвращает обрамленное {@link Option} некоторое значение
     * @param value значение
     * @param <V> тип значения
     * @return обрамленное {@link Option} значение
     */
    public static <V> Option<V> some(V value) {
        return new Some<V>(value);
    }

    /**
     * Возвращает обрамленное {@link Option} пустое значение
     * @param <V> тип значения
     * @return обрамленное {@link Option} пустое значение
     */
    @SuppressWarnings("unchecked")
    public static <V> Option<V> none() {
        return (Option<V>) None;
    }

    /**
     * Возвращает None если value равно null, иначе Some(value).
     * @param value значение
     * @param <V> тип значения
     */
    public static <V> Option<V> option(V value) {
        return value == null ? Option.<V>none() : some(value);
    }

    /**
     * @return если значение пусто возвращет true
     */
    public abstract boolean isEmpty();

    /**
     * @return если значение не пусто возвращет true
     */
    public boolean isDefined() {
        return !this.isEmpty();
    }

    /**
     * Возвращает обрамленное значение, если опция не пуста, иначе, значение по умолчанию.
     * @param defaultValue значение по умолчанию
     */
    public T getOrElse(T defaultValue) {
        return isEmpty() ? defaultValue : get();
    }

    /**
     * Возвращает обрамленное значение, если опция не пуста, иначе null
     */
    public T getOrNull() {
        return getOrElse(null);
    }

    /**
     * Возвращает обрамленное значение, если опция не пуста, иначе бросает исключение
     * @param e исключение
     */
    public T getOrException(RuntimeException e) {
        if (isDefined()) {
            return get();
        }
        else {
            throw e;
        }
    }

    public Iterator<T> iterator() {
        return isEmpty() ? Collections.<T>emptyList().iterator() : Collections.singletonList(get()).iterator();
    }

    /**
     * @return Обрамленное значение.
     * Если значение пусто будет {@link java.util.NoSuchElementException}
     */
    public abstract T get();

    private static final class Some<T> extends Option<T> {
        private static final long serialVersionUID = -6594285056904344306L;
        private final T value;

        public Some(T value) {
            this.value = value;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public T get() {
            return this.value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            Some other = (Some) obj;

            if (get() != null ? !get().equals(other.get()) : other.get() != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return get() != null ? get().hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Some{" + get() + "}";
        }
    }

    private static final class None extends Option<Object> {
        private static final long serialVersionUID = -4496938325844728235L;

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public Object get() {
            throw new NoSuchElementException("None.get");
        }

        @Override
        public String toString() {
            return "None";
        }
    }
}
