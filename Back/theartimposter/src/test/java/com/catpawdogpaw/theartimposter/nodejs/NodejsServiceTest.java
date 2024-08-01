package com.catpawdogpaw.theartimposter.nodejs;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.catpawdogpaw.theartimposter.nodejs.entity.dto.STNDTO;
import com.catpawdogpaw.theartimposter.nodejs.service.NodejsService;

@SpringBootTest
public class NodejsServiceTest {

    @Autowired
    private NodejsService nodejsService;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        mockServer = MockRestServiceServer.createServer(restTemplate);

        // Injecting the RestTemplate with the mock server into the service
        nodejsService = new NodejsService();
    }

    @Test
    public void testSendToNode() {
        STNDTO stndto = nodejsService.createSTNDTO();

        mockServer.expect(MockRestRequestMatchers.requestTo("http://localhost:3000/receive-data"))
                  .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                  .andRespond(MockRestResponseCreators.withSuccess("Success", MediaType.APPLICATION_JSON));

        assertDoesNotThrow(() -> nodejsService.sendToNode(stndto));

        mockServer.verify();
    }
}
