package ru.clevertec.courses.reviewer.parser.impl;

import com.opencsv.CSVReader;
import com.opencsv.ICSVParser;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;
import ru.clevertec.courses.reviewer.validator.CsvValidator;

import static java.nio.charset.StandardCharsets.UTF_8;
import static ru.clevertec.courses.reviewer.constant.Constant.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public abstract class ParsingStrategy {

    protected final ICSVParser icsvParser;
    protected final CsvValidator csvValidator;

    public abstract ReceiptDto parse(File file);

    public abstract String getHeader();

    protected String getCsvData(List<String[]> stringArrayList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String[] array : stringArrayList) {
            stringBuilder.append(String.join(SEPARATOR_STRING, array));
            stringBuilder.append(APPENDER);
        }

        return stringBuilder.toString();
    }

    @SneakyThrows
    protected List<String[]> getStringArrayList(List<String> currentHeaders, List<String> nextHeaders,
                                                CSVReader csvReader, boolean isRequired)
            throws IncorrectFileStructureException {

        if (!isRequired && Arrays.asList(csvReader.peek()).equals(nextHeaders)) {
            return List.of();
        }
        List<String[]> stringArrayList = new ArrayList<>();

        csvValidator.checkFirstLineIsHeader(csvReader.peek(), currentHeaders);

        while (!Arrays.toString(csvReader.peek()).equals(Arrays.toString(EMPTY_STRING_ARRAY)) &&
                csvReader.peek() != null) {
            csvValidator.checkNextLineIsNotHeader(csvReader.peek(), nextHeaders);
            stringArrayList.add(csvReader.readNext());
        }

        csvReader.readNext();

        return stringArrayList;
    }

    protected <T> List<T> getParseList(String csvData, Class<T> clazz) throws IncorrectFileStructureException {
        try (var inputStreamReader = new InputStreamReader(new ByteArrayInputStream(csvData.getBytes(UTF_8)), UTF_8)) {

            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(inputStreamReader)
                    .withType(clazz)
                    .withSeparator(SEPARATOR_CHAR)
                    .withThrowExceptions(true)
                    .withExceptionHandler(exception -> new CsvRequiredFieldEmptyException())
                    .build();

            return csvToBean.parse();

        } catch (Exception e) {
            throw new IncorrectFileStructureException();
        }
    }

    protected <T> T getRequiredFirstItem(List<T> parseList) {
        if (parseList.isEmpty()) {
            return null;

        } else if (parseList.size() == REQUIRED_NUMBER_ONE) {
            return parseList.getFirst();

        } else {
            throw new IncorrectFileStructureException();
        }
    }

}
