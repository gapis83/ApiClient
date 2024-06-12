package org.s21.sato;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
public class CrptApi
{
    private static final String DEFAULT_STRING = "string";
    private static final String API_URL = "https://ismp.crpt.ru/api/v3/lk/documents/create";
    private final ReentrantLock lock = new ReentrantLock();
    private int requestCount = 0;
    private final int requestLimit;
    private final long period;
    private long startTime;


    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.requestLimit = requestLimit;
        this.period = timeUnit.toMillis(1);
        this.startTime = System.currentTimeMillis();
    }

    public int createDocument(Document document, String signature) {
        lock.lock();
        int countRequestsInMethod = 0;
        try {
            boolean isTimeRemaining = true;
            while (isTimeRemaining) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                long sleepTime = period - elapsedTime;
                if (sleepTime < 0) {
                    requestCount = 0;
                    startTime = System.currentTimeMillis();
                    sleepTime = period;
                }
                if (requestCount == requestLimit) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println(e);
                        return countRequestsInMethod;
                    }
                    requestCount = 0;
                    startTime = System.currentTimeMillis();
                    isTimeRemaining = false;
                }

                HttpURLConnection conn = null;
                try {
                    URL url = new URL(API_URL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Signature", signature);

                    JSONObject jsonDocument = document.documentToJSON();
                    String jsonString = jsonDocument.toString();
                    byte[] jsonBytes = jsonString.getBytes(StandardCharsets.UTF_8);

                    conn.getOutputStream().write(jsonBytes);
                    countRequestsInMethod++;
                    requestCount++;
                } catch (ProtocolException e) {
                    System.err.println(e);
                } catch (IOException e) {
                    System.err.println(e);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        } finally {
            lock.unlock();
        }
        return countRequestsInMethod;
    }

    public static class Document {
        private JSONObject description = new JSONObject();
        private JSONArray productsArray = new JSONArray();
        private String docId;
        private String docStatus;
        private String docType;
        private boolean importRequest;
        private String ownerInn;
        private String participantInn;
        private String producerInn;
        private String productionDate;
        private String productionType;
        private String regDate;
        private String regNumber;

        public Document() {
            this.description.put("participantInn", DEFAULT_STRING);
            this.docId = DEFAULT_STRING;
            this.docStatus = DEFAULT_STRING;
            this.docType = "LP_INTRODUCE_GOODS";
            this.importRequest = true;
            this.ownerInn = DEFAULT_STRING;
            this.participantInn = DEFAULT_STRING;
            this.producerInn = DEFAULT_STRING;
            this.productionDate = "2020-01-23";
            this.productionType = DEFAULT_STRING;
            this.regDate = "2020-01-23";
            this.regNumber = DEFAULT_STRING;
            Product product1 = new Product();
            productsArray.put(product1.toJSON());
        }

        private static class Product {
            private String certificateDocument;
            private String certificateDocumentDate;
            private String certificateDocumentNumber;
            private String ownerInn;
            private String producerInn;
            private String productionDate;
            private String tnvedCode;
            private String uitCode;
            private String uituCode;

            public Product() {
                this.certificateDocument = DEFAULT_STRING;
                this.certificateDocumentDate = "2020-01-23";
                this.certificateDocumentNumber = DEFAULT_STRING;
                this.ownerInn = DEFAULT_STRING;
                this.producerInn = DEFAULT_STRING;
                this.productionDate = "2020-01-23";
                this.tnvedCode = DEFAULT_STRING;
                this.uitCode = DEFAULT_STRING;
                this.uituCode = DEFAULT_STRING;
            }

            public JSONObject toJSON() {
                JSONObject productJson = new JSONObject();
                try {
                    productJson.put("certificate_document", certificateDocument);
                    productJson.put("certificate_document_date", certificateDocumentDate);
                    productJson.put("certificate_document_number", certificateDocumentNumber);
                    productJson.put("owner_inn", ownerInn);
                    productJson.put("producer_inn", producerInn);
                    productJson.put("production_date", productionDate);
                    productJson.put("tnved_code", tnvedCode);
                    productJson.put("uit_code", uitCode);
                    productJson.put("uitu_code", uituCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return productJson;
            }
        }
        public JSONObject documentToJSON() {
            JSONObject jsonDocument = new JSONObject();
            jsonDocument.put("description", description);
            jsonDocument.put("doc_id", docId);
            jsonDocument.put("doc_status", docStatus);
            jsonDocument.put("doc_type", docType);
            jsonDocument.put("importRequest", importRequest);
            jsonDocument.put("owner_inn", ownerInn);
            jsonDocument.put("participant_inn", participantInn);
            jsonDocument.put("producer_inn", producerInn);
            jsonDocument.put("production_date", productionDate);
            jsonDocument.put("production_type", productionType);
            jsonDocument.put("products", productsArray);
            jsonDocument.put("reg_date", regDate);
            jsonDocument.put("reg_number", regNumber);

            return jsonDocument;
        }
    }
}

