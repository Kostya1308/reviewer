package ru.clevertec.courses.reviewer.parser.impl;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.constant.Constant;
import ru.clevertec.courses.reviewer.dto.BlankReceiptDto;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.validator.CsvValidator;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BlankReceiptParsingStrategy extends ParsingStrategy {

    public BlankReceiptParsingStrategy(ICSVParser icsvParser, CsvValidator csvValidator) {
        super(icsvParser, csvValidator);
    }

    @Override
    public ReceiptDto parse(File file) {
        List<BlankReceiptDto> blankReceiptDtoList = new ArrayList<>();

        try (var commonFileInputStreamReader = new InputStreamReader(new FileInputStream(file), UTF_8);
             var commonFileCsvReader = new CSVReaderBuilder(commonFileInputStreamReader)
                     .withCSVParser(icsvParser)
                     .build()) {

            List<String[]> errorStringArrayList = getLastStringArrayList(commonFileCsvReader);
            String errorCsvData = getCsvData(errorStringArrayList);
            var errorInputStreamReader = new InputStreamReader(new ByteArrayInputStream(errorCsvData.getBytes(UTF_8)));

            blankReceiptDtoList = getParseList(errorInputStreamReader, BlankReceiptDto.class);

            errorInputStreamReader.close();

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return getRequiredFirstItem(blankReceiptDtoList);
    }

    @Override
    public String getHeader() {
        return Constant.ERROR_HEADER;
    }
}
