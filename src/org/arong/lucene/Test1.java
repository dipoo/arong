//package org.arong.lucene;
//
//import java.util.Date;
//
//import org.apache.lucene.document.Document;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.queryParser.QueryParser;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.util.Version;
//import org.junit.Test;
//
//public class Test1 {
//
//	@Test
//	public void test1() throws Exception {
//		IndexWriter indexWriter = LuceneUtil.getIndexWriter();
//		User user = new User(2, "张绍荣", "框架", new Date());
//		indexWriter.addDocument(DocumentUtil.pojo2Document(user));
//		indexWriter.commit();
//	}
//
//	@Test
//	public void test2() {
//		IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
//		QueryParser parser = new QueryParser(Version.LUCENE_30, "password",
//				Configuration.getAnalyzer());
//		try {
//			Query query = parser.parse("框架");
//			Document document = indexSearcher.doc(1);
//			System.out.println(DocumentUtil.document2Pojo(document,User.class).getName());
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//}
