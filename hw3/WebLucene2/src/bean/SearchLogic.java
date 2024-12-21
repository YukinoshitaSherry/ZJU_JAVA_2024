package bean;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * SearchLogic.java
 * 
 * @version 1.0
 * @createTime Lucene���ݿ����
 */
public class SearchLogic {
	private static Connection conn = null;// ���ݿ����Ӷ���
	private static Statement stmt = null;
	private static ResultSet rs = null;
	private final String searchDir = "C:\\index";// �����ļ�����·��
	private static File indexFile = null;// �����ļ�
	private static Searcher searcher = null;// ����
	private static Analyzer analyzer = null;// �ִ�
	/** ����ҳ�滺�� */
	private final int maxBufferedDocs = 500;

	/**
	 * ��ȡ���ݿ�����
	 * 
	 * @return ResultSet
	 * @throws Exception
	 */
	public List<SearchBean> getResult(String queryStr) throws Exception {
		List<SearchBean> result = null;
		conn = JdbcUtil.getConnection();
		if (conn == null) {
			throw new Exception("���ݿ�����ʧ�ܣ�");
		}
		String sql = "select *   from content";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);// ��ѯ���ݿ�����
			this.createIndex(rs); // �����ݿⴴ������,�˴�ִ��һ�Σ���Ҫÿ�����ж������������Ժ������и��¿��Ժ�̨���ø�������
			TopDocs topDocs = this.search(queryStr);// ����
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			result = this.addHits2List(scoreDocs);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("���ݿ��ѯsql���� sql : " + sql);
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
		return result;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getJSON(String keyword) throws Exception {
		SearchLogic logic = new SearchLogic();
		// �õ��������
		List<SearchBean> result = logic.getResult(keyword.toLowerCase());
		
		List list = new ArrayList();
		for (int i = 0; i < result.size(); i++) {
			// ѭ��������������뵽list��
			HashMap dynBean = new HashMap();
			dynBean.put("id", result.get(i).getId());
			dynBean.put("content", result.get(i).getContent());
			dynBean.put("title", result.get(i).getTitle());
			dynBean.put("link", result.get(i).getLink());
			list.add(dynBean);
		}
		System.out.println("���ҵ���" + list.size());
		if (list.size() > 0) {
			// ��ʽ����json��ʽ�������ǰ̨
			JSONArray json = JSONArray.fromObject(list);
			return json.toString();
		} else {
			return "[]";
		}

	}

	/**
	 * Ϊ���ݿ�������ݴ�������
	 * 
	 * @param rs
	 * @throws Exception
	 */
	private void createIndex(ResultSet rs) throws Exception {
		Directory directory = null;
		IndexWriter indexWriter = null;

		try {
			// �����ļ�����
			indexFile = new File(searchDir);
			// ����������򴴽�
			if (!indexFile.exists()) {
				indexFile.mkdir();
			}
			// �������ļ�
			directory = FSDirectory.open(indexFile);
			analyzer = new IKAnalyzer();// �ִ�

			indexWriter = new IndexWriter(directory, analyzer, true,
					IndexWriter.MaxFieldLength.UNLIMITED);
			indexWriter.setMaxBufferedDocs(maxBufferedDocs);
			Document doc = null;
			while (rs.next()) {
				// �����ĵ�
				doc = new Document();
				// ��������
				Field link = new Field("link", String.valueOf(rs
						.getString("link")), Field.Store.YES,
						Field.Index.NOT_ANALYZED, TermVector.NO);
				// ���±���
				Field title = new Field("title",
						rs.getString("title") == null ? "" : rs
								.getString("title"), Field.Store.YES,
						Field.Index.ANALYZED, TermVector.NO);
				// ��������
				Field content = new Field("content",
						rs.getString("content") == null ? "" : rs
								.getString("content"), Field.Store.YES,
						Field.Index.ANALYZED, TermVector.NO);

				doc.add(link);// ������������
				doc.add(title);// �������±���
				doc.add(content);// ������������
				indexWriter.addDocument(doc);
			}

			indexWriter.optimize();
			indexWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��������
	 * 
	 * @param queryStr
	 * @return
	 * @throws Exception
	 */
	private TopDocs search(String queryStr) throws Exception {
		if (searcher == null) {
			indexFile = new File(searchDir);
			// �������ļ�
			searcher = new IndexSearcher(FSDirectory.open(indexFile));
		}
		// searcher.setSimilarity(new IKSimilarity());
		QueryParser parser = new QueryParser(Version.LUCENE_29, "content",
				new IKAnalyzer());
		// Query query = parser.parse(queryStr);
		Query query = new TermQuery(new Term("content", queryStr));
		TopDocs topDocs = searcher.search(query, null, 100);// ����ƥ�䵽��ǰ100����¼
		return topDocs;
	}

	/**
	 * ���ؽ������ӵ�List��
	 * 
	 * @param scoreDocs
	 * @return
	 * @throws Exception
	 */
	private List<SearchBean> addHits2List(ScoreDoc[] scoreDocs)
			throws Exception {
		List<SearchBean> listBean = new ArrayList<SearchBean>();
		SearchBean bean = null;
		// ������������list
		for (int i = 0; i < scoreDocs.length; i++) {
			int docId = scoreDocs[i].doc;
			Document doc = searcher.doc(docId);
			bean = new SearchBean();
			bean.setId(doc.get("id"));
			bean.setLink(doc.get("link"));
			bean.setTitle(doc.get("title"));
			bean.setContent(doc.get("content"));
			listBean.add(bean);
		}
		return listBean;
	}

}