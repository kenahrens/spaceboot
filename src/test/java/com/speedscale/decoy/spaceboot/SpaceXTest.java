package com.speedscale.decoy.spaceboot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;


@ActiveProfiles(value = "integration")
@SpringBootTest(classes = SpacebootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class SpaceXTest {
    @Autowired
    private MockMvc mockMvc;

    private WireMockServer wireMockServer;

    @BeforeEach
    void startRecording() {
        wireMockServer = new WireMockServer(
            WireMockConfiguration.options()
                .port(9081)
                .notifier(new ConsoleNotifier(true))
        );
        wireMockServer.start();
        System.out.println("Stub mapping size: " + wireMockServer.getStubMappings().size());
    }

    @AfterEach
    void stopRecording() {
        wireMockServer.stop();
    }


    @Test
    void getLaunches() throws Exception {
        mockMvc.perform(get("/space")
                .contentType("application/json")
            )
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
}
