package ru.clevertec.courses.reviewer.parser.impl;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.factory.ParsingStrategyFactory;
import ru.clevertec.courses.reviewer.parser.FileParser;
import ru.clevertec.courses.reviewer.parser.ParsingStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileParserImpl implements FileParser {

    private final ParsingStrategyFactory parsingStrategyFactory;

    @SneakyThrows
    public ReceiptDto parseCsvFile(File file) {
        try (InputStream commonFileInputStream = new FileInputStream(file);
             InputStreamReader commonFileInputStreamReader =
                     new InputStreamReader(Objects.requireNonNull(commonFileInputStream), StandardCharsets.UTF_8);
             CSVReader commonFileCsvReader = new CSVReader(commonFileInputStreamReader)) {

            String[] firstLine = commonFileCsvReader.peek();
            ParsingStrategy parsingStrategy = parsingStrategyFactory.getStrategy(firstLine[0], file.getName());

            return parsingStrategy.parse(file);

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new IOException(e);
        }
    }

}
