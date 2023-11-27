package ru.clevertec.courses.reviewer.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.clevertec.courses.reviewer.constant.Constant;

@Data
@EqualsAndHashCode
public class BlankReceiptDto implements ReceiptDto{

    @CsvBindByName(column = Constant.ERROR_HEADER)
    private String errorMessage;

}
