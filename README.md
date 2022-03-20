# Open Cambio

Open Cambio is a REST API for currency exchange rates from the [European Central Bank](http://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html).

Exchange rates are usually updated around 16:00 CET on business days.

## API Documentation

#### 1. Get all currencies
```
GET /v1/currencies
```
* The currency display name is in the language specified by the `Accept-Language` request header. If no language is specified, then the default is `en-US`.

Example:
```
curl http://api.opencambio.org/v1/currencies
```

#### 2. Get currency
```
GET /v1/currencies/{code}
```
* Currency code is in the [ISO 4217 format.](https://www.iso.org/iso-4217-currency-codes.html)

Example:
```
curl http://api.opencambio.org/v1/currencies/CAD
```
```
{
    "code": "CAD",
    "displayName": "Canadian Dollar",
    "startDate": "1999-01-04",
    "endDate": "2022-01-28"
}
```
* `startDate` and `endDate` indicates the date range of available exchange rates for this currency.

#### 3. Get currency exchange rates between dates
```
GET /v1/currencies/{code}/rates?start={start}&end={end}
```
* Exchange rates are based on the currency specified by `code`.
* Exchange rates are available for business days only.
* The response is paginated and each page will contain exchange rates for the same month.

Example:
```
curl http://api.opencambio.org/v1/currencies/USD/rates?start=2022-01-01&end=2022-12-31
```
```
{
    "rates": [
        {
            "date": "2021-01-04",
            "base": "USD",
            "rates": {
                "AUD": 1.295381,
                "BGN": 1.590599,
                "BRL": 5.143218,
                "CAD": 1.270414,
                "CHF": 0.879230,
                "CNY": 6.464217,
                "CZK": 21.259760,
                "DKK": 6.049041,
                "EUR": 0.813273,
                "GBP": 0.733247,
                "HKD": 7.752928,
                "HRK": 6.145495,
                "HUF": 293.851660,
                "IDR": 13876.602148,
                "ILS": 3.206734,
                "INR": 73.022935,
                "ISK": 126.951855,
                "JPY": 102.976578,
                "KRW": 1083.303514,
                "MXN": 19.765046,
                "MYR": 4.006507,
                "NOK": 8.493820,
                "NZD": 1.387850,
                "PHP": 48.030254,
                "PLN": 3.698358,
                "RON": 3.961695,
                "RUB": 73.472675,
                "SEK": 8.205514,
                "SGD": 1.317339,
                "THB": 29.869877,
                "TRY": 7.366542,
                "USD": 1.000000,
                "ZAR": 14.574984
            }
        },
        
        .
        .
        .
        
        {
            "date": "2021-01-29",
            "base": "USD",
            "rates": {
                "AUD": 1.303478,
                "BGN": 1.611569,
                "BRL": 5.485169,
                "CAD": 1.278840,
                "CHF": 0.889750,
                "CNY": 6.431032,
                "CZK": 21.440343,
                "DKK": 6.128049,
                "EUR": 0.823995,
                "GBP": 0.728272,
                "HKD": 7.753214,
                "HRK": 6.234180,
                "HUF": 295.311471,
                "IDR": 14003.098221,
                "ILS": 3.280653,
                "INR": 72.867502,
                "ISK": 128.625577,
                "JPY": 104.688530,
                "KRW": 1116.496375,
                "MXN": 20.222232,
                "MYR": 4.042519,
                "NOK": 8.522578,
                "NZD": 1.389503,
                "PHP": 48.066085,
                "PLN": 3.733026,
                "RON": 4.016975,
                "RUB": 75.723385,
                "SEK": 8.331411,
                "SGD": 1.328362,
                "THB": 29.900297,
                "TRY": 7.314766,
                "USD": 1.000000,
                "ZAR": 15.083883
            }
        }
    ],
    "pagination": {
        "offset": 0,
        "total": 12
    },
    "_links": {
        "self": {
            "href": "http://localhost:5000/v1/currencies/USD/rates?start=2021-01-01&end=2021-12-31{&offset}",
            "templated": true
        },
        "first": {
            "href": "http://localhost:5000/v1/currencies/USD/rates?start=2021-01-01&end=2021-12-31&offset=0"
        },
        "last": {
            "href": "http://localhost:5000/v1/currencies/USD/rates?start=2021-01-01&end=2021-12-31&offset=11"
        },
        "next": {
            "href": "http://localhost:5000/v1/currencies/USD/rates?start=2021-01-01&end=2021-12-31&offset=1"
        }
    }
}
```

#### 4. Get currency exchange rates by date
```
GET /v1/currencies/{code}/rates/{date}
```
* Exchange rates are based on the currency specified by `code`.
* If there are no exchange rates for the requested date, then returns the exchange rates for the last business day before. 

Example:
```
curl http://api.opencambio.org/v1/currencies/USD/rates/2022-01-28
```
```
{
    "date": "2022-01-28",
    "base": "USD",
    "rates": {
        "AUD": 1.433920,
        "BGN": 1.755971,
        "BRL": 5.400162,
        "CAD": 1.278417,
        "CHF": 0.931766,
        "CNY": 6.361735,
        "CZK": 21.945592,
        "DKK": 6.682708,
        "EUR": 0.897828,
        "GBP": 0.746795,
        "HKD": 7.794039,
        "HRK": 6.759742,
        "HUF": 321.799246,
        "IDR": 14408.116359,
        "ILS": 3.204974,
        "INR": 75.059706,
        "ISK": 129.287126,
        "JPY": 115.532412,
        "KRW": 1211.590950,
        "MXN": 20.816485,
        "MYR": 4.189981,
        "NOK": 9.001617,
        "NZD": 1.529090,
        "PHP": 51.201293,
        "PLN": 4.108009,
        "RON": 4.440923,
        "RUB": 77.761986,
        "SEK": 9.473874,
        "SGD": 1.356528,
        "THB": 33.425211,
        "TRY": 13.595260,
        "USD": 1.000000,
        "ZAR": 15.608189
    }
}
```

#### 5. Get latest currency exchange rates
```
GET /v1/currencies/{code}/rates/latest
```
* Exchange rates are based on the currency specified by `code`.

Example:
```
curl http://api.opencambio.org/v1/currencies/USD/rates/latest
```

## Build and Run

```
./gradlew build
```

## Running tests

### Integration tests
```
./gradlew test --tests "*integration.*"
```

### Karate tests
```
./gradlew test --tests "*karate.*" -Dkarate.env=local
```

```
./gradlew test --tests "*karate.*" -Dkarate.env=dev
```

### Running locally

```
java -jar build/libs/open-cambio-0.0.2-SNAPSHOT.jar
```

### Running with Docker

Create the docker image:
```
docker build -t open-cambio .
````

Run docker container:
```
docker run --name open-cambio -dp 5000:5000 open-cambio
```

### Testing

```
curl http://localhost:5000/v1/currencies/CAD
```