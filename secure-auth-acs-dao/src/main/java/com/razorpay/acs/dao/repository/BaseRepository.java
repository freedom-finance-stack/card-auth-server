package com.razorpay.acs.dao.repository;

import com.razorpay.acs.dao.model.BaseEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID> extends CrudRepository<T, ID> {
    @Override
    @Query("update #{#entityName} e set e.deleted_at= now() where e.id = ?1")
    @Modifying
    void deleteById(ID id);

    @Override
    default void deleteAllById(Iterable<? extends ID> ids) {
        ids.forEach(id -> deleteById(id));
    }

    @Override
    default void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(entity -> delete(entity));
    }

    @Override
    @Query("update #{#entityName} e set e.deleted_at= now()")
    @Modifying
    void deleteAll();

    @Override
    default void delete(T entity) {
        delete((T) entity.getId());
    }

}
