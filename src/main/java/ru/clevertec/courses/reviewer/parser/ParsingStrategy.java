package ru.clevertec.courses.reviewer.parser;

import com.opencsv.CSVReader;
import lombok.SneakyThrows;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.exception.FailedReviewException;

import static ru.clevertec.courses.reviewer.constant.Constant.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ParsingStrategy {

    ReceiptDto parse(File file);

    String getHeader();

    default String getCsvData(List<String[]> stringArrayList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String[] array : stringArrayList) {
            stringBuilder.append(String.join(SEPARATOR_STRING, array));
            stringBuilder.append(APPENDER);
        }

        return stringBuilder.toString();
    }


    @SneakyThrows
    default List<String[]> getLastStringArrayList(CSVReader commonFileCsvReader) {
        List<String[]> totalStringArrayList = new ArrayList<>();

        while (!(Arrays.toString(commonFileCsvReader.peek()).equals(Arrays.toString(EMPTY_STRING_ARRAY)) ||
                commonFileCsvReader.peek() == null)) {
            totalStringArrayList.add(commonFileCsvReader.readNext());
        }

        return totalStringArrayList;
    }

    default <T> T getRequiredFirstItem(List<T> parseList) {
        if (parseList.size() == REQUIRED_NUMBER_ONE) {
            return parseList.get(0);
        } else {
            throw new FailedReviewException(INCORRECT_STRUCTURE_MESSAGE);
        }
    }

}
