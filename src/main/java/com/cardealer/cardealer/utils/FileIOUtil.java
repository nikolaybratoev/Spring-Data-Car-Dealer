package com.cardealer.cardealer.utils;

import java.io.IOException;

public interface FileIOUtil {

    String readFileContent(String filePath) throws IOException;

    void write(String contents, String filePath) throws IOException;
}
