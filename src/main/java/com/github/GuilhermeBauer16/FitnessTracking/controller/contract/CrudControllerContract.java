package com.github.GuilhermeBauer16.FitnessTracking.controller.contract;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CrudControllerContract<T, I> {

    ResponseEntity<T> create(T target);

    ResponseEntity<T> update(T target);

    ResponseEntity<T> findById(I id);

    ResponseEntity<List<T>> findAll();

    ResponseEntity<?> delete(I id);
}
