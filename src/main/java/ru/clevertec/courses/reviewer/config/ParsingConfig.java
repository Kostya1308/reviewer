package ru.clevertec.courses.reviewer.config;

import com.opencsv.CSVParserBuilder;
import com.opencsv.ICSVParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.clevertec.courses.reviewer.constant.Constant.QUOTE_CHAR;
import static ru.clevertec.courses.reviewer.constant.Constant.SEPARATOR_CHAR;

@Configuration
public class ParsingConfig {

    @Bean
    public ICSVParser icsvParser() {
        return new CSVParserBuilder()
                .withSeparator(SEPARATOR_CHAR)
                .withIgnoreQuotations(false)
                .withQuoteChar(QUOTE_CHAR)
                .build();
    }

}
