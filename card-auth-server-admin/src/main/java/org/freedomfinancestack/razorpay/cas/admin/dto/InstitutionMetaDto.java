package org.freedomfinancestack.razorpay.cas.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionMetaDto {
    private byte[] logoData;

    private String logoFilename;

    private String logoFileType;
}
