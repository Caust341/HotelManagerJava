package repository;

import java.util.List;

public interface IRepository<T> {
    List<T> read();
    void write(List<T> list);
}
