package org.freedomfinancestack.razorpay.cas.contract;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;

import lombok.Data;

@Data
public class MessageExtension implements Validatable {

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    @JsonProperty("criticalityIndicator")
    private String criticalityIndicator;

    @JsonProperty("data")
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
