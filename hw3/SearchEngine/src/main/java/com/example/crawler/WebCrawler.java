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

    public void crawl(String keyword) {
        boolean hasSuccessfulCrawl = false;
        try {
            for (String url : urlsToIndex) {
                // 直接爬取给定的URL
                if (crawlPage(url)) {
                    hasSuccessfulCrawl = true;
                }

                // 获取相关链接
                Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)").timeout(10000).get();

                // 提取所有链接
                Elements links = doc.select("a[href]");

                for (int i = 0; i < Math.min(links.size(), MAX_PAGES - 1); i++) {
                    String nextUrl = links.get(i).attr("abs:href");
                    if (!nextUrl.isEmpty() && nextUrl.contains(keyword.toLowerCase())) {
                        if (crawlPage(nextUrl)) {
                            hasSuccessfulCrawl = true;
                        }
                        if (crawledCount >= MAX_PAGES) break;
                    }
                }
            }

            if (hasSuccessfulCrawl) {
                System.out.println("\n爬取完成！如果搜索结果未找到匹配内容，可能是因为：");
                System.out.println("1. 关键词不在已爬取的页面内容中");
                System.out.println("2. 需要调整搜索关键词");
                System.out.println("3. 尝试爬取其他相关网页\n");
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
            StringBuilder content = new StringBuilder();

            Elements mainContent = doc.select("body");
            content.append(mainContent.text());

            indexer.createIndex("标题: " + title + "\n内容: " + content.toString(), "webpage:" + url, "webpage");
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