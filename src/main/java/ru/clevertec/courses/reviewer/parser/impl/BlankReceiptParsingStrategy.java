package ru.clevertec.courses.reviewer.parser.impl;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.constant.Constant;
import ru.clevertec.courses.reviewer.dto.BlankReceiptDto;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;
import ru.clevertec.courses.reviewer.validator.CsvValidator;

import static ru.clevertec.courses.reviewer.constant.Constant.ERROR_HEADER;
import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Component
public class BlankReceiptParsingStrategy extends ParsingStrategy {

    public BlankReceiptParsingStrategy(ICSVParser icsvParser, CsvValidator csvValidator) {
        super(icsvParser, csvValidator);
    }

    @Override
    @SneakyThrows
    public ReceiptDto parse(File file) {
        try (var fileReader = new InputStreamReader(new BufferedInputStream(new FileInputStream(file)));
             var csvReader = new CSVReaderBuilder(fileReader)
                     .withCSVParser(icsvParser)
                     .build()) {

            List<String[]> errorStrings = getStringArrayList(List.of(ERROR_HEADER), List.of(), csvReader, true);
            String errorCsvData = getCsvData(errorStrings);
            List<BlankReceiptDto> blankReceiptDtoList = getParseList(errorCsvData, BlankReceiptDto.class);

            return getRequiredFirstItem(blankReceiptDtoList);

        } catch (IncorrectFileStructureException e) {
            throw new IncorrectFileStructureException(substringToDot(file.getName()));
        }
    }

    @Override
    public String getHeader() {
        return Constant.ERROR_HEADER;
    }
}
