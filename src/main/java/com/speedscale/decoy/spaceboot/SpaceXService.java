package com.speedscale.decoy.spaceboot;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

@Service
public class SpaceXService {
    
    static Logger logger = LoggerFactory.getLogger(SpaceXService.class);

    // static final String NASA_URI = "https://api.spacexdata.com/v5/launches/latest";
    // static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private final String spacexBaseURL;
    private HttpTransport httpTransport;
    private HttpRequestFactory factory;

    public SpaceXService(@Value("${spacexBaseURL}") String spacexBaseURL) {
        this.spacexBaseURL = spacexBaseURL;
        this.httpTransport = new NetHttpTransport();
        this.factory = httpTransport.createRequestFactory();
    }

    public String ship() throws Exception {
                
        String shipURL = spacexBaseURL + "/v4/ships/5ea6ed2d080df4000697c901";
        logger.info("Calling " + shipURL);
        
        GenericUrl url = new GenericUrl(shipURL);
        HttpRequest req = factory.buildGetRequest(url);
        HttpResponse res = req.execute();
        InputStream is = res.getContent();
        String text = new BufferedReader(
            new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        return text;
    }

    public String launches() throws Exception {
        
        String launchesURL = spacexBaseURL + "/v5/launches/latest";
        logger.info("Calling " + launchesURL);
        
        GenericUrl url = new GenericUrl(launchesURL);
        HttpRequest req = factory.buildGetRequest(url);
        HttpResponse res = req.execute();
        InputStream is = res.getContent();
        String text = new BufferedReader(
            new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        return text;
    }
}
