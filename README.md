# Trade_Assignment

Write a Java program with all the JUNIT cases. TDD approach will be preferred. Time to complete the
below assignment is 2 to 3 hrs.

Problem Statement
There is a scenario where thousands of trades are flowing into one store, assume any way of
transmission of trades. We need to create a one trade store, which stores the trade in the following
order

Trade Id  Version  Counter-Party Id  Book-Id  Maturity Date  Created Date  Expired

   T1        1          CP-1           B1      2020-05-20     <today date>   N
   T2        2          CP-2           B1      2021-05-20     <today date>   N
   T2        1          CP-1           B1      2021-05-20      2015-03-14    N
   T3        3          CP-3           B2      2014-05-20     <today date    Y

There are couples of validation, we need to provide in the above assignment
1. During transmission if the lower version is being received by the store it will reject the trade and
throw an exception. If the version is same it will override the existing record.
2. Store should not allow the trade which has less maturity date then today date.
3. Store should automatically update the expire flag if in a store the trade crosses the maturity
date.
===============================================================================================
1. Scenario - Adding trade in DB by passing valid data.
     POST request - Import below cURL in postman and hit the api, will get 200 sucesss response.
                                                                     
curl --location --request POST 'http://localhost:8080/trade' \
--header 'Content-Type: application/json' \
--data-raw '{
    "tradeId": "T1",
    "version": 1,
    "counterPartyId": "CP-1",
    "bookId": "B1",
    "maturityDate": "2022-05-20",
    "expiredFlag": "N"
}'
                                                                                                                                          
2. Scenario - Getting all trades detais present in DB.   
     GET request - Import below cURL in postman and hit the api, will get all trades details in response body.  
                                                                  
curl --location --request GET 'http://localhost:8080/trade' \
--data-raw ''
                                                                    
Response -
         [
    {
        "tradeId": "T1",
        "version": 1,
        "counterPartyId": "CP-1",
        "bookId": "B1",
        "maturityDate": "2022-05-20",
        "createdDate": "2022-01-23",
        "expiredFlag": "N"
    },
    {
        "tradeId": "T2",
        "version": 2,
        "counterPartyId": "CP-2",
        "bookId": "B1",
        "maturityDate": "2022-05-20",
        "createdDate": "2022-01-23",
        "expiredFlag": "N"
    }
]                                                                     
                                                                     
                                                                     
                                                                     
3. Scenario - Getting error message when store should not allow the trade which has less maturity date then today date. 
      POST request - Import below cURL in postman and hit the api, will get ERROR message in response body. 
                                                                     
curl --location --request POST 'http://localhost:8080/trade' \
--header 'Content-Type: application/json' \
--data-raw '{
    "tradeId": "T1",
    "version": 1,
    "counterPartyId": "CP-1",
    "bookId": "B1",
    "maturityDate": "2021-05-20",
    "expiredFlag": "N"
}'                                                                     
        
Response - 
         {
    "_embedded": {
        "errors": [
            {
                "message": "Invalid Trade: T1 , 2021-05-20  MaturityDate should be greater then CurrentDate.",
                "logref": "T1 , 2021-05-20  MaturityDate should be greater then CurrentDate."
            }
        ]
    }
} 
                                                                                                                                          
4. Scenario -  Getting error message when During transmission if the lower version is being received by the store it will reject the trade and
               throw an exception. If the version is same it will override the existing record.  
   POST request - Import below cURL in postman and hit the api, will get ERROR message in response body. 
                                                                     
curl --location --request POST 'http://localhost:8080/trade' \
--header 'Content-Type: application/json' \
--data-raw '{
    "tradeId": "T2",
    "version": 1,
    "counterPartyId": "CP-2",
    "bookId": "B1",
    "maturityDate": "2022-05-20",
    "expiredFlag": "N"
}'
                                                                     
Response -                                                                      
         {
    "_embedded": {
        "errors": [
            {
                "message": "Invalid Trade: T2 , Version=1, Same tradeId should have same or greater version.",
                "logref": "T2 , Version=1, Same tradeId should have same or greater version."
            }
        ]
    }
}                                                            
                                                                     
                                                                     
                                                                     
                                                                     
                                                                     
