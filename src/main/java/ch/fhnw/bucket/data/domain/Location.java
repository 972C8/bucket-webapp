package ch.fhnw.bucket.data.domain;

import okhttp3.OkHttpClient;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.net.URLEncoder;

/**
 * Takes a location in String format and uses the Google Maps API to geocode the location into latitude (lat)
 * and longitude (lng).
 */
@Entity
public class Location {

    private final static String googleMapsUrl = "";

    @Id
    @GeneratedValue
    private Long id;

    //The location provided in text format.
    //Used to generate the location of lat and lng.
    private String textLocation;

    //Latitude of location
    private String lat;

    //Longitude of location
    private String lng;

    //The generated url to display
    private String generatedUrl;

    private String getGeocode(String textLocation) {
            /*OkHttpClient client = new OkHttpClient();
            String encodedAddress = URLEncoder.encode(textLocation, "UTF-8");
            Request request = new Request.Builder()
                    .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?language=en&address=" + encodedAddress)
                    .get()
                    .addHeader("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", {your - api - key - here}/*  Use your API Key here)
                  /*  .build();
            ResponseBody responseBody = client.newCall(request).execute().body();
            return responseBody.string();*/
        return null;
        }
}
