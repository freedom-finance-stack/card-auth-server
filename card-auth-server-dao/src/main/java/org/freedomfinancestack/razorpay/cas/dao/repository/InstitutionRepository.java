package org.freedomfinancestack.razorpay.cas.dao.repository;

import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends BaseRepository<Institution, String> {

    @Query(
            "update #{#entityName} e set e.deletedAt = now(), e.deletedBy = :user where e.id ="
                    + " :id")
    @Modifying
    void softDeleteById(@Param("user") String user, @Param("id") String id);
}
