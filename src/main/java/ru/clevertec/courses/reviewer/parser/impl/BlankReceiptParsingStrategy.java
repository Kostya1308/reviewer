package ru.clevertec.courses.reviewer.parser.impl;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.constant.Constant;
import ru.clevertec.courses.reviewer.dto.BlankReceiptDto;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.validator.CsvValidator;

import static java.nio.charset.StandardCharsets.*;
import static ru.clevertec.courses.reviewer.constant.Constant.SEPARATOR_CHAR;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BlankReceiptParsingStrategy extends ParsingStrategy {

    public BlankReceiptParsingStrategy(CsvValidator csvValidator) {
        super(csvValidator);
    }

    @Override
    public ReceiptDto parse(File file) {
        List<BlankReceiptDto> blankReceiptDtoList = new ArrayList<>();

        try (var commonFileInputStreamReader = new InputStreamReader(new FileInputStream(file), UTF_8);
             var commonFileCsvReader = new CSVReader(commonFileInputStreamReader)) {

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
