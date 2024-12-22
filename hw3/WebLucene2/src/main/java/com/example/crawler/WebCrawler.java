package com.example.crawler;

import com.example.indexer.Indexer;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WebCrawler {
    private final Set<String> visitedUrls = new HashSet<>();
    private final Indexer indexer;
    private final int maxDepth;
    private int crawledCount = 0;
    private static final int MAX_PAGES = 10; // 限制爬取页面数量

    public WebCrawler(Indexer indexer, int maxDepth) {
        this.indexer = indexer;
        this.maxDepth = maxDepth;
    }

    public void crawl(String url, int depth) {
        if (depth > maxDepth || visitedUrls.contains(url) || crawledCount >= MAX_PAGES) {
            return;
        }

        try {
            visitedUrls.add(url);
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)").timeout(10000).get();

            // 提取网页内容
            StringBuilder content = new StringBuilder();
            String title = doc.title();

            // 提取主要内容
            Elements mainContent = doc.select("p, h1, h2, h3, article, .content, .main");
            mainContent.forEach(element -> {
                String text = element.text().trim();
                if (!text.isEmpty()) {
                    content.append(text).append("\n");
                }
            });

            // 如果内容太少，获取更多内容
            if (content.length() < 100) {
                content.append(doc.body().text());
            }

            // 索引页面内容
            indexer.createIndex("标题: " + title + "\n内容: " + content.toString(), "webpage:" + url, "webpage");
            indexer.commit();

            crawledCount++;
            System.out.println("已成功索引第 " + crawledCount + " 个网页");
            System.out.println("网址: " + url);
            System.out.println("标题: " + title);
            System.out.println("--------------------");

            // 只在第一层爬取链接
            if (depth == 0) {
                doc.select("a[href]").stream().map(link -> link.attr("abs:href")).filter(nextUrl -> !nextUrl.isEmpty() && nextUrl.startsWith("http") && !nextUrl.contains("#") && !visitedUrls.contains(nextUrl)).limit(MAX_PAGES - crawledCount).forEach(nextUrl -> crawl(nextUrl, depth + 1));
            }
        } catch (IOException e) {
            System.err.println("爬取页面失败: " + url + "\n原因: " + e.getMessage());
        }
    }

    public void waitForCompletion() {
        System.out.println("\n爬取完成！共索引了 " + crawledCount + " 个网页");
        System.out.println("现在可以搜索网页内容了！");
    }
}