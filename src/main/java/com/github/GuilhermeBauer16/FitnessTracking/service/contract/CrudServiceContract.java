package com.github.GuilhermeBauer16.FitnessTracking.service.contract;

import java.util.List;

public interface CrudServiceContract<T, I> {

    T create(T target);

    T update(T target);

    T findById(I id);

    List<T> findAll();

    void delete(I id);
}
