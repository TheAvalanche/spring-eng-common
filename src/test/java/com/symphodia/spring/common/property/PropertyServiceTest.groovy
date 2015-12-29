package com.symphodia.spring.common.property

import com.google.common.cache.LoadingCache
import spock.lang.Specification

import static com.symphodia.spring.common.property.PropertyServiceTest.PropertyEnum.TEST_PROPERTY_1
import static com.symphodia.spring.common.property.PropertyServiceTest.PropertyEnum.TEST_PROPERTY_2
import static com.symphodia.spring.common.property.PropertyType.*


class PropertyServiceTest extends Specification {

    PropertyService propertyService;

    PropertyRepository propertyRepository = Mock();

    LoadingCache<PropertyKey, Property> cache = Mock();

    def setup() {
        propertyService = new PropertyService(propertyRepository: propertyRepository, cache: cache);
    }

    def "test get string value"() {
        when:
        cache.getUnchecked(_) >> new Property(key: "TEST_KEY", value: "value", propertyType: STRING)
        then:
        propertyService.getValue(null) == "value"
    }

    def "test get boolean value"() {
        when:
        cache.getUnchecked(_) >> new Property(key: "TEST_KEY", value: "true", propertyType: BOOLEAN)
        then:
        propertyService.getBooleanValue(null) == Boolean.TRUE
    }

    def "test get integer value"() {
        when:
        cache.getUnchecked(_) >> new Property(key: "TEST_KEY", value: "10", propertyType: INTEGER)
        then:
        propertyService.getIntValue(null) == 10
    }

    def "test cache content"() {
        when:
        propertyRepository.findOne(_) >> null
        propertyService.init()

        then:
        propertyService.getValue(TEST_PROPERTY_1) == TEST_PROPERTY_1.value
        propertyService.getValue(TEST_PROPERTY_2) == TEST_PROPERTY_2.value

        propertyService.cacheContent().contains(String.format("%s : %s", TEST_PROPERTY_1, TEST_PROPERTY_1.value))
        propertyService.cacheContent().contains(String.format("%s : %s", TEST_PROPERTY_2, TEST_PROPERTY_2.value))
    }

    def "test put value"() {
        setup:
        Property property = Property.createFrom(TEST_PROPERTY_1)
        propertyRepository.findOne(TEST_PROPERTY_1.name()) >> property
        propertyService.init()
        when:
        propertyService.putValue(TEST_PROPERTY_1, "test.package")
        then:
        propertyService.getValue(TEST_PROPERTY_1) == "test.package"
        when:
        propertyService.putValue(TEST_PROPERTY_1, "test.package.updated")
        then:
        propertyService.getValue(TEST_PROPERTY_1) == "test.package.updated"
    }

    enum PropertyEnum implements PropertyKey {
        TEST_PROPERTY_1("test", "test", STRING),
        TEST_PROPERTY_2("test", "test", STRING)

        String value

        String description

        PropertyType propertyType

        PropertyEnum(defaultValue, defaultDescription, defaultPropertyType) {
            this.value = defaultValue;
            this.description = defaultDescription;
            this.propertyType = defaultPropertyType;
        }

        @Override
        String getName() {
            return name()
        }

        @Override
        String getValue() {
            return value
        }

        @Override
        String getDescription() {
            return description
        }

        @Override
        PropertyType getPropertyType() {
            return propertyType
        }
    }

}
