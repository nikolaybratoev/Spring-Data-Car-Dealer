package com.cardealer.cardealer.utils.impls;

import com.cardealer.cardealer.utils.FileIOUtil;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class FileIOUtilImpl implements FileIOUtil {

    @Override
    public String readFileContent(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath))
                .stream()
                .filter(row -> !row.isEmpty())
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public void write(String content, String filePath) throws IOException {
        Files.write(Paths.get(filePath),
                Collections.singleton(content), UTF_8);
    }
}
