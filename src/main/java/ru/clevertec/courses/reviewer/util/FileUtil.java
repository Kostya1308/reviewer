package ru.clevertec.courses.reviewer.util;

import static ru.clevertec.courses.reviewer.constant.Constant.DOT;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtil {

    public static String substringToDot(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(DOT));
    }

}
