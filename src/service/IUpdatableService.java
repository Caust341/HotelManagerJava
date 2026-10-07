package service;

import java.util.List;

public interface IUpdatableService<T> {
    List<T> showAll();
    T findById(Long input);
    T findByIndex(Integer i, List<T> list);
    void update(T oldT, T newT);
}
