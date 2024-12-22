package com.example;

import com.example.crawler.WebCrawler;
import com.example.indexer.Indexer;
import com.example.parser.Parser;
import com.example.searcher.Searcher;
import com.example.searcher.Searcher.SearchResult;
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

public class Main {
    private static Parser parser;
    private static Indexer indexer;
    private static Searcher searcher;
    private static String watchPath = null;
    private static boolean hasWebContent = false;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            parser = new Parser();
            indexer = new Indexer();
            searcher = new Searcher();

            System.out.println("欢迎使用文件搜索引擎！");
            System.out.println("本程序支持本地文件和网页内容的索引与搜索。");

            while (true) {
                showMainMenu();
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // 消费换行符

                    switch (choice) {
                    case 1: // 添加本地目录
                        handleLocalDirectory(scanner);
                        break;
                    case 2: // 添加网页索引
                        handleWebCrawling(scanner);
                        break;
                    case 3: // 开始搜索
                        if (watchPath == null && !hasWebContent) {
                            System.out.println("\n错误：请先添加本地目录或网页索引！");
                            continue;
                        }
                        handleSearch(scanner);
                        break;
                    case 4: // 退出
                        System.out.println("感谢使用搜索引擎，再见！");
                        indexer.close();
                        System.exit(0);
                        return;
                    default:
                        System.out.println("无效的选项，请输入1-4之间的数字");
                    }
                } catch (Exception e) {
                    System.out.println("输入错误，请输入有效的数字选项(1-4)");
                    scanner.nextLine(); // 清除错误输入
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showMainMenu() {
        System.out.println("\n请选择操作：");
        System.out.println("1. 添加本地目录");
        System.out.println("2. 添加网页索引");
        System.out.println("3. 开始搜索");
        System.out.println("4. 退出");
    }

    private static void handleLocalDirectory(Scanner scanner) {
        try {
            System.out.println("请输入要索引的目录路径（例如：documents）：");
            String path = scanner.nextLine();
            File folder = new File(path);

            if (!folder.exists() || !folder.isDirectory()) {
                System.out.println("错误：无效的目录路径！请确保目录存在且路径正确。");
                return;
            }

            watchPath = path;
            indexExistingFiles(path, parser, indexer);
            startFileWatcher(path, parser, indexer);
        } catch (IOException e) {
            System.out.println("错误：目录访问失败 - " + e.getMessage());
        }
    }

    private static void handleWebCrawling(Scanner scanner) {
        System.out.println("请输入要索引的网页URL（例如: https://www.baidu.com）：");
        String url = scanner.nextLine();
        System.out.println("请输入爬取深度（1-3，数字越大爬取范围越广）：");
        int depth = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\n开始爬取网页，请稍候...");
        WebCrawler crawler = new WebCrawler(indexer, depth);
        crawler.crawl(url, 0);
        crawler.waitForCompletion();
        hasWebContent = true;
    }

    private static void handleSearch(Scanner scanner) {
        try {
            System.out.println("请输入搜索关键词：");
            String query = scanner.nextLine();

            System.out.println("\n搜索范围：");
            System.out.println("1. 所有内容");
            System.out.println("2. 仅本地文件");
            System.out.println("3. 仅网页内容");

            int scope = scanner.nextInt();
            scanner.nextLine();

            switch (scope) {
            case 1:
                // 所有内容，不需要修改查询
                break;
            case 2:
                // 仅本地文件
                query += " AND NOT path:webpage*";
                break;
            case 3:
                // 仅网页内容
                query += " AND path:webpage*";
                break;
            default:
                System.out.println("无效的选项，使用默认搜索范围（所有内容）");
            }

            displaySearchResults(searcher.search(query));
        } catch (Exception e) {
            System.err.println("搜索出错: " + e.getMessage());
        }
    }

    private static void displaySearchResults(List<SearchResult> results) {
        if (results.isEmpty()) {
            System.out.println("未找到匹配结果");
            return;
        }

        System.out.println("\n找到 " + results.size() + " 个结果:");
        for (SearchResult result : results) {
            System.out.println("\n文件路径: " + result.getFilePath());
            System.out.println("文件类型: " + (result.getMimeType() != null ? result.getMimeType() : "未知类型"));
            System.out.println("相关度: " + result.getScore());
            System.out.println("内容预览: " + result.getPreview());
            System.out.println("----------------------------------------");
        }
    }

    private static void indexExistingFiles(String path, Parser parser, Indexer indexer) throws IOException {
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IOException("无效的目录路径: " + path);
        }

        System.out.println("开始索引现���文件...");
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
                            indexer.createIndex(fileContent.get("content"), file.getPath(), fileContent.get("mimeType"));
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
                                indexer.createIndex(fileContent.get("content"), filePath.toString(), fileContent.get("mimeType"));
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
}