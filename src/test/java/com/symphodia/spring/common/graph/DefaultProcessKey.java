package com.symphodia.spring.common.graph;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class DefaultProcessKey implements ProcessKey {

    private String type;

    public DefaultProcessKey(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultProcessKey that = (DefaultProcessKey) o;

        return Objects.equal(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.type);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(type).toString();
    }

}
