package com.symphodia.spring.common.property;

import org.springframework.data.repository.Repository;

public interface PropertyRepository extends Repository<Property, String> {

    void save(Property order);

    Property findOne(String key);

}
