package com.github.lerocha.opencambio.tools;

import com.github.lerocha.opencambio.entity.Currency;
import com.github.lerocha.opencambio.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Generates src/main/resources/db/migration/V2__seed_exchange_rates.sql from the ECB API.
 *
 * Run with:
 *   ./gradlew test --tests "*.SeedDataGenerator" -Dgenerate.seed=true
 *
 * Commit the output file and re-run whenever a new Flyway seed version is needed.
 */
@EnabledIfSystemProperty(named = "generate.seed", matches = "true")
@SpringBootTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class SeedDataGenerator {

    private static final String OUTPUT_FILE = "src/main/resources/db/migration/V2__seed_exchange_rates.sql";
    private static final int BATCH_SIZE = 500;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void generate() throws Exception {
        List<Currency> currencies = currencyRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Currency::getCode))
                .toList();

        List<Map<String, Object>> rates = jdbcTemplate.queryForList(
                "SELECT currency_code, exchange_date, exchange_rate FROM exchange_rate ORDER BY exchange_date, currency_code"
        );

        try (PrintWriter out = new PrintWriter(new FileWriter(OUTPUT_FILE))) {
            out.printf("-- Seed generated on %s; totalRates=%d%n%n", LocalDate.now(), rates.size());

            out.println("INSERT INTO currency (code, display_name, start_date, end_date, created_date, last_modified_date) VALUES");
            List<String> currencyValues = currencies.stream()
                    .map(c -> String.format("  ('%s', '%s', %s, %s, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                            c.getCode(),
                            c.getDisplayName() != null ? c.getDisplayName().replace("'", "''") : "",
                            c.getStartDate() != null ? "'" + c.getStartDate() + "'" : "null",
                            c.getEndDate() != null ? "'" + c.getEndDate() + "'" : "null"))
                    .toList();
            out.println(String.join(",\n", currencyValues) + ";");
            out.println();

            for (int i = 0; i < rates.size(); i += BATCH_SIZE) {
                List<Map<String, Object>> batch = rates.subList(i, Math.min(i + BATCH_SIZE, rates.size()));
                out.println("INSERT INTO exchange_rate (currency_code, exchange_date, exchange_rate, created_date, last_modified_date) VALUES");
                List<String> batchValues = batch.stream()
                        .map(r -> String.format("  ('%s', '%s', %s, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                                r.get("CURRENCY_CODE"),
                                r.get("EXCHANGE_DATE"),
                                r.get("EXCHANGE_RATE")))
                        .toList();
                out.println(String.join(",\n", batchValues) + ";");
                out.println();
            }
        }

        System.out.printf("%nGenerated %d currencies and %d exchange rates -> %s%n",
                currencies.size(), rates.size(), OUTPUT_FILE);
    }
}
