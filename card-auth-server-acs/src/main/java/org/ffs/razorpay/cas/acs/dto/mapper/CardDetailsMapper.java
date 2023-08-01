package org.ffs.razorpay.cas.acs.dto.mapper;

import org.ffs.razorpay.cas.acs.dto.CardDetailDto;
import org.ffs.razorpay.cas.dao.model.CardDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The {@code CardDetailsMapper} is a MapStruct mapper interface that provides mapping methods to
 * convert between {@link CardDetailDto} and {@link CardDetail} objects.
 *
 * <p>The mapper uses the {@code @Mapper} annotation to generate the mapping implementations
 * automatically. The instance of this mapper can be obtained using the {@code INSTANCE} field
 * defined in this interface.
 *
 * @version 1.0.0
 * @since ACS 1.0.0
 * @author jaydeepRadadiya
 */
@Mapper
public interface CardDetailsMapper {

    /** An instance of the {@code CardDetailsMapper} interface to be used for mapping. */
    CardDetailsMapper INSTANCE = Mappers.getMapper(CardDetailsMapper.class);

    /**
     * Converts a {@link CardDetailDto} object to a {@link CardDetail} object.
     *
     * @param cardDetailDto The {@link CardDetailDto} object to convert.
     * @return The mapped {@link CardDetail} object.
     */
    @Mapping(target = "cardholder.name", source = "name")
    @Mapping(target = "cardholder.mobileNumber", source = "mobileNumber")
    @Mapping(target = "cardholder.emailId", source = "emailId")
    @Mapping(target = "cardholder.dob", source = "dob")
    CardDetail toCardDetailModel(CardDetailDto cardDetailDto);

    /**
     * Converts a {@link CardDetail} object to a {@link CardDetailDto} object.
     *
     * @param cardDetail The {@link CardDetail} object to convert.
     * @return The mapped {@link CardDetailDto} object.
     */
    @Mapping(target = "emailId", source = "cardholder.emailId")
    @Mapping(target = "dob", source = "cardholder.dob")
    @Mapping(target = "name", source = "cardholder.name")
    @Mapping(target = "mobileNumber", source = "cardholder.mobileNumber")
    CardDetailDto toCardDetailDto(CardDetail cardDetail);
}
