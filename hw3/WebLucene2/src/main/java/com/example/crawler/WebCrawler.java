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
        try {
            for (String baseUrl : urlsToIndex) {
                // 构建搜索URL
                String searchUrl = baseUrl + "/s?wd=" + java.net.URLEncoder.encode(keyword, "UTF-8");
                Document searchDoc = Jsoup.connect(searchUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)").timeout(10000).get();

                // 提取搜索结果链接
                Elements searchResults = searchDoc.select("div.result h3.t a");

                for (int i = 0; i < Math.min(searchResults.size(), MAX_PAGES); i++) {
                    String resultUrl = searchResults.get(i).attr("href");
                    crawlPage(resultUrl);
                    if (crawledCount >= MAX_PAGES) break;
                }
            }
        } catch (IOException e) {
            System.err.println("搜索失败: " + e.getMessage());
        }
    }

    public void waitForCompletion() {
        // 同步执行，无需等待
    }

    private void crawlPage(String url) {
        if (visitedUrls.contains(url) || crawledCount >= MAX_PAGES) {
            return;
        }

        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)").timeout(10000).get();

            visitedUrls.add(url);
            String title = doc.title();
            StringBuilder content = new StringBuilder();

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

        } catch (IOException e) {
            System.err.println("爬取页面失败: " + url + "\n原因: " + e.getMessage());
        }
    }
}