package ru.clevertec.courses.reviewer.parser.impl;

import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.dto.CompletedReceiptDto;
import ru.clevertec.courses.reviewer.dto.ReceiptDto;
import ru.clevertec.courses.reviewer.exception.IncorrectFileStructureException;
import ru.clevertec.courses.reviewer.validator.CsvValidator;

import static ru.clevertec.courses.reviewer.constant.Constant.*;
import static ru.clevertec.courses.reviewer.util.FileUtil.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Component
public class CompletedReceiptParsingStrategy extends ParsingStrategy {

    public CompletedReceiptParsingStrategy(ICSVParser icsvParser, CsvValidator csvValidator) {
        super(icsvParser, csvValidator);
    }

    @Override
    @SneakyThrows
    public ReceiptDto parse(File file) {
        try (var fileReader = new InputStreamReader(new BufferedInputStream(new FileInputStream(file)));
             var csvReader = new CSVReaderBuilder(fileReader)
                     .withCSVParser(icsvParser)
                     .build()) {

            List<String[]> dateTimeStrings = getStringArrayList(dateTimeHeaders, goodsHeaders, csvReader, true);
            String dateTimeCsvData = getCsvData(dateTimeStrings);
            List<CompletedReceiptDto.DateTimeInfo> dateTimeInfoList =
                    getParseList(dateTimeCsvData, CompletedReceiptDto.DateTimeInfo.class);

            List<String[]> goodsStrings = getStringArrayList(goodsHeaders, discountHeaders, csvReader, true);
            String goodsCsvData = getCsvData(goodsStrings);
            List<CompletedReceiptDto.GoodsInfo> goodsInfoList =
                    getParseList(goodsCsvData, CompletedReceiptDto.GoodsInfo.class);

            List<String[]> discountStrings = getStringArrayList(discountHeaders, totalHeaders, csvReader, false);
            String discountCsvData = getCsvData(discountStrings);
            List<CompletedReceiptDto.DiscountInfo> discountInfoList =
                    getParseList(discountCsvData, CompletedReceiptDto.DiscountInfo.class);

            List<String[]> totalStrings = getStringArrayList(totalHeaders, List.of(), csvReader, true);
            String totalCsvData = getCsvData(totalStrings);
            List<CompletedReceiptDto.TotalInfo> totalInfoList =
                    getParseList(totalCsvData, CompletedReceiptDto.TotalInfo.class);

            return CompletedReceiptDto.builder()
                    .dateTimeInfo(getRequiredFirstItem(dateTimeInfoList))
                    .goodsInfoList(goodsInfoList)
                    .discountInfo(getRequiredFirstItem(discountInfoList))
                    .totalInfo(getRequiredFirstItem(totalInfoList))
                    .build();

        } catch (IncorrectFileStructureException e) {
            throw new IncorrectFileStructureException(substringToDot(file.getName()));
        }
    }

    @Override
    public String getHeader() {
        return DATE_HEADER;
    }

}
