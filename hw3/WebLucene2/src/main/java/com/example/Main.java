package com.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.tika.exception.TikaException;

import com.example.indexer.Indexer;
import com.example.parser.Parser;
import com.example.searcher.Searcher;
import com.example.searcher.Searcher.SearchResult;


public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // 让用户指定要监控的文件夹
            System.out.println("请输入要监控的文件夹路径：");
            String watchPath = scanner.nextLine();

            // 创建解析器和索引器
            Parser parser = new Parser();
            Indexer indexer = new Indexer();

            // 首次索引现有文件
            indexExistingFiles(watchPath, parser, indexer);

            // 启动文件监控
            startFileWatcher(watchPath, parser, indexer);

            // 创建搜索器
            Searcher searcher = new Searcher();

            // 交互式搜索
            while (true) {
                System.out.println("\n请选择操作：");
                System.out.println("1. 搜索文件");
                System.out.println("2. 更改监控目录");
                System.out.println("3. 退出");

                int choice = scanner.nextInt();
                scanner.nextLine(); // 消费换行符

                switch (choice) {
                case 1:
                    performSearch(searcher, scanner);
                    break;
                case 2:
                    System.out.println("请输入新的监控目录：");
                    watchPath = scanner.nextLine();
                    indexExistingFiles(watchPath, parser, indexer);
                    break;
                case 3:
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void indexExistingFiles(String path, Parser parser, Indexer indexer) throws IOException {
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IOException("无效的目录路径: " + path);
        }

        System.out.println("开始索引现有文件...");
        indexFilesInDirectory(folder, parser, indexer);
        indexer.commit();
        System.out.println("索引完成！");
    }

    private static void indexFilesInDirectory(File folder, Parser parser, Indexer indexer) throws IOException {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    indexFilesInDirectory(file, parser, indexer);
                } else {
                    try {
                        Map<String, String> fileContent = parser.parseFile(file.getPath());
                        if (fileContent.get("content") != null) {
                            indexer.createIndex(fileContent.get("content"), file.getPath());
                            System.out.println("已索引: " + file.getPath());
                        }
                    } catch (TikaException e) {
                        System.err.println("解析文件失败: " + file.getPath() + ", 错误: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("索引文件失败: " + file.getPath() + ", 错误: " + e.getMessage());
                    }
                }
            }
        }
    }

    private static void startFileWatcher(String path, Parser parser, Indexer indexer) {
        new Thread(() -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path dir = Paths.get(path);
                dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path filePath = dir.resolve((Path)event.context());
                        if (Files.isRegularFile(filePath)) {
                            try {
                                Map<String, String> fileContent = parser.parseFile(filePath.toString());
                                indexer.createIndex(fileContent.get("content"), filePath.toString());
                                indexer.commit();
                                System.out.println("新文件已索引: " + filePath);
                            } catch (Exception e) {
                                System.err.println("索引新文件失败: " + filePath);
                            }
                        }
                    }
                    key.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void performSearch(Searcher searcher, Scanner scanner) {
        try {
            System.out.println("请输入搜索关键词：");
            String keyword = scanner.nextLine();

            List<SearchResult> results = searcher.search(keyword);
            if (results.isEmpty()) {
                System.out.println("未找到匹配结果");
                return;
            }

            System.out.println("\n找到 " + results.size() + " 个结果:");
            for (SearchResult result : results) {
                System.out.println("\n文件路径: " + result.getFilePath());
                System.out.println("相关度得分: " + result.getScore());
                System.out.println("----------------------------------------");
            }
        } catch (Exception e) {
            System.err.println("搜索出错: " + e.getMessage());
        }
    }
}