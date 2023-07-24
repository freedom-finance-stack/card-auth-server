package com.razorpay.ffs.cas.acs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDetailResponse {

    private CardDetailDto cardDetailDto;
    private boolean isSuccess;
    // these will be used in case of API fetch
    private String statusCode;
    private String statusDescription;
}
