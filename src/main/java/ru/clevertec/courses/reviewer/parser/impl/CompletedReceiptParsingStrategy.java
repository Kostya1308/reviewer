package ru.clevertec.courses.reviewer.parser.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.CompletedReceiptDto;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;
import ru.clevertec.courses.reviewer.validator.CsvValidator;

import static ru.clevertec.courses.reviewer.constant.Constant.DATE_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.DEFAULT_NUMBER_LINES_TO_SKIP;
import static ru.clevertec.courses.reviewer.constant.Constant.DESCRIPTION_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.DISCOUNT_CARD_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.DISCOUNT_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.DISCOUNT_PERCENTAGE_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.PRICE_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.QTY_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.REQUIRED_NUMBER_ONE;
import static ru.clevertec.courses.reviewer.constant.Constant.SEPARATOR_CHAR;
import static ru.clevertec.courses.reviewer.constant.Constant.TIME_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.TOTAL_DISCOUNT_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.TOTAL_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.TOTAL_PRICE_HEADER;
import static ru.clevertec.courses.reviewer.constant.Constant.TOTAL_WITH_DISCOUNT_HEADER;
import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class CompletedReceiptParsingStrategy extends ParsingStrategy {

    public CompletedReceiptParsingStrategy(CsvValidator csvValidator) {
        super(csvValidator);
    }

    @Override
    public ReceiptDto parse(File file) {
        try {
            CompletedReceiptDto.DateTimeInfo dateTimeParse = getDateTimeParse(file);
            int skipLines = REQUIRED_NUMBER_ONE + DEFAULT_NUMBER_LINES_TO_SKIP;

            List<CompletedReceiptDto.GoodsInfo> goodsParse = getGoodsParse(file, skipLines);
            skipLines += goodsParse.size() + DEFAULT_NUMBER_LINES_TO_SKIP;

            CompletedReceiptDto.DiscountInfo discountParse = getDiscountParse(file, skipLines);
            skipLines = Optional.ofNullable(discountParse).isEmpty() ?
                    skipLines : skipLines + REQUIRED_NUMBER_ONE + DEFAULT_NUMBER_LINES_TO_SKIP;

            CompletedReceiptDto.TotalInfo totalParse = getTotalParse(file, skipLines);

            return CompletedReceiptDto.builder()
                    .dateTimeInfo(dateTimeParse)
                    .goodsInfoList(goodsParse)
                    .discountInfo(discountParse)
                    .totalInfo(totalParse)
                    .build();

        } catch (IncorrectFileStructureException e) {
            throw new IncorrectFileStructureException(substringToDot(file.getName()));
        }

    }

    @Override
    public String getHeader() {
        return DATE_HEADER;
    }

    private CompletedReceiptDto.DateTimeInfo getDateTimeParse(File file) throws IncorrectFileStructureException {
        List<CompletedReceiptDto.DateTimeInfo> dateTimeParse = new ArrayList<>();

        try (InputStream commonFileInputStream = new FileInputStream(file);
             InputStreamReader commonFileInputStreamReader =
                     new InputStreamReader(Objects.requireNonNull(commonFileInputStream), StandardCharsets.UTF_8);
             CSVReader commonFileCsvReader = new CSVReader(commonFileInputStreamReader)) {

            csvValidator.checkFirstLineIsHeader(commonFileCsvReader.peek(), DATE_HEADER, TIME_HEADER);

            List<String[]> dateTimeStringArrayList = getStringArrayList(commonFileCsvReader, QTY_HEADER);
            String dateTimeCsvData = getCsvData(dateTimeStringArrayList);

            InputStream datetimeInputStream = new ByteArrayInputStream(dateTimeCsvData.getBytes(StandardCharsets.UTF_8));
            InputStreamReader datetimeInputStreamReader =
                    new InputStreamReader(Objects.requireNonNull(datetimeInputStream), StandardCharsets.UTF_8);

            dateTimeParse = getParseList(datetimeInputStreamReader, CompletedReceiptDto.DateTimeInfo.class);

            datetimeInputStream.close();
            datetimeInputStreamReader.close();

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return getRequiredFirstItem(dateTimeParse);
    }

    private List<CompletedReceiptDto.GoodsInfo> getGoodsParse(File file, int skipLines) {
        List<CompletedReceiptDto.GoodsInfo> goodsParse = new ArrayList<>();

        try (InputStream commonFileInputStream = new FileInputStream(file);
             InputStreamReader commonFileInputStreamReader =
                     new InputStreamReader(Objects.requireNonNull(commonFileInputStream), StandardCharsets.UTF_8);
             CSVReader commonFileCsvReader = new CSVReaderBuilder(commonFileInputStreamReader)
                     .withSkipLines(skipLines)
                     .build()) {

            csvValidator.checkFirstLineIsHeader(commonFileCsvReader.peek(), QTY_HEADER, DESCRIPTION_HEADER, PRICE_HEADER,
                    DISCOUNT_HEADER, TOTAL_HEADER);

            List<String[]> goodsStringArrayList = getStringArrayList(commonFileCsvReader, DISCOUNT_CARD_HEADER, TOTAL_PRICE_HEADER);
            String goodsCsvData = getCsvData(goodsStringArrayList);

            InputStream goodsInputStream = new ByteArrayInputStream(goodsCsvData.getBytes(StandardCharsets.UTF_8));
            InputStreamReader goodsInputStreamReader =
                    new InputStreamReader(Objects.requireNonNull(goodsInputStream), StandardCharsets.UTF_8);

            goodsParse = getParseList(goodsInputStreamReader, CompletedReceiptDto.GoodsInfo.class);

            goodsInputStream.close();
            goodsInputStreamReader.close();

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return goodsParse;
    }

    private CompletedReceiptDto.DiscountInfo getDiscountParse(File file, int skipLines) {
        List<CompletedReceiptDto.DiscountInfo> discountParse = new ArrayList<>();

        try (InputStream commonFileInputStream = new FileInputStream(file);
             InputStreamReader commonFileInputStreamReader =
                     new InputStreamReader(Objects.requireNonNull(commonFileInputStream), StandardCharsets.UTF_8);
             CSVReader commonFileCsvReader = new CSVReaderBuilder(commonFileInputStreamReader)
                     .withSkipLines(skipLines)
                     .build()) {

            if (Arrays.toString(commonFileCsvReader.peek()).contains(TOTAL_PRICE_HEADER)) {
                return null;
            }

            csvValidator.checkFirstLineIsHeader(commonFileCsvReader.peek(), DISCOUNT_CARD_HEADER, DISCOUNT_PERCENTAGE_HEADER);

            List<String[]> discountStringArrayList = getStringArrayList(commonFileCsvReader, TOTAL_HEADER);
            String discountCsvData = getCsvData(discountStringArrayList);

            InputStream discountInputStream = new ByteArrayInputStream(discountCsvData.getBytes(StandardCharsets.UTF_8));
            InputStreamReader discountInputStreamReader =
                    new InputStreamReader(Objects.requireNonNull(discountInputStream), StandardCharsets.UTF_8);

            discountParse = getParseList(discountInputStreamReader, CompletedReceiptDto.DiscountInfo.class);

            discountInputStream.close();
            discountInputStreamReader.close();

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return getRequiredFirstItem(discountParse);
    }


    private CompletedReceiptDto.TotalInfo getTotalParse(File file, int skipLines) {
        List<CompletedReceiptDto.TotalInfo> discountParse = new ArrayList<>();
        try (InputStream commonFileInputStream = new FileInputStream(file);
             InputStreamReader commonFileInputStreamReader =
                     new InputStreamReader(Objects.requireNonNull(commonFileInputStream), StandardCharsets.UTF_8);
             CSVReader commonFileCsvReader = new CSVReaderBuilder(commonFileInputStreamReader)
                     .withSkipLines(skipLines)
                     .build()) {

            csvValidator.checkFirstLineIsHeader(commonFileCsvReader.peek(), TOTAL_PRICE_HEADER, TOTAL_DISCOUNT_HEADER, TOTAL_WITH_DISCOUNT_HEADER);

            List<String[]> totalStringArrayList = getLastStringArrayList(commonFileCsvReader);
            String totalCsvData = getCsvData(totalStringArrayList);

            InputStream totalInputStream = new ByteArrayInputStream(totalCsvData.getBytes(StandardCharsets.UTF_8));
            InputStreamReader totalInputStreamReader =
                    new InputStreamReader(Objects.requireNonNull(totalInputStream), StandardCharsets.UTF_8);

            discountParse = getParseList(totalInputStreamReader, CompletedReceiptDto.TotalInfo.class);

            totalInputStream.close();
            totalInputStreamReader.close();

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return getRequiredFirstItem(discountParse);
    }

    private static <T extends CompletedReceiptDto.Body> List<T> getParseList(InputStreamReader inputStreamReader,
                                                                             Class<T> clazz) {
        List<T> parse = new CsvToBeanBuilder<T>(inputStreamReader)
                .withType(clazz)
                .withSeparator(SEPARATOR_CHAR)
                .build()
                .parse();

        return getCastedList(parse);
    }

    private static <T extends CompletedReceiptDto.Body> List<T> getCastedList(List<T> parse) {
        List<T> castedList = new ArrayList<>();

        for (T t : parse) {
            try {
                castedList.add(t);
            } catch (ClassCastException e) {
                log.error(e.getMessage());
            }
        }

        return castedList;
    }


}
