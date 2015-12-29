package com.symphodia.spring.common.property;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PROPERTY")
public class Property {

    @Id
    @Column(name="KEY")
    private String key;

    @Column(name="VALUE", length = 4000)
    private String value;

    @Column(name = "DESCRIPTION",length = 1000)
    private String description;

    @Column(name = "PROPERTY_TYPE")
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private PropertyType propertyType;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public static Property createFrom(PropertyKey propertyKey) {
        Property property = new Property();
        property.setKey(propertyKey.getName());
        property.setValue(propertyKey.getValue());
        property.setDescription(propertyKey.getDescription());
        property.setPropertyType(propertyKey.getPropertyType());
        return property;
    }
}
