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

import com.example.crawler.WebCrawler;
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
                System.out.println("3. 添加网页索引");
                System.out.println("4. 退出");

                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // 消费换行符

                    if (choice < 1 || choice > 4) {
                        System.out.println("无效的选项，请输入1-4之间的数字");
                        continue;
                    }

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
                        System.out.println("请输入要索引的网页URL（例如: https://www.baidu.com）：");
                        String url = scanner.nextLine();
                        System.out.println("请输入爬取深度（1-3，数字越大爬取范围越广）：");
                        int depth = scanner.nextInt();
                        scanner.nextLine(); // 消费换行符

                        System.out.println("\n开始爬取网页，请稍候...");
                        System.out.println("提示：爬取过程中会显示进度，完成后即可搜索网页内容");

                        WebCrawler crawler = new WebCrawler(indexer, depth);
                        crawler.crawl(url, 0);
                        crawler.waitForCompletion();
                        break;
                    case 4:
                        System.out.println("感谢使用搜索引擎，再见！");
                        indexer.close(); // 关闭索引器
                        System.exit(0);  // 确保程序完全退出
                        return;
                    }
                } catch (Exception e) {
                    System.out.println("输入错误，请输入有效的数字选项(1-4)");
                    scanner.nextLine(); // 清除错误输入
                    continue;
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

    private static void performSearch(Searcher searcher, Scanner scanner) {
        try {
            System.out.println("\n请选择搜索选项：");
            System.out.println("1. 按关键词搜索");
            System.out.println("2. 按文件类型搜索 (如: pdf, doc, html)");
            System.out.println("3. 按文件路径搜索");
            System.out.println("4. 组合搜索");

            int option = scanner.nextInt();
            scanner.nextLine(); // 消费换行符

            String query = "";
            switch (option) {
            case 1:
                System.out.println("请输入搜索关键词：");
                query = scanner.nextLine();
                System.out.println("搜索范围：");
                System.out.println("1. 所有内容");
                System.out.println("2. 仅本地文件");
                System.out.println("3. 仅网页内容");
                int scope = scanner.nextInt();
                scanner.nextLine(); // 消费换行符

                switch (scope) {
                case 2:
                    query = query + " AND NOT path:webpage*";
                    break;
                case 3:
                    query = query + " AND path:webpage*";
                    break;
                }
                break;
            case 2:
                System.out.println("请输入文件类型（如：html）：");
                String fileType = scanner.nextLine().toLowerCase();
                query = "mimeType:" + fileType;
                break;
            case 3:
                System.out.println("请输入文件路径包含的内容：");
                String pathQuery = scanner.nextLine();
                query = "path:" + pathQuery;
                break;
            case 4:
                System.out.println("请输入关键词：");
                String keyword = scanner.nextLine();
                System.out.println("请输入文件类型（可选，直接回车跳过）：");
                String type = scanner.nextLine();
                query = keyword;
                if (!type.trim().isEmpty()) {
                    query += " AND mimeType:" + type;
                }
                break;
            }

            List<SearchResult> results = searcher.search(query);
            if (results.isEmpty()) {
                System.out.println("未找到匹配结果");
                return;
            }

            System.out.println("\n找到 " + results.size() + " 个结果:");
            for (SearchResult result : results) {
                System.out.println("\n文件路径: " + result.getFilePath());
                System.out.println("文件类型: " + (result.getMimeType() != null ? result.getMimeType() : "未知类型"));
                System.out.println("相关度得: " + result.getScore());
                System.out.println("内容预览: " + result.getPreview());
                System.out.println("----------------------------------------");
            }
        } catch (Exception e) {
            System.err.println("搜索出错: " + e.getMessage());
        }
    }
}