package service;

public interface IFullService<T> extends IUpdatableService<T>{
    void add(T entity);
    void delete(T entityOld);
}
