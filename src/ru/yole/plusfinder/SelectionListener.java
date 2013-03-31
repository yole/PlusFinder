package ru.yole.plusfinder;

import ru.yole.plusfinder.model.BaseEntity;

/**
 * @author yole
 */
public interface SelectionListener<T extends BaseEntity> {
    void onSelected(T entity);
}
