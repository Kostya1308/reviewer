package ru.clevertec.courses.reviewer.downloader;

import java.io.File;

public interface FileDownloader {

    File downloadFromPath(String path);

}
