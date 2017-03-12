# txcamb.io

txcamb.io is a REST API for currency exchange rates from the [European Central Bank](http://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html).

Exchange rates are usually updated around 16:00 CET on business days.

## API Documentation

#### 1. Get all currencies
```
GET /v1/currencies
```
* The currency display name is in the language specified by the `Accept-Language` request header. If no language is specified, then the default is `en-US`.

#### 2. Get currency
```
GET /v1/currencies/{code}
```
* Currency code is in the [ISO 4217 format.](https://www.iso.org/iso-4217-currency-codes.html)

Example:
```
curl http://api.txcamb.io/v1/currencies/USD
```
```
{
  "displayName" : "US Dollar",
  "startDate" : null,
  "endDate" : null,
  "currencyCode" : "USD",
  "_links" : {
    "self": {
      "href": "http://api.txcamb.io/v1/currencies/USD"
    }
  }
}
```
* `startDate` and `endDate` indicates the date range of available exchange rates for this currency.

#### 3. Get currency exchange rates between dates
```
GET /v1/currencies/{code}/rates?startDate={start}&endDate={end}
```
* Exchange rates are based on the currency specified by `code`.

#### 4. Get currency exchange rates by date
```
GET /v1/currencies/{code}/rates/{date}
```
* Exchange rates are based on the currency specified by `code`.

Example:
```
curl http://api.txcamb.io/v1/currencies/USD/2017-03-10
```
```
{
    "date": "2017-03-10",
    "base": "USD",
    "rates": {
        "AUD": 1.328965,
        "BGN": 1.844051,
        "BRL": 3.176882,
        "CAD": 1.350368,
        "CHF": 1.012918,
        "CNY": 6.914577,
        "CZK": 25.477089,
        "DKK": 7.008392,
        "EUR": 0.942863,
        "GBP": 0.822648,
        "HKD": 7.766076,
        "HRK": 6.995098,
        "HUF": 294.427683,
        "IDR": 13375.231002,
        "ILS": 3.685273,
        "INR": 66.634453,
        "JPY": 115.425232,
        "KRW": 1156.213465,
        "MXN": 19.770036,
        "MYR": 4.453235,
        "NOK": 8.618235,
        "NZD": 1.447106,
        "PHP": 50.236659,
        "PLN": 4.07873,
        "RON": 4.289082,
        "RUB": 59.114087,
        "SEK": 9.030549,
        "SGD": 1.41816,
        "THB": 35.410146,
        "TRY": 3.752688,
        "USD": 1,
        "ZAR": 13.264285
    },
    "_links": {
        "self": {
            "href": "http://api.txcamb.io/v1/currencies/USD/rates/2017-03-10"
        }
    }
}
```

#### 5. Get latest currency exchange rates
```
GET /v1/currencies/{code}/rates/latest
```
* Exchange rates are based on the currency specified by `code`.