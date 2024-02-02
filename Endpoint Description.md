# Card Auth Server ACS (1.0.0)

Freedom Finance Stack: [Contact Us](contact@freedomfinancestack.org) | 
License: [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

Card Auth Server ACS

# Challenge-Request-Controller
## Handles browser Challenge Request generating user's browser


<button style="background-color: #00b300; border-radius: 20px 20px 20px 20px; padding: 5px 10px; color: white; ">POST</button> 
`/v2/transaction/challenge/browser`
#### Generated server url: http://127.0.0.1:8080/v2/transaction/challenge/browser

<div style="border: 10px red;padding:10px">
<details >
  <summary >Request</summary>
    <br>
    <text style="font-weight: bold">QueryParameters
      <ul>
        <li>creq <text style="font-weight: normal; color:red; margin-right: 80px"> (required)</text>string</li>
        <li>threeDSSessionData <text style="margin-left: 36px">string</text></li>
      </ul>
</details>
</div>

<details>
  <summary>Responses</summary>
 
  <ul>
    <li><textarea style="background-color: lightgreen;font-weight: bold">200  Request Successfully handled and validated </textarea> </li>
    <li>
        <text style="background-color: #800000; font-weight: bold;padding: 5px; border-radius:10px 10px 10px 10px ">
            <text style="font-weight: bold">400 </text> 
            Bad Request or Request not according to Areq Schema
        </text>
    </li>
    <li><text font-weight="bold">500 </text> Server Exception Occurred during request handling</li>
  </ul>
</details>