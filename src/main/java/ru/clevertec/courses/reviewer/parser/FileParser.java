package ru.clevertec.courses.reviewer.parser;

import ru.clevertec.courses.reviewer.dto.ReceiptDto;

import java.io.File;

public interface FileParser {

    ReceiptDto parseCsvFile(File file);

}
