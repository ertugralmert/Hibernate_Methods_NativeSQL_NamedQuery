package com.mert.utility;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface IRepository<T,ID> {

    T save(T entity);
    Iterable<T> saveAll(Iterable<T> entities);
    Optional<T> findById(ID id);
    boolean existsById(ID id);
    List<T> findAll();


    List<T> findByColumnAndValue(String columnName, Object value);
    void deleteById(ID id);
    List<T> findAllByEntity(T entity);
}
