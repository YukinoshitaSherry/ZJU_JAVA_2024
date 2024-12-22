package com.example.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.indexer.Indexer;

public class WebCrawler {
    private final Indexer indexer;
    private final int maxDepth;
    private int crawledCount = 0;
    private static final int MAX_PAGES = 10;
    private static Set<String> urlsToIndex = new HashSet<>();
    private final Set<String> visitedUrls = new HashSet<>();

    public WebCrawler(Indexer indexer, int maxDepth) {
        this.indexer = indexer;
        this.maxDepth = maxDepth;
    }

    public static void addUrlToIndex(String url) { urlsToIndex.add(url); }

    public void crawl(String startUrl) {
        boolean hasSuccessfulCrawl = false;
        try {
            // 直接爬取给定的URL
            if (crawlPage(startUrl)) {
                hasSuccessfulCrawl = true;
            }

            // 获取相关链接
            Document doc = Jsoup.connect(startUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)").timeout(10000).get();

            // 提取所有链接
            Elements links = doc.select("a[href]");

            for (int i = 0; i < Math.min(links.size(), MAX_PAGES - 1); i++) {
                String nextUrl = links.get(i).attr("abs:href");
                if (!nextUrl.isEmpty() && nextUrl.startsWith(startUrl)) {
                    if (crawlPage(nextUrl)) {
                        hasSuccessfulCrawl = true;
                    }
                    if (crawledCount >= MAX_PAGES) break;
                }
            }

            if (hasSuccessfulCrawl) {
                System.out.println("\n爬取完成！共爬取 " + crawledCount + " 个页面");
            }

        } catch (IOException e) {
            System.err.println("爬取失败: " + e.getMessage());
        }
    }

    public void waitForCompletion() {
        // 同步执行，无需等待
    }

    private boolean crawlPage(String url) {
        if (visitedUrls.contains(url) || crawledCount >= MAX_PAGES) {
            return false;
        }

        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)").timeout(10000).get();

            visitedUrls.add(url);
            String title = doc.title();

            // 提取更多有意义的内容
            StringBuilder content = new StringBuilder();
            content.append("标题: ").append(title).append("\n");
            content.append("网页内容: ").append(doc.body().text());

            // 存储完整内容
            String contentStr = content.toString();
            indexer.createIndex(contentStr, "webpage:" + url, "webpage");
            indexer.commit();

            crawledCount++;
            System.out.println("已成功索引第 " + crawledCount + " 个网页");
            System.out.println("网址: " + url);
            System.out.println("标题: " + title);
            System.out.println("--------------------");

            return true;

        } catch (IOException e) {
            System.err.println("爬取页面失败: " + url + "\n原因: " + e.getMessage());
            return false;
        }
    }
}