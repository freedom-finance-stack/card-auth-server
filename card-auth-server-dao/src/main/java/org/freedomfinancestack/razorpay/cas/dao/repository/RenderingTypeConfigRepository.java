package org.freedomfinancestack.razorpay.cas.dao.repository;

import java.util.List;
import java.util.Optional;

import org.freedomfinancestack.razorpay.cas.dao.model.RenderingTypeConfig;
import org.freedomfinancestack.razorpay.cas.dao.model.RenderingTypeConfigPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RenderingTypeConfigRepository
        extends BaseRepository<RenderingTypeConfig, RenderingTypeConfigPK> {
    @Query(
            "SELECT rt FROM RenderingTypeConfig rt where rt.renderingTypeConfigPK.institutionId= ?1"
                    + " and rt.renderingTypeConfigPK.cardRangeId = ?2 and"
                    + " rt.renderingTypeConfigPK.acsInterface= ?3 and rt.defaultRenderOption = ?4")
    Optional<RenderingTypeConfig> findByInstitutionIdCardRangeIdAcsInterfaceDefaultRenderOption(
            String institutionId,
            String cardRangeId,
            String acsInterface,
            String defaultRenderOption);

    @Query(
            "SELECT rt FROM RenderingTypeConfig rt where rt.renderingTypeConfigPK.institutionId= ?1"
                    + " and rt.renderingTypeConfigPK.cardRangeId = ?2 and"
                    + " rt.renderingTypeConfigPK.acsInterface= ?3")
    List<RenderingTypeConfig> findByInstitutionIdCardRangeIdAcsInterface(
            String institutionId, String cardRangeId, String acsInterface);
}
