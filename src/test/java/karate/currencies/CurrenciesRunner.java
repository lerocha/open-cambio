package karate.currencies;

import com.intuit.karate.junit5.Karate;

class CurrenciesRunner {

    @Karate.Test
    Karate testCurrencies() {
        return Karate.run("currencies").relativeTo(getClass());
    }

}
