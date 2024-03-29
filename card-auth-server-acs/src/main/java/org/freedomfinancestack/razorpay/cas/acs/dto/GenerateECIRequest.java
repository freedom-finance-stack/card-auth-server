package org.freedomfinancestack.razorpay.cas.acs.dto;

import org.freedomfinancestack.razorpay.cas.contract.enums.MessageCategory;
import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;

import lombok.Data;

/**
 * The {@code GenerateECIRequest} class represents a request object for generating ECI (Electronic
 * Commerce Indicator).
 *
 * <p>This class contains required and optional fields to generate ECI and is annotated with Lombok
 * annotation {@code @Data}, which automatically generates data methods for this class.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Data
public class GenerateECIRequest {
    /** The transaction status for which ECI is to be generated. */
    TransactionStatus transactionStatus;

    /** The network associated with the transaction. */
    byte networkCode;

    /** The message category for the transaction. */
    MessageCategory messageCategory;

    /**
     * Optional field - The ThreeRIInd (3DS Requestor Authentication Indicator) to be set in the
     * request.
     */
    String threeRIInd;

    /**
     * Constructs a {@code GenerateECIRequest} with the required parameters.
     *
     * @param transactionStatus The transaction status for which ECI is to be generated.
     * @param networkCode The network associated with the transaction.
     * @param messageCategory The message category for the transaction.
     */
    public GenerateECIRequest(
            TransactionStatus transactionStatus,
            byte networkCode,
            MessageCategory messageCategory) {
        this.transactionStatus = transactionStatus;
        this.networkCode = networkCode;
        this.messageCategory = messageCategory;
    }

    /**
     * Sets the optional field ThreeRIInd in the request.
     *
     * @param threeRIInd The ThreeRIInd (3DS Requestor Authentication Indicator) to be set in the
     *     request.
     * @return The current {@code GenerateECIRequest} instance with ThreeRIInd set.
     */
    public GenerateECIRequest setThreeRIInd(String threeRIInd) {
        this.threeRIInd = threeRIInd;
        return this;
    }
}
