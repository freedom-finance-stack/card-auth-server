package org.freedomfinancestack.razorpay.cas.admin.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInstitutionRequestDto {

    private InstitutionDto institutionDto;

    private MultipartFile logo;

}
