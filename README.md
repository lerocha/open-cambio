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
```json
{
    "rates": [
        {
            "date": "2022-12-30",
            "base": "USD",
            "rates": {
                "AUD": 1.471311,
                "BGN": 1.833678,
                "BRL": 5.286518,
                "CAD": 1.353835,
                "CHF": 0.923214,
                "CNY": 6.898744,
                "CZK": 22.610164,
                "DKK": 6.972155,
                "EUR": 0.937559,
                "GBP": 0.831549,
                "HKD": 7.797019,
                "HRK": 7.065911,
                "HUF": 375.839115,
                "IDR": 15488.299269,
                "ILS": 3.520908,
                "INR": 82.665480,
                "ISK": 142.040128,
                "JPY": 131.876993,
                "KRW": 1260.163136,
                "MXN": 19.553723,
                "MYR": 4.405026,
                "NOK": 9.857304,
                "NZD": 1.574911,
                "PHP": 55.615976,
                "PLN": 4.388525,
                "RON": 4.640447,
                "SEK": 10.427340,
                "SGD": 1.340709,
                "THB": 34.534971,
                "TRY": 18.718264,
                "ZAR": 16.968499
            }
        },
        
        {
            "date": "2022-12-01",
            "base": "USD",
            "rates": {
                "AUD": 1.470921,
                "BGN": 1.870863,
                "BRL": 5.214081,
                "CAD": 1.344845,
                "CHF": 0.943945,
                "CNY": 7.075283,
                "CZK": 23.303042,
                "DKK": 7.114311,
                "EUR": 0.956572,
                "GBP": 0.819926,
                "HKD": 7.783720,
                "HRK": 7.222116,
                "HUF": 395.542377,
                "IDR": 15459.001340,
                "ILS": 3.409413,
                "INR": 81.244500,
                "ISK": 142.242204,
                "JPY": 136.292329,
                "KRW": 1304.581979,
                "MXN": 19.272241,
                "MYR": 4.405013,
                "NOK": 9.804382,
                "NZD": 1.573178,
                "PHP": 56.239717,
                "PLN": 4.495696,
                "RON": 4.716186,
                "SEK": 10.425101,
                "SGD": 1.357854,
                "THB": 34.939736,
                "TRY": 18.631912,
                "ZAR": 17.734169
            }
        }
    ],
    "pagination": {
        "offset": 0,
        "total": 12
    },
    "_links": {
        "self": {
            "href": "http://api.opencambio.org/v1/currencies/USD/rates?start=2022-01-01&end=2022-12-31{&offset}",
            "templated": true
        },
        "first": {
            "href": "http://api.opencambio.org/v1/currencies/USD/rates?start=2022-01-01&end=2022-12-31&offset=0"
        },
        "last": {
            "href": "http://api.opencambio.org/v1/currencies/USD/rates?start=2022-01-01&end=2022-12-31&offset=11"
        },
        "next": {
            "href": "http://api.opencambio.org/v1/currencies/USD/rates?start=2022-01-01&end=2022-12-31&offset=1"
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
```json
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