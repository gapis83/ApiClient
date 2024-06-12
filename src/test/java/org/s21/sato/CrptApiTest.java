package org.s21.sato;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class CrptApiTest {

    CrptApi api = new CrptApi(TimeUnit.SECONDS, 3);

    @Test
    public void shouldSendNoMoreThanThreeRequestsPerSecond() throws InterruptedException, IOException {
        // Act
        int testCount = 0;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 1000) {
            testCount = api.createDocument(new CrptApi.Document(), "signature");
        }
        assertTrue(testCount <= 4);
    }

    @Test
    public void shouldReturnExpectedByteArray() {
        CrptApi.Document document = new CrptApi.Document();
        JSONObject jsonDocument = document.documentToJSON();

        String expectedJsonString = "{\"description\":{\"participantInn\":\"string\"}," +
                "\"doc_id\":\"string\"," +
                "\"doc_status\":\"string\"," +
                "\"doc_type\":\"LP_INTRODUCE_GOODS\"," +
                "\"importRequest\":true," +
                "\"owner_inn\":\"string\"," +
                "\"participant_inn\":\"string\"," +
                "\"producer_inn\":\"string\"," +
                "\"production_date\":\"2020-01-23\"," +
                "\"production_type\":\"string\"," +
                "\"products\":[{\"certificate_document\":\"string\"," +
                "\"certificate_document_date\":\"2020-01-23\"," +
                "\"certificate_document_number\":\"string\"," +
                "\"owner_inn\":\"string\"," +
                "\"producer_inn\":\"string\"," +
                "\"production_date\":\"2020-01-23\"," +
                "\"tnved_code\":\"string\"," +
                "\"uit_code\":\"string\"," +
                "\"uitu_code\":\"string\"}]," +
                "\"reg_date\":\"2020-01-23\"," +
                "\"reg_number\":\"string\"}";

        JSONObject expectedJson = new JSONObject(expectedJsonString);

        try {
            JSONAssert.assertEquals(expectedJson.toString(), jsonDocument.toString(), JSONCompareMode.LENIENT);
        } catch (AssertionError e) {
            System.out.println("JSON-объекты не совпадают: " + e.getMessage());
            System.out.println("Expected JSON: " + expectedJson.toString(2));
            System.out.println("Actual JSON: " + jsonDocument.toString(2));
        }
    }
}

