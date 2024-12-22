package com.example.searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Searcher {
    private final IndexSearcher searcher;
    private final QueryParser parser;
    private static final String INDEX_DIR = "lucene_index";

    public Searcher() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        DirectoryReader reader = DirectoryReader.open(dir);
        searcher = new IndexSearcher(reader);
        parser = new QueryParser("content", new StandardAnalyzer());
    }

    public List<SearchResult> search(String queryString) throws Exception {
        Query query = parser.parse(queryString);
        TopDocs hits = searcher.search(query, 10);

        List<SearchResult> results = new ArrayList<>();
        Set<String> addedPaths = new HashSet<>(); // 用于跟踪已添加的文件路径

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            String path = doc.get("path");

            // 检查文件路径是否已经添加过
            if (!addedPaths.contains(path)) {
                addedPaths.add(path);
                results.add(new SearchResult(path, doc.get("content"), doc.get("mimeType"), scoreDoc.score));
            }
        }
        return results;
    }

    // 搜索结果类
    public static class SearchResult {
        private final String filePath;
        private final String content;
        private final String mimeType;
        private final float score;

        public SearchResult(String filePath, String content, String mimeType, float score) {
            this.filePath = filePath;
            this.content = content;
            this.mimeType = mimeType != null ? mimeType : "未知类型";
            this.score = score;
        }

        public String getFilePath() { return filePath; }
        public String getMimeType() { return mimeType; }
        public float getScore() { return score; }

        public String getPreview() {
            if (content == null || content.isEmpty()) {
                return "无预览内容";
            }

            String cleanContent = content.replaceAll("<[^>]*>", "").replaceAll("\\s+", " ").trim();

            // 区分网页和本地文件的显示
            if (filePath.startsWith("webpage:")) {
                String url = filePath.substring(7); // 移除"webpage:"前缀
                String[] lines = cleanContent.split("\n");
                StringBuilder preview = new StringBuilder();
                preview.append("网页URL: ").append(url).append("\n");

                // 提取并显示有意义的内容
                for (String line : lines) {
                    if (line.length() > 10 && !line.startsWith("URL:")) { // 跳过太短的行和URL行
                        preview.append(line.substring(0, Math.min(line.length(), 100)));
                        preview.append("...\n");
                        break;
                    }
                }
                return preview.toString();
            } else {
                // 本地文件的显示逻辑保持不变
                int previewLength = Math.min(cleanContent.length(), 200);
                return cleanContent.substring(0, previewLength) + (cleanContent.length() > 200 ? "..." : "");
            }
        }
    }
}