package com.krogerqa.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class ApiUtils {
    /**
     * @param userName   null if authorization not required
     * @param password   null if authorization not required
     * @param requestUrl request url including baseurl
     * @param headers    send as new Hashmap() if no headers present
     * @param queryParam send as new Hashmap() if no query params present
     * @return Response from the request url
     */
    public Response getApi(String userName, String password, String payload, String requestUrl, Map<String, String> headers,
                           Map<String, String> queryParam) {
        if (payload == null) {
            payload = "";
        }
        if (userName == null || password == null) {
            return RestAssured.given().relaxedHTTPSValidation()
                    .contentType(ContentType.JSON).queryParams(queryParam).headers(headers).body(payload)
                    .request(Method.GET, requestUrl).andReturn();
        } else {
            return RestAssured.given().relaxedHTTPSValidation().auth().preemptive().basic(userName, password)
                    .contentType(ContentType.JSON).queryParams(queryParam).headers(headers).body(payload)
                    .request(Method.GET, requestUrl).andReturn();
        }
    }

    public Response postApiWithXmlPayload(String userName, String password, String payload, String requestUrl,
                                          Map<String, String> headers, Map<String, String> queryParams) {
        if (userName == null || password == null) {
            return RestAssured.given()
                    .relaxedHTTPSValidation()
                    .contentType("application/xml")
                    .headers(headers)
                    .queryParams(queryParams)
                    .body(payload)
                    .request(Method.POST, requestUrl)
                    .andReturn();
        } else {
            return RestAssured.given()
                    .relaxedHTTPSValidation()
                    .auth().preemptive().basic(userName, password)
                    .contentType("application/xml")
                    .headers(headers)
                    .queryParams(queryParams)
                    .body(payload)
                    .request(Method.POST, requestUrl)
                    .andReturn();
        }
    }

    /**
     * @param userName    null if authorization not required
     * @param password    null if authorization not required
     * @param payload     input payload as String
     * @param requestUrl  request url including baseurl
     * @param headers     send as new Hashmap() if no headers present
     * @param queryParams send as new Hashmap() if no query params present
     * @return Response from the request url
     */
    public Response postPayload(String userName, String password, String payload, String requestUrl,
                                Map<String, String> headers, Map<String, String> queryParams) {
        if (userName == null || password == null) {
            return RestAssured.given().relaxedHTTPSValidation()
                    .contentType(ContentType.JSON).headers(headers).queryParams(queryParams)
                    .body(payload).request(Method.POST, requestUrl).andReturn();
        } else {
            return RestAssured.given().relaxedHTTPSValidation().auth().preemptive().basic(userName, password)
                    .contentType(ContentType.JSON).headers(headers).queryParams(queryParams)
                    .body(payload).request(Method.POST, requestUrl).andReturn();
        }
    }

    public Response deletePayload(String userName, String password, String requestUrl, Map<String, String> headers, Map<String, String> queryParams) {
        if (userName == null || password == null) {
            return RestAssured.given().relaxedHTTPSValidation()
                    .contentType(ContentType.JSON).headers(headers).queryParams(queryParams)
                    .request(Method.DELETE, requestUrl).andReturn();
        } else {
            return RestAssured.given().relaxedHTTPSValidation().auth().preemptive().basic(userName, password)
                    .contentType(ContentType.JSON).headers(headers).queryParams(queryParams)
                    .request(Method.DELETE, requestUrl).andReturn();
        }
    }

    public Response postPayloadForFormUrlencoded(String userName, String password, HashMap<String, String> formParam, String requestUrl) {
        if (userName == null || password == null) {
            return RestAssured.given().relaxedHTTPSValidation().formParams(formParam).request(Method.POST, requestUrl).andReturn();
        } else {
            return RestAssured.given().relaxedHTTPSValidation().auth().preemptive().basic(userName, password).formParams(formParam).request(Method.POST, requestUrl).andReturn();
        }
    }

    public Response postPayloadForFormUrlencodedWithHeaders(HashMap<String, String> formParam, HashMap<String, String> headers, String requestUrl) {
        return RestAssured.given().relaxedHTTPSValidation().headers(headers).contentType(ContentType.URLENC).formParams(formParam).request(Method.POST, requestUrl).andReturn();
    }

    public Response postPayloadWithBearerToken(String payload, String requestUrl, Map<String, String> headers, Map<String, String> queryParams, String bearerToken) {
        return RestAssured.given().relaxedHTTPSValidation().auth().oauth2(bearerToken)
                .contentType(ContentType.JSON).headers(headers).queryParams(queryParams)
                .body(payload).request(Method.POST, requestUrl).andReturn();
    }

    public Response getApiWithBearerToken(String requestUrl, Map<String, String> headers, Map<String, String> queryParams, String bearerToken) {
        return RestAssured.given().relaxedHTTPSValidation().auth().oauth2(bearerToken)
                .contentType(ContentType.JSON).headers(headers).queryParams(queryParams)
                .request(Method.GET, requestUrl).andReturn();
    }

    public Response putPayload(String userName, String password, String payload, String requestUrl,
                               Map<String, String> headers, Map<String, String> queryParams) {
        if (userName == null || password == null) {
            return RestAssured.given().relaxedHTTPSValidation()
                    .contentType(ContentType.JSON).headers(headers).queryParams(queryParams)
                    .body(payload).request(Method.PUT, requestUrl).andReturn();
        } else {
            return RestAssured.given().relaxedHTTPSValidation().auth().preemptive().basic(userName, password)
                    .contentType(ContentType.JSON).headers(headers).queryParams(queryParams)
                    .body(payload).request(Method.PUT, requestUrl).andReturn();
        }
    }
    /*
     * @param userName    null if authorization not required
     * @param password    null if authorization not required
     * @param payload     input payload as String
     * @param requestUrl  request url including baseurl
     * @param headers     send as new Hashmap() if no headers present
     * @param queryParams send as new Hashmap() if no query params present
     * @return Response from the request url
     */

    /**
     * Creates string for API payload from POJO class object
     *
     * @param javaObject POJO class object
     */
    public String convertObjectToString(Object javaObject) {
        String payload;
        try {
            payload = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(javaObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return payload;
    }
}
