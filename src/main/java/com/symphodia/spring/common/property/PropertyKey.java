package com.symphodia.spring.common.property;

public interface PropertyKey {

    String getName();
    String getValue();
    String getDescription();
    PropertyType getPropertyType();

}
