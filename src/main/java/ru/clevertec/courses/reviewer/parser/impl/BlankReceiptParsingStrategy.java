package ru.clevertec.courses.reviewer.parser.impl;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.constant.Constant;
import ru.clevertec.courses.reviewer.dto.BlankReceiptDto;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.validator.CsvValidator;

import static ru.clevertec.courses.reviewer.constant.Constant.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class BlankReceiptParsingStrategy extends ParsingStrategy {

    public BlankReceiptParsingStrategy(CsvValidator csvValidator) {
        super(csvValidator);
    }

    @Override
    public ReceiptDto parse(File file) {
        List<BlankReceiptDto> blankReceiptDtoList = new ArrayList<>();

        try (InputStream commonFileInputStream = new FileInputStream(file);
             InputStreamReader commonFileInputStreamReader =
                     new InputStreamReader(Objects.requireNonNull(commonFileInputStream), StandardCharsets.UTF_8);
             CSVReader commonFileCsvReader = new CSVReader(commonFileInputStreamReader)) {

            List<String[]> errorStringArrayList = getLastStringArrayList(commonFileCsvReader);
            String errorCsvData = getCsvData(errorStringArrayList);

            InputStream errorInputStream = new ByteArrayInputStream(errorCsvData.getBytes(StandardCharsets.UTF_8));
            InputStreamReader errorInputStreamReader =
                    new InputStreamReader(Objects.requireNonNull(errorInputStream), StandardCharsets.UTF_8);

            blankReceiptDtoList = new CsvToBeanBuilder<BlankReceiptDto>(errorInputStreamReader)
                    .withType(BlankReceiptDto.class)
                    .withSeparator(SEPARATOR_CHAR)
                    .build()
                    .parse();

            errorInputStream.close();
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
