package es.jastxz.micro_tracking.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IpInfoResponse {
    private String ip;
    private String city;
    private String region;
    private String country;
    @JsonProperty("country_name")
    private String countryName;
    private String org; // ISP/Empresa
    private String timezone;
}
