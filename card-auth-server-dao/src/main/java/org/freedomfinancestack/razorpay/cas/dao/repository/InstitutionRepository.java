package org.freedomfinancestack.razorpay.cas.dao.repository;

import org.freedomfinancestack.razorpay.cas.dao.model.Institution;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.data.rest.core.annotation.RestResource;

@Repository
@RepositoryRestResource
public interface InstitutionRepository extends BaseRepository<Institution, String> {}
