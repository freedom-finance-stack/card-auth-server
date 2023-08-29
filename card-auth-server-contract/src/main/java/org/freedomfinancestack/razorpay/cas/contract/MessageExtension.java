package org.freedomfinancestack.razorpay.cas.contract;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;

import lombok.Data;

@Data
public class MessageExtension implements Validatable {

    private String name;
    private String id;
    private String criticalityIndicator;
    private Map<String, Object> data;

    public boolean isValid() {
        if (this.name != null && name.length() > 64) {
            return false;
        } else if (this.id != null && id.length() > 64) {
            return false;
        } else if (this.getData() != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String dataString = objectMapper.writeValueAsString(getData());
                JsonParser.parseString(dataString);
                return dataString.length() <= 8059;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
