package com.razorpay.threeds.dto;

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
    private String statusCode;
    private String statusDescription;

}
