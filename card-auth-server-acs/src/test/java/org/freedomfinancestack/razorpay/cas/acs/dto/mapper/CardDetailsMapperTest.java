package org.freedomfinancestack.razorpay.cas.acs.dto.mapper;

import org.freedomfinancestack.razorpay.cas.acs.dto.CardDetailDto;
import org.freedomfinancestack.razorpay.cas.dao.model.CardDetail;
import org.freedomfinancestack.razorpay.cas.dao.model.Cardholder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardDetailsMapperTest {

    private final CardDetailsMapper cardDetailsMapper = new CardDetailsMapperImpl();

    @Test
    void testToCardDetailModel() {
        // Arrange
        CardDetailDto cardDetailDto =
                CardDetailDto.builder()
                        .name("John Doe")
                        .mobileNumber("1234567890")
                        .emailId("john.doe@example.com")
                        .dob("1990-01-01")
                        .cardNumber("1234567890123456")
                        .cardExpiry("12/25")
                        .blocked(false)
                        .institutionId("123")
                        .build();

        // Act
        CardDetail cardDetail = cardDetailsMapper.toCardDetailModel(cardDetailDto);

        // Assert
        assertNotNull(cardDetail);
        assertNotNull(cardDetail.getCardholder());
        assertEquals(cardDetailDto.getName(), cardDetail.getCardholder().getName());
        assertEquals(cardDetailDto.getMobileNumber(), cardDetail.getCardholder().getMobileNumber());
        assertEquals(cardDetailDto.getEmailId(), cardDetail.getCardholder().getEmailId());
        assertEquals(cardDetailDto.getDob(), cardDetail.getCardholder().getDob());
        assertEquals(cardDetailDto.getCardNumber(), cardDetail.getCardNumber());
        assertEquals(cardDetailDto.getCardExpiry(), cardDetail.getCardExpiry());
        assertEquals(cardDetailDto.isBlocked(), cardDetail.isBlocked());
        assertEquals(cardDetailDto.getInstitutionId(), cardDetail.getInstitutionId());
    }

    @Test
    void testToCardDetailDtoNull() {
        assertNull(cardDetailsMapper.toCardDetailDto(null));
        assertNull(cardDetailsMapper.toCardDetailModel(null));
    }

    @Test
    void testToCardDetailDto() {
        // Arrange
        CardDetail cardDetail =
                CardDetail.builder()
                        .cardholder(
                                Cardholder.builder()
                                        .name("John Doe")
                                        .mobileNumber("1234567890")
                                        .emailId("john.doe@example.com")
                                        .dob("1990-01-01")
                                        .build())
                        .cardNumber("1234567890123456")
                        .cardExpiry("12/25")
                        .blocked(false)
                        .institutionId("123")
                        .build();

        // Act
        CardDetailDto cardDetailDto = cardDetailsMapper.toCardDetailDto(cardDetail);

        // Assert
        assertNotNull(cardDetailDto);
        assertEquals(cardDetail.getCardholder().getName(), cardDetailDto.getName());
        assertEquals(cardDetail.getCardholder().getMobileNumber(), cardDetailDto.getMobileNumber());
        assertEquals(cardDetail.getCardholder().getEmailId(), cardDetailDto.getEmailId());
        assertEquals(cardDetail.getCardholder().getDob(), cardDetailDto.getDob());
        assertEquals(cardDetail.getCardNumber(), cardDetailDto.getCardNumber());
        assertEquals(cardDetail.getCardExpiry(), cardDetailDto.getCardExpiry());
        assertEquals(cardDetail.isBlocked(), cardDetailDto.isBlocked());
        assertEquals(cardDetail.getInstitutionId(), cardDetailDto.getInstitutionId());
    }
}
