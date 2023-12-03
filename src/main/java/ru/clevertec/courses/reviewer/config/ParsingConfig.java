package ru.clevertec.courses.reviewer.config;

import com.opencsv.CSVParserBuilder;
import com.opencsv.ICSVParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.courses.reviewer.constant.Constant;

@Configuration
public class ParsingConfig {

    @Bean
    public ICSVParser icsvParser() {
        return new CSVParserBuilder()
                .withSeparator(Constant.SEPARATOR_CHAR)
                .withIgnoreQuotations(false)
                .withQuoteChar('#')
                .build();
    }

}
