package org.ffs.razorpay.cas.dao.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    CREATED("U", "Created"),
    SUCCESS("Y", "Authentication Successful"),
    FAILED("N", "Not Authenticated"),
    UNABLE_TO_AUTHENTICATE("U", "Unable to Authentication"),
    ATTEMPT("A", "Attempt"),
    CHALLENGE_REQUIRED("C", "Challenge Required"),
    CHALLENGE_REQUIRED_DECOUPLED("D", "Challenge Required; Decoupled Authentication confirmed"),
    REJECTED("R", "Authentication Rejected"),
    INFORMATIONAL("I", "Informational Only");

    private final String status;
    private final String description;

    TransactionStatus(String status, String description) {
        this.status = status;
        this.description = description;
    }
}
