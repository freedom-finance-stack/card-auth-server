# Card Auth Server ACS 

Freedom Finance Stack: [Contact Us](contact@freedomfinancestack.org) | 
License: [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

Card Auth Server ACS

# Challenge-Request-Controller
## Handles browser Challenge Request generating user's browser
This endpoint orchestrates the generation of a Challenge Request, a crucial step in the user authentication flow. When a request is made to this endpoint, it triggers the creation of challenges specifically tailored for the user's browser, enhancing the security of the authentication process.

`POST` `/v2/transaction/challenge/browser`
#### Generated server url: http://127.0.0.1:8080/v2/transaction/challenge/browser


<details >
  <summary >Request</summary>
    
| Query Params       | Type   | Description                                                                                                                                                                                             |
|--------------------|--------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| creq (required)    | string | The Challenge Request parameter is required for initiating the generation of the user's browser Challenge Request. It contains essential information needed to execute the authentication challenge.    |
| threeDSSessionData | string | The ThreeDS Session Data provides additional context for the authentication process. It may include session-related information to enhance the processing of the Challenge Request.                     |
</details>

<details>
  <summary>Responses</summary>

| StatusCode | Response Message                                   | Response Schema         | Response Type |
|------------|----------------------------------------------------|-------------------------|---------------|
| 200        | Request Successfully handled and validated         | html/text;charset=utf-8 | string        | 
| 400        | Bad Request or Request not according to Areq Schema | html/text;charset=utf-8 | string        |
| 500        | Server Exception Occurred during request handling  | html/text;charset=utf-8 | string        |

</details>

## Handles browser validation Challenge Request generating user's browser

This endpoint plays a crucial role in the authentication process by validating Challenge Requests generated for the user's browser. Upon receiving a request, it verifies the authenticity and correctness of the challenge, adding an additional layer of security to the user authentication flow.

`POST` `/v2/transaction/challenge/browser/validate`
#### Generated server url: http://127.0.0.1:8080/v2/transaction/challenge/browser/validate

<details>
  <summary >Request</summary>
<br>
<b>REQUEST BODY SCHEMA</b>: application/x-www-form-urlencoded;charset=UTF-8
<br>

| Query Params           | Type                                                                                         |
|------------------------|----------------------------------------------------------------------------------------------|
| threeDSServerTransID   | string                                                                                       |
| threeDSRequestorAppURL | string                                                                                       |
| acsTransID             | string                                                                                       |
| challengeWindowSize    | string                                                                                       |
| messageType            | string                                                                                       |
| messageVersion         | string                                                                                       |
| sdkCounterStoA         | string                                                                                       |
| sdkTransID             | string                                                                                       |
| challengeCancel        | string                                                                                       |
| challengeDataEntry     | string                                                                                       |
| challengeHTMLDataEntry | string                                                                                       |
| messageExtension       | Array [name,string, id, string, criticalityIndicator, boolean, data, object, valid, boolean] | 
| resendChallenge        | string                                                                                       |
| challengeNoEntry       | string                                                                                       | 
| whitelistingDataEntry  | string                                                                                       | 
| oobContinue            | string                                                                                       | 
| threeDSMessageType     | string <br> Enum: "AReq" "ARes" "CReq" "CRes" "Erro" "RReq" "RRes"                           | 

</details>

<details>
  <summary>Responses</summary>

| StatusCode | Response Message                                   | Response Schema         | Response Type |
|------------|----------------------------------------------------|-------------------------|---------------|
| 200        | Request Successfully handled and validated         | html/text;charset=utf-8 | string        | 
| 400        | Bad Request or Request not according to Areq Schema | html/text;charset=utf-8 | string        |
| 500        | Server Exception Occurred during request handling  | html/text;charset=utf-8 | string        |

</details>

## Handles App Based Challenge Request
This endpoint facilitates the initiation and handling of App-Based Challenge Requests, 
allowing users to trigger authentication challenges within the application. 
It ensures a secure and user-friendly experience, managing the flow of challenges for enhanced authentication and security measures.
`POST` `/v2/transaction/challenge/app`
#### Generated server url: http://127.0.0.1:8080/v2/transaction/challenge/app

<details >
  <summary >Request</summary>
<br>
<b>REQUEST BODY SCHEMA</b>: application/x-www-form-urlencoded;charset=UTF-8
<br>

| <b>REQUEST BODY SCHEMA</b>     | Type   |
|--------------------------------|--------|
| application/json;charset=UTF-8 | string |
</details>

<details>
  <summary>Responses</summary>

| StatusCode | Response Message                                    | Response Schema                | Response Type |
|------------|-----------------------------------------------------|--------------------------------|---------------|
| 200        | Request Successfully handled and validated          | application/jose;charset=UTF-8 | string        | 
| 400        | Bad Request or Request not according to Areq Schema | application/jose;charset=UTF-8 | string        |
| 500        | Server Exception Occurred during request handling   | application/jose;charset=UTF-8 | string        |

</details>

<details>
  <summary>Request samples</summary>

|Payload| Type     |
|-------|----------|
|application/jose;charset=utf-8| "string" |

</details>


# authentication-request-controller
## Handles Authentication Request generating from 3DS Server

`POST` `/v2/transaction/authentication
`
#### Generated server url: http://127.0.0.1:8080/v2/transaction/authentication
This endpoint plays a crucial role in the authentication flow, handling requests generated by the 3DS Server. 
It ensures the secure exchange of authentication data, facilitating a seamless and robust authentication process for enhanced security measures. 
The endpoint manages the validation and processing of authentication requests to guarantee a smooth user experience.
<details >
  <summary >Request</summary>
<br>
<b>REQUEST BODY SCHEMA</b>: application/json
<br>

| Query Params                            | Type                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
|-----------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| threeDSRequestorURL                     | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSCompInd                          | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSRequestorAuthenticationInd       | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSRequestorID                      | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSRequestorName                    | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSServerRefNumber                  | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSServerTransID                    | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSServerURL                        | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| acquirerBIN                             | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| acquirerMerchantID                      | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| browserAcceptHeader                     | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| browserJavaEnabled                      | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| browserLanguage                         | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| browserColorDepth                       | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| browserScreenHeight                     | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| browserScreenWidth                      | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| browserTZ                               | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| browserUserAgent                        | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| acctNumber                              | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| deviceChannel                           | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| deviceRenderOptions                     | object (DeviceRenderOptions) {sdkInterface:string, sdkUiType:Array of strings, valid:boolean, mandatoryValueAvailable:boolean}                                                                                                                                                                                                                                                                                                                                                                             |
| mcc                                     | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| merchantCountryCode                     | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| merchantName                            | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| messageCategory                         | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| messageType                             | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| messageVersion                          | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| notificationURL                         | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| purchaseAmount                          | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| purchaseCurrency                        | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| purchaseExponent                        | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| purchaseDate                            | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| sdkAppID                                | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| sdkEphemPubKey                          | object(EphemPubKey) {alg:string, kid:string, use:string, kty:string, crv:string, x:string, y:string, valid:boolean}                                                                                                                                                                                                                                                                                                                                                                                        |
| sdkMaxTimeout                           | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| sdkReferenceNumber                      | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| sdkTransID                              | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSRequestorAuthenticationInfo      | object (ThreeDSRequestorAuthenticationInfo){threeDSReqAuthMethod: string, threeDSReqAuthTimestamp: string, threeDSReqAuthData: string, valid: boolean}                                                                                                                                                                                                                                                                                                                                                     |
| threeDSRequestorChallengeInd            | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSRequestorPriorAuthenticationInfo | object (ThreeDSRequestorPriorAuthenticationInfo) {threeDSReqPriorAuthData:string, threeDSReqPriorAuthMethod:string, threeDSReqPriorAuthTimestamp:string, threeDSReqPriorRef:string, valid:boolean, mandatoryValueAvailable: boolean}                                                                                                                                                                                                                                                                       |
| addrMatch	                              | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| acctInfo                                | object (CardholderAccountInformation) {chAccAgeInd:string,chAccDate:string,chAccChangeInd:string ,chAccChange:string,chAccPwChangeInd:string ,chAccPwChange:string ,shipAddressUsageInd:string ,shipAddressUsage:string ,txnActivityDay:string ,txnActivityYear:string, provisionAttemptsDay:string ,nbPurchaseAccount:string ,suspiciousAccActivity:string ,shipNameIndicator:string ,paymentAccInd:string ,paymentAccAge:string, length:integer <int32>,empty:boolean ,valid:boolean ,dataValid:boolean} |
| acctID                                  | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| merchantRiskIndicator                   | object (ThreeDSMerchantFeilds){shipIndicator:string,deliveryTimeframe:string ,deliveryEmailAddress:string ,reorderItemsInd:string ,preOrderPurchaseInd:string ,preOrderDate:string ,giftCardAmount:string ,giftCardCurr:string,giftCardCount ,string:length:integer <int32>,valid:boolean}                                                                                                                                                                                                                 |
| threeDSServerOperatorID                 | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| acctType                                | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| broadInfo                               | object (BrodInfo) { data:string}                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| browserIP                               | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| cardExpiryDate                          | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| billAddrCity                            | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| billAddrCountry                         | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| billAddrLine1                           | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| billAddrLine2                           | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| billAddrLine3                           | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| billAddrPostCode                        | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| billAddrState                           | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| email                                   | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| homePhone                               | object (Phone) {cc:string,subscriber:string ,valid:boolean}                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| mobilePhone                             | object (Phone) {cc:string,subscriber:string ,valid:boolean }                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| cardholderName                          | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| shipAddrCity                            | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| shipAddrCountry                         | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| shipAddrLine1                           | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| shipAddrLine2                           | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| shipAddrLine3                           | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| shipAddrPostCode                        | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| shipAddrState                           | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| workPhone                               | object (Phone) {cc:string,subscriber:string ,valid:boolean }                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| deviceInfo                              | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| dsReferenceNumber                       | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| dsTransID                               | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| dsURL                                   | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| payTokenInd                             | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| purchaseInstalData                      | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| messageExtension                        | Array of objects (MessageExtension) Array [name:string ,id:string,criticalityIndicator:boolean,data:object,valid:boolean]                                                                                                                                                                                                                                                                                                                                                                                  |
| recurringExpiry                         | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| recurringFrequency                      | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| sdkEncData                              | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| transType                               | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSReqAuthMethodInd                 | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSRequestorDecMaxTime              | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSRequestorDecReqInd               | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| browserJavascriptEnabled                | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| payTokenSource                          | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| whiteListStatus                         | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| whiteListStatusSource                   | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| threeDSMessageType                      | string <br> Enum: "AReq" "ARes" "CReq" "CRes" "Erro" "RReq" "RRes"                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| transactionId                           | string                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |


</details>

<details>
  <summary>Responses</summary>

| StatusCode | Response Message                                    | Response Schema  | Response Type |
|------------|-----------------------------------------------------|------------------|---------------|
| 200        | Request Successfully handled and validated          | application/json | object        | 
| 400        | Bad Request or Request not according to Areq Schema | application/json | object        |
| 500        | Server Exception Occurred during request handling   | application/json | object        |

</details>

<details>
  <summary>Response sample</summary>
<ul>
<li> Payload</li>
<details>
  <summary>application/json</summary>

<code>
{
  "threeDSRequestorURL": "string",
  "threeDSCompInd": "string",
  "threeDSRequestorAuthenticationInd": "string",
  "threeDSRequestorID": "string",
  "threeDSRequestorName": "string",
  "threeDSServerRefNumber": "string",
  "threeDSServerTransID": "string",
  "threeDSServerURL": "string",
  "threeRIInd": "string",
  "acquirerBIN": "string",
  "acquirerMerchantID": "string",
  "browserAcceptHeader": "string",
  "browserJavaEnabled": "string",
  "browserLanguage": "string",
  "browserColorDepth": "string",
  "browserScreenHeight": "string",
  "browserScreenWidth": "string",
  "browserTZ": "string",
  "browserUserAgent": "string",
  "acctNumber": "string",
  "deviceChannel": "string",
  "deviceRenderOptions": {
    "sdkInterface": "string",
    "sdkUiType": [
      "string"
    ],
    "valid": true,
    "mandatoryValueAvailable": true
  },
  "mcc": "string",
  "merchantCountryCode": "string",
  "merchantName": "string",
  "messageCategory": "string",
  "messageType": "string",
  "messageVersion": "string",
  "notificationURL": "string",
  "purchaseAmount": "string",
  "purchaseCurrency": "string",
  "purchaseExponent": "string",
  "purchaseDate": "string",
  "sdkAppID": "string",
  "sdkEphemPubKey": {
    "alg": "string",
    "kid": "string",
    "use": "string",
    "kty": "string",
    "crv": "string",
    "x": "string",
    "y": "string",
    "valid": true
  },
  "sdkMaxTimeout": "string",
  "sdkReferenceNumber": "string",
  "sdkTransID": "string",
  "threeDSRequestorAuthenticationInfo": {
    "threeDSReqAuthMethod": "string",
    "threeDSReqAuthTimestamp": "string",
    "threeDSReqAuthData": "string",
    "valid": true
  },
  "threeDSRequestorChallengeInd": "string",
  "threeDSRequestorPriorAuthenticationInfo": {
    "threeDSReqPriorAuthData": "string",
    "threeDSReqPriorAuthMethod": "string",
    "threeDSReqPriorAuthTimestamp": "string",
    "threeDSReqPriorRef": "string",
    "valid": true,
    "mandatoryValueAvailable": true
  },
  "addrMatch": "string",
  "acctInfo": {
    "chAccAgeInd": "string",
    "chAccDate": "string",
    "chAccChangeInd": "string",
    "chAccChange": "string",
    "chAccPwChangeInd": "string",
    "chAccPwChange": "string",
    "shipAddressUsageInd": "string",
    "shipAddressUsage": "string",
    "txnActivityDay": "string",
    "txnActivityYear": "string",
    "provisionAttemptsDay": "string",
    "nbPurchaseAccount": "string",
    "suspiciousAccActivity": "string",
    "shipNameIndicator": "string",
    "paymentAccInd": "string",
    "paymentAccAge": "string",
    "length": 0,
    "empty": true,
    "valid": true,
    "dataValid": true
  },
  "acctID": "string",
  "merchantRiskIndicator": {
    "shipIndicator": "string",
    "deliveryTimeframe": "string",
    "deliveryEmailAddress": "string",
    "reorderItemsInd": "string",
    "preOrderPurchaseInd": "string",
    "preOrderDate": "string",
    "giftCardAmount": "string",
    "giftCardCurr": "string",
    "giftCardCount": "string",
    "length": 0,
    "valid": true
  },
  "threeDSServerOperatorID": "string",
  "acctType": "string",
  "broadInfo": {
    "data": "string"
  },
  "browserIP": "string",
  "cardExpiryDate": "string",
  "billAddrCity": "string",
  "billAddrCountry": "string",
  "billAddrLine1": "string",
  "billAddrLine2": "string",
  "billAddrLine3": "string",
  "billAddrPostCode": "string",
  "billAddrState": "string",
  "email": "string",
  "homePhone": {
    "cc": "string",
    "subscriber": "string",
    "valid": true
  },
  "mobilePhone": {
    "cc": "string",
    "subscriber": "string",
    "valid": true
  },
  "cardholderName": "string",
  "shipAddrCity": "string",
  "shipAddrCountry": "string",
  "shipAddrLine1": "string",
  "shipAddrLine2": "string",
  "shipAddrLine3": "string",
  "shipAddrPostCode": "string",
  "shipAddrState": "string",
  "workPhone": {
    "cc": "string",
    "subscriber": "string",
    "valid": true
  },
  "deviceInfo": "string",
  "dsReferenceNumber": "string",
  "dsTransID": "string",
  "dsURL": "string",
  "payTokenInd": "string",
  "purchaseInstalData": "string",
  "messageExtension": [
    {
      "name": "string",
      "id": "string",
      "criticalityIndicator": true,
      "data": {
        "property1": {},
        "property2": {}
      },
      "valid": true
    }
  ],
  "recurringExpiry": "string",
  "recurringFrequency": "string",
  "sdkEncData": "string",
  "transType": "string",
  "threeDSReqAuthMethodInd": "string",
  "threeDSRequestorDecMaxTime": "string",
  "threeDSRequestorDecReqInd": "string",
  "browserJavascriptEnabled": "string",
  "payTokenSource": "string",
  "whiteListStatus": "string",
  "whiteListStatusSource": "string",
  "threeDSMessageType": "AReq",
  "transactionId": "string"
}
</code>
</details>
</ul>
</details>

<details>
  <summary>Response sample</summary>
<ul>
<li>200</li>
<details>
<summary>application/json</summary>
<code>
{
  "threeDSServerTransID": "string",
  "acsReferenceNumber": "string",
  "acsTransID": "string",
  "dsReferenceNumber": "string",
  "dsTransID": "string",
  "messageType": "string",
  "messageVersion": "string",
  "sdkTransID": "string",
  "transStatus": "string",
  "cardholderInfo": "string",
  "acsChallengeMandated": "string",
  "acsOperatorID": "string",
  "acsRenderingType": {
    "acsInterface": "string",
    "acsUiTemplate": "string"
  },
  "acsSignedContent": "string",
  "acsURL": "string",
  "authenticationType": "string",
  "authenticationValue": "string",
  "broadInfo": "string",
  "eci": "string",
  "messageExtension": [
    {
      "name": "string",
      "id": "string",
      "criticalityIndicator": true,
      "data": {
        "property1": {},
        "property2": {}
      },
      "valid": true
    }
  ],
  "transStatusReason": "string",
  "acsDecConInd": "string",
  "whiteListStatus": "string",
  "whiteListStatusSource": "string",
  "threeDSMessageType": "AReq"
}
</code>
</details>

<li>400</li>
<li>500</li>
</ul>

</details>