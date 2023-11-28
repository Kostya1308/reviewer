package ru.clevertec.courses.reviewer.parser.impl;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;
import ru.clevertec.courses.reviewer.factory.ParsingStrategyFactory;
import ru.clevertec.courses.reviewer.parser.FileParser;

import static java.nio.charset.StandardCharsets.UTF_8;
import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileParserImpl implements FileParser {

    private final ParsingStrategyFactory parsingStrategyFactory;

    public ReceiptDto parseCsvFile(File file) {
        ReceiptDto receiptDto = null;

        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), UTF_8);
             CSVReader commonFileCsvReader = new CSVReader(inputStreamReader)) {

            String[] firstLine = commonFileCsvReader.peek();
            ParsingStrategy parsingStrategy = parsingStrategyFactory.getStrategy(firstLine[0]);

            receiptDto = parsingStrategy.parse(file);

        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (IncorrectFileStructureException e) {
            throw new IncorrectFileStructureException(substringToDot(file.getName()));
        }

        return receiptDto;
    }

}
