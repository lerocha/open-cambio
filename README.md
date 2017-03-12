# txcamb.io

txcamb.io is a REST API for currency exchange rates from the [European Central Bank](http://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html).

Exchange rates are usually updated around 16:00 CET on business days.

## API Documentation

#### Get all currencies
```
GET /v1/currencies
```
* The currency display name is in the language specified by the `Accept-Language` request header. If no language is specified, then the default is `en-US`.

#### Get currency
```
GET /v1/currencies/{code}
```
* Currency code is in the [ISO 4217 format.](https://www.iso.org/iso-4217-currency-codes.html)

#### Get currency exchange rates between dates
```
GET /v1/currencies/{code}/rates?startDate=2017-01-01&endDate=
```
* Exchange rates are based on the currency specified by `code`.

#### Get currency exchange rates by date
```
GET /v1/currencies/{code}/rates/{date}
```
* Exchange rates are based on the currency specified by `code`.

#### Get latest currency exchange rates
```
GET /v1/currencies/{code}/rates/latest
```
* Exchange rates are based on the currency specified by `code`.