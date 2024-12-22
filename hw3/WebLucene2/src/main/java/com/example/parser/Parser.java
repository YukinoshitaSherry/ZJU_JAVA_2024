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

    // 获取更友好的文件类型显示
    private String getReadableMimeType(String mimeType) {
        if (mimeType == null || mimeType.isEmpty()) {
            return "未知类型";
        }
        switch (mimeType) {
        case "text/html":
            return "HTML文件";
        case "application/pdf":
            return "PDF文件";
        case "application/msword":
        case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
            return "Word文档";
        case "text/plain":
            return "文本文件";
        default:
            return mimeType;
        }
    }

    private String normalizeMimeType(String mimeType) {
        if (mimeType == null || mimeType.isEmpty()) {
            return "unknown";
        }
        // 提取主要类型
        if (mimeType.contains("pdf")) {
            return "pdf";
        } else if (mimeType.contains("html")) {
            return "html";
        } else if (mimeType.contains("word")) {
            return "doc";
        } else if (mimeType.contains("text")) {
            return "txt";
        }
        return mimeType.split("/")[1];
    }

    public Map<String, String> parseFile(String filePath) throws IOException, TikaException {
        File file = new File(filePath);
        Map<String, String> result = new HashMap<>();

        // 获取文件类型
        String mimeType = tika.detect(file);
        if (mimeType == null || mimeType.isEmpty()) {
            mimeType = "application/octet-stream";
        }
        result.put("mimeType", normalizeMimeType(mimeType));
        result.put("readableMimeType", getReadableMimeType(mimeType));
        result.put("fileName", file.getName());

        // 根据文件类型选择解析方式
        if (mimeType.contains("html")) {
            Document doc = Jsoup.parse(file, "UTF-8");
            result.put("title", doc.title());
            result.put("content", doc.body().text().replaceAll("<[^>]*>", ""));
        } else {
            // 使用Tika解析其他类型文件
            result.put("content", tika.parseToString(file));
        }

        return result;
    }
}