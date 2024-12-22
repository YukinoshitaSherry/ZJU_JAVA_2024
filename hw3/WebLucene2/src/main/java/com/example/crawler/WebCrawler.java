package com.example.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.indexer.Indexer;


public class WebCrawler {
    private final Set<String> visitedUrls = new HashSet<>();
    private final Indexer indexer;
    private final ExecutorService executorService;
    private final int maxDepth;
    private int crawledCount = 0;
    private boolean isCompleted = false;

    public WebCrawler(Indexer indexer, int maxDepth) {
        this.indexer = indexer;
        this.maxDepth = maxDepth;
        this.executorService = Executors.newFixedThreadPool(5);
    }

    public void crawl(String url, int depth) {
        if (depth > maxDepth || visitedUrls.contains(url)) {
            return;
        }

        try {
            visitedUrls.add(url);
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)").timeout(5000).get();

            // 提取更有价值的内容
            StringBuilder content = new StringBuilder();
            content.append("标题: ").append(doc.title()).append("\n");
            content.append("URL: ").append(url).append("\n\n");

            // 提取网页主要内容
            Elements mainContent = doc.select("p, h1, h2, h3, h4, article, .content, .main");
            mainContent.forEach(element -> {
                String text = element.text().trim();
                if (!text.isEmpty()) {
                    content.append(text).append("\n");
                }
            });

            // 索引页面内容，使用特殊标记表明这是网页内容
            indexer.createIndex(content.toString(),
                                "webpage:" + url, // 添加前缀以区分网页和本地文件
                                "webpage"         // 使用特殊类型标记网页内容
            );
            indexer.commit();

            crawledCount++;
            System.out.println("已成功索引第 " + crawledCount + " 个网页");
            System.out.println("网址: " + url);
            System.out.println("标题: " + doc.title());
            System.out.println("提取内容长度: " + content.length() + " 字符");
            System.out.println("--------------------");

            // 获取并过滤链接
            doc.select("a[href]")
                .stream()
                .map(link -> link.attr("abs:href"))
                .filter(nextUrl -> !nextUrl.isEmpty() && nextUrl.startsWith("http") && !nextUrl.contains("#") && !visitedUrls.contains(nextUrl))
                .limit(10) // 限制每个页面的链接处理数量
                .forEach(nextUrl -> crawl(nextUrl, depth + 1));

        } catch (IOException e) {
            System.err.println("爬取页面失败: " + url + "\n原因: " + e.getMessage());
        }
    }

    public void waitForCompletion() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
            System.out.println("\n爬取完成！共索引了 " + crawledCount + " 个网页");
            System.out.println("现在可以搜索网页内容了！");
        } catch (InterruptedException e) {
            System.err.println("爬取超时，已索引 " + crawledCount + " 个网页");
        }
    }
}