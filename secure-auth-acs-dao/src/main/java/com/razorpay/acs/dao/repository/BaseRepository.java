package com.razorpay.acs.dao.repository;

import com.razorpay.acs.dao.model.BaseEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID> extends CrudRepository<T, ID> {

    @Query("update #{#entityName} e set e.deleted_at= now() where e.id = ?1")
    @Modifying
    @Transactional
    void softDeleteById(ID id);

    @Override
    default void deleteAllById(Iterable<? extends ID> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void deleteById(ID id) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void delete(T entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void deleteAll(Iterable<? extends T> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void deleteAll() {
        throw new UnsupportedOperationException();
    }

}
