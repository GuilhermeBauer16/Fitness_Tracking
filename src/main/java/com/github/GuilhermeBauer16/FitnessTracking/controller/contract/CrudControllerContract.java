package com.github.GuilhermeBauer16.FitnessTracking.controller.contract;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CrudControllerContract<T, I> {

    ResponseEntity<T> create(T target);

    ResponseEntity<T> update(T target);

    ResponseEntity<T> findById(I id);

    ResponseEntity<Page<T>> findAll(final Pageable pageable);

    ResponseEntity<?> delete(I id);
}
