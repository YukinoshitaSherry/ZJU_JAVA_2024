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
        TopDocs hits = searcher.search(query, 20);

        List<SearchResult> results = new ArrayList<>();
        Set<String> addedPaths = new HashSet<>();

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            String path = doc.get("path");

            if (!addedPaths.contains(path)) {
                addedPaths.add(path);
                String content = doc.get("content");
                if (content != null && !content.trim().isEmpty()) {
                    results.add(new SearchResult(path, content, doc.get("mimeType"), scoreDoc.score));
                }
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
            if (filePath.startsWith("webpage:")) {
                String url = filePath.substring(7);
                String[] lines = content.split("\n");
                StringBuilder preview = new StringBuilder();

                String title = "";
                String mainContent = "";

                for (String line : lines) {
                    if (line.startsWith("标题:")) {
                        title = line.substring(3).trim();
                    } else if (line.startsWith("内容:")) {
                        mainContent = line.substring(3).trim();
                        break;
                    }
                }

                preview.append("网页标题: ").append(title).append("\n");
                preview.append("网页地址: ").append(url).append("\n");
                if (!mainContent.isEmpty()) {
                    preview.append("相关内容: ").append(mainContent.substring(0, Math.min(mainContent.length(), 150))).append("...");
                }

                return preview.toString();
            } else {
                // 本地文件的显示逻辑保持不变
                return content.substring(0, Math.min(content.length(), 200)) + (content.length() > 200 ? "..." : "");
            }
        }
    }
}