package com.journal.Journal.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherApiResponse {

    @Data
    public static class Coord {
        private double lon;
        private double lat;
    }

    @Data
    public static class Main {
        private double temp;

        @JsonProperty("feels_like")
        private double feelsLike;
    }

    private Coord coord;
    private String base;
    private Main main;
    private int visibility;
    private int timezone;
    private int id;
    private String name;
}
