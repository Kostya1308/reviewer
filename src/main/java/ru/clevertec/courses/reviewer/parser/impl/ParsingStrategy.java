package ru.clevertec.courses.reviewer.parser.impl;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;
import ru.clevertec.courses.reviewer.validator.CsvValidator;

import static ru.clevertec.courses.reviewer.constant.Constant.*;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public abstract class ParsingStrategy {

    protected final CsvValidator csvValidator;

    public abstract ReceiptDto parse(File file);

    public abstract String getHeader();

    String getCsvData(List<String[]> stringArrayList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String[] array : stringArrayList) {
            stringBuilder.append(String.join(SEPARATOR_STRING, array));
            stringBuilder.append(APPENDER);
        }

        return stringBuilder.toString();
    }

    @SneakyThrows
    protected List<String[]> getStringArrayList(CSVReader commonFileCsvReader, String... headers)
            throws IncorrectFileStructureException {
        List<String[]> stringArrayList = new ArrayList<>();

        while (!Arrays.toString(commonFileCsvReader.peek()).equals(Arrays.toString(EMPTY_STRING_ARRAY))) {
            csvValidator.checkNextLineIsNotHeader(commonFileCsvReader.peek(), headers);
            stringArrayList.add(commonFileCsvReader.readNext());
        }

        return stringArrayList;
    }

    @SneakyThrows
    protected List<String[]> getLastStringArrayList(CSVReader commonFileCsvReader) {
        List<String[]> totalStringArrayList = new ArrayList<>();

        while (!(Arrays.toString(commonFileCsvReader.peek()).equals(Arrays.toString(EMPTY_STRING_ARRAY)) ||
                commonFileCsvReader.peek() == null)) {
            totalStringArrayList.add(commonFileCsvReader.readNext());
        }

        return totalStringArrayList;
    }

    protected <T> List<T> getParseList(InputStreamReader inputStreamReader, Class<T> clazz) {
        return new CsvToBeanBuilder<T>(inputStreamReader)
                .withType(clazz)
                .withSeparator(SEPARATOR_CHAR)
                .build()
                .parse();
    }

    protected <T> T getRequiredFirstItem(List<T> parseList) {
        if (parseList.size() == REQUIRED_NUMBER_ONE) {
            return parseList.getFirst();
        } else {
            throw new IncorrectFileStructureException();
        }
    }

}
