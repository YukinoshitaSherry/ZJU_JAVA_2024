package com.example.parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Parser {
    private final Tika tika;

    public Parser() { this.tika = new Tika(); }

    public Map<String, String> parseFile(String filePath) throws IOException, TikaException {
        File file = new File(filePath);
        Map<String, String> result = new HashMap<>();

        // 获取文件类型
        String mimeType = tika.detect(file);
        result.put("mimeType", mimeType);
        result.put("fileName", file.getName());

        // 根据文件类型选择解析方式
        if (mimeType.contains("html")) {
            Document doc = Jsoup.parse(file, "UTF-8");
            result.put("title", doc.title());
            result.put("content", doc.body().text());
        } else {
            // 使用Tika解析其他类型文件
            result.put("content", tika.parseToString(file));
        }

        return result;
    }
}