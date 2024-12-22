package com.example.indexer;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Indexer {
    private final IndexWriter writer;
    private static final String INDEX_DIR = "SearchEngine_index";

    public Indexer() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        config.setRAMBufferSizeMB(256.0);
        writer = new IndexWriter(dir, config);
    }

    public void createIndex(String content, String filePath, String mimeType) throws IOException {
        Document doc = new Document();

        // 存储文件路径
        doc.add(new StringField("path", filePath, Field.Store.YES));

        // 存储文件类型
        doc.add(new StringField("mimeType", mimeType, Field.Store.YES));

        // 存储文件内容
        doc.add(new TextField("content", content, Field.Store.YES));

        // 添加到索引
        writer.addDocument(doc);
    }

    public void close() throws IOException { writer.close(); }

    public void commit() throws IOException { writer.commit(); }
}