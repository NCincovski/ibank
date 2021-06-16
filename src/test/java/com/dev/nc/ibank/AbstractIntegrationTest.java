package com.dev.nc.ibank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integration-test.properties")
public abstract class AbstractIntegrationTest {
    public static final String ROOT_PATH = "/api";
    public static final String TEST = "Integration Test";

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper objectMapper;

    protected static String getMvcResultContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    protected <T> T mapToObject(MvcResult result, Class<T> t) throws UnsupportedEncodingException, JsonProcessingException {
        assertThat(result).isNotNull();
        return objectMapper.readValue(getMvcResultContent(result), t);
    }
}
