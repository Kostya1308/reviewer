package ru.clevertec.courses.reviewer.util;

import lombok.experimental.UtilityClass;

import static ru.clevertec.courses.reviewer.constant.Constant.DOT;

@UtilityClass
public class FileUtil {

    public static String substringToDot(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(DOT));
    }

}
