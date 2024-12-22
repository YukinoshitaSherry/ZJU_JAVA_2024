package com.example.searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        TopDocs hits = searcher.search(query, 10); // 返回前10个结果

        List<SearchResult> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            results.add(new SearchResult(doc.get("path"), doc.get("content"), scoreDoc.score));
        }
        return results;
    }

    // 搜索结果类
    public static class SearchResult {
        private final String filePath;
        private final String content;
        private final float score;

        public SearchResult(String filePath, String content, float score) {
            this.filePath = filePath;
            this.content = content;
            this.score = score;
        }

        // Getters
        public String getFilePath() { return filePath; }
        public String getContent() { return content; }
        public float getScore() { return score; }
    }
}