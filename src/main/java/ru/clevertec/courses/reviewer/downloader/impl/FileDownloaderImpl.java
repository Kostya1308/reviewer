package ru.clevertec.courses.reviewer.downloader.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.downloader.FileDownloader;

import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileDownloaderImpl implements FileDownloader {

    private final ResourceLoader resourceLoader;

    @SneakyThrows
    public File downloadFromPath(String path) {
        return resourceLoader.getResource(path).getFile();
    }

}
