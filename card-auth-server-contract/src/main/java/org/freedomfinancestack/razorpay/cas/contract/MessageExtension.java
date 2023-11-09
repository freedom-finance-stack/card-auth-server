package org.freedomfinancestack.razorpay.cas.contract;

import java.util.Map;

import org.freedomfinancestack.extensions.validation.validator.Validatable;
import org.freedomfinancestack.razorpay.cas.contract.utils.Util;

import lombok.Data;

@Data
public class MessageExtension implements Validatable {

    private String name;
    private String id;
    private boolean criticalityIndicator;
    private Map<String, Object> data;

    public boolean isValid() {
        if (this.name != null && name.length() > 64) {
            return false;
        } else if (this.id != null && id.length() > 64) {
            return false;
        } else if (this.getData() != null) {
            try {
                String dataString = Util.gson.toJson(data);
                return dataString.length() <= 8059;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
