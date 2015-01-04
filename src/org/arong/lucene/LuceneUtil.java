package org.arong.lucene;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.search.IndexSearcher;

public class LuceneUtil {
	private static IndexWriter indexWriter = null;

	private static IndexSearcher indexSearcher = null;

	public static IndexWriter getIndexWriter() {
		// 获取indexWriter之前先关闭indexSearcher
		closeIndexSearcher();
		indexSearcher = null;
		return indexWriter;
	}

	public static IndexSearcher getIndexSearcher() {
		// 多线程控制
		if (indexSearcher == null) {
			synchronized (LuceneUtil.class) {
				if (indexSearcher == null) {
					try {
						indexSearcher = new IndexSearcher(
								Configuration.getDir());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return indexSearcher;
	}

	static {
		try {
			indexWriter = new IndexWriter(Configuration.getDir(),
					Configuration.getAnalyzer(), MaxFieldLength.LIMITED);
			// 程序关闭时释放资源
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					closeIndexWriter();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 关闭indexWriter
	private static void closeIndexWriter() {
		if (indexWriter != null) {
			try {
				indexWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// 关闭indexSearcher
	private static void closeIndexSearcher() {
		if (indexSearcher != null) {
			try {
				indexSearcher.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}