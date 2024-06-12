# My_SelSup

This Java class provides functionality to work with the CRPT API. The class is thread-safe and supports limiting the number of requests to the API within a specific time interval. The limitation is specified in the constructor as the number of requests within a certain time period. For example:
```
public CrptApi(TimeUnit timeUnit, int requestLimit)
```

- timeUnit: Specifies the time interval, such as seconds, minutes, etc.
- requestLimit: A positive value that determines the maximum number of requests within this time interval.

When the limit is exceeded, the request should be blocked to avoid exceeding the maximum number of API requests, and execution should continue without throwing an exception when the limit on the number of API calls is not exceeded as a result of this call. Under no circumstances should the limit on the number of requests be exceeded for the method.

Method
The class implements a single method for creating a document to introduce goods produced in the Russian Federation into circulation. The document and signature must be passed to the method as a Java object and a string, respectively.

The method is called via HTTPS POST method to the following URL:
```
https://ismp.crpt.ru/api/v3/lk/documents/create
```
The request body is sent in JSON format as follows:
```
{
"description": {
"participantInn": "string"
},
"doc_id": "string",
"doc_status": "string",
"doc_type": "LP_INTRODUCE_GOODS",
"importRequest": true,
"owner_inn": "string",
"participant_inn": "string",
"producer_inn": "string",
"production_date": "2020-01-23",
"production_type": "string",
"products": [
{
"certificate_document": "string",
"certificate_document_date": "2020-01-23",
"certificate_document_number": "string",
"owner_inn": "string",
"producer_inn": "string",
"production_date": "2020-01-23",
"tnved_code": "string",
"uit_code": "string",
"uitu_code": "string"
}
],
"reg_date": "2020-01-23",
"reg_number": "string"
}
```