package org.aec.hydro.utils;

import com.google.common.collect.ImmutableSet;
import net.minecraft.state.property.Property;

import java.util.Collection;
import java.util.Optional;

public class StringProperty extends Property<String> {
    private final ImmutableSet<String> values;

    protected StringProperty(String name, Collection<String> values) {
        super(name, String.class);
        this.values = ImmutableSet.copyOf(values);
    }

    public static StringProperty of(String name, String... values) {
        return new StringProperty(name, ImmutableSet.copyOf(values));
    }

    @Override
    public Collection<String> getValues() {
        return values;
    }

    @Override
    public String name(String value) {
        return value;
    }

    @Override
    public Optional<String> parse(String name) {
        return values.contains(name) ? Optional.of(name) : Optional.empty();
    }
}

