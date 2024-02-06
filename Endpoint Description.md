# Card Auth Server ACS 

Freedom Finance Stack: [Contact Us](contact@freedomfinancestack.org) | 
License: [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

Card Auth Server ACS

# Challenge-Request-Controller
## Handles browser Challenge Request generating user's browser

`POST` `/v2/transaction/challenge/browser`
#### Generated server url: http://127.0.0.1:8080/v2/transaction/challenge/browser

[//]: # (//todo add description for the endpoint)
<details >
  <summary >Request</summary>
    
| Query Params       | Type   |
|--------------------|--------|
| creq (required)    | string |
| threeDSSessionData | string |
</details>

<details>
  <summary>Responses</summary>

| StatusCode | Response Message                                   | Response Schema         | Response Type |
|------------|----------------------------------------------------|-------------------------|---------------|
| 200        | Request Successfully handled and validated         | html/text;charset=utf-8 | string        | 
| 400        | Bad Request or Request not according to Areq Schema | html/text;charset=utf-8 | string        |
| 500        | Server Exception Occurred during request handling  | html/text;charset=utf-8 | string        |

</details>