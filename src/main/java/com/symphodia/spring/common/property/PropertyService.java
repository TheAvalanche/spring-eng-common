package com.symphodia.spring.common.property;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Service
public class PropertyService {

    @Autowired
    PropertyRepository propertyRepository;

    LoadingCache<PropertyKey,Property> cache;

    @PostConstruct
    public void init() {
        cache = CacheBuilder
                .newBuilder()
                .build(new CacheLoader<PropertyKey, Property>() {
                    @Override
                    public Property load(PropertyKey key) throws Exception {
                        Property property = loadPropertyFromDatabase(key);

                        if (property == null) {
                            property = createDefaultProperty(key);
                        }

                        return property;
                    }
                });
    }

    private Property createDefaultProperty(PropertyKey key) {
        Property property = Property.createFrom(key);
        propertyRepository.save(property);
        return property;
    }

    private Property loadPropertyFromDatabase(PropertyKey key) {
        return propertyRepository.findOne(key.getName());
    }

    public void clearCache() {
        cache.invalidateAll();
    }

    public String cacheContent() {
        final StringBuilder sb = new StringBuilder();
        cache.asMap().entrySet().forEach(e -> sb.append(String.format("%s : %s \n", e.getKey().getName(), e.getValue().getValue())));
        return sb.toString();
    }

    @Transactional
    public void putValue(PropertyKey key, String value) {
        Property property = loadPropertyFromDatabase(key);

        if (property == null) {
            property = createDefaultProperty(key);
        }
        property.setValue(value);

        cache.invalidateAll();
    }

    public Integer getIntValue(PropertyKey key) {
        return (Integer) parseValue(key);
    }

    public boolean getBooleanValue(PropertyKey key) {
        return (Boolean) parseValue(key);
    }

    public String getValue(PropertyKey key) {
        return (String) parseValue(key);
    }

    private Object parseValue(PropertyKey key) {
        Property property = cache.getUnchecked(key);

        switch (property.getPropertyType()) {
            case BOOLEAN:
                return Boolean.valueOf(property.getValue());
            case INTEGER:
                return Integer.valueOf(property.getValue());
            case STRING:
            default:
                return Strings.nullToEmpty(property.getValue());
        }
    }
}
