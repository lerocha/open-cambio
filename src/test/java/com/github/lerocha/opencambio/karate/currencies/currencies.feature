Feature: Get Currencies API

  Background:
    * url url

  Scenario: get all currencies, then the first by currency code and its rates.
    Given path 'v1/currencies'
    When method get
    Then status 200
    And def currency = response[0]

    Given path 'v1/currencies', currency.code
    When method get
    Then status 200

    Given path 'v1/currencies', currency.code, 'rates'
    When method get
    Then status 200

    Given path 'v1/currencies', currency.code, 'rates/latest'
    When method get
    Then status 200

    Given path 'v1/currencies', currency.code, 'rates/2022-03-18'
    When method get
    Then status 200

  Scenario: get exchange rates for a specific date

    Given def date = '2022-03-18'
    And path 'v1/currencies/USD/rates', date
    When method get
    Then status 200
    And match response.date == date
    And match response.base == 'USD'
    And match response.rates contains { BRL: 5.067588, CAD: 1.263718, EUR: 0.908431, USD: 1.000000 }

  Scenario: get exchange rates for a specific date using invalid currency

    Given def date = '2022-03-18'
    And path 'v1/currencies/ZZZ/rates', date
    When method get
    Then status 400
    And match response contains { status: 400, title: 'Bad Request', detail: 'Invalid currency code'}
