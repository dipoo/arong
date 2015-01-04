package org.arong.net;

import java.io.IOException;

public class GetRealUrl {
	private String realUrl;
	private String testURL;

	public GetRealUrl(final String url) {
		if (url != null && !"".equals(url)) {
			this.testURL = url.trim();
			if (this.testURL.startsWith("http://")) {
				this.testURL = this.testURL.substring(7);
			}
			if (this.testURL.startsWith("https://")) {
				this.testURL = this.testURL.substring(8);
			}
		} else {
			throw new RuntimeException("url is NULL!");
		}
	}

	/**
	 * ����ָ�����ӽ�������ַ���ֵ
	 * 
	 * @param searchURL
	 * @param anchor
	 * @param trail
	 * @return
	 */
	private static String getLinks(final String searchURL, final String anchor,
			final String trail) {
		String count = "0";
		String serverResponse;
		try {
			// �ҹ���ɫ����
			if (searchURL.startsWith("http://www.soso.com")) {
				// �������ݵ�gb2312ͬ־(-_-||)
				serverResponse = SimpleWebClient.getRequestHttp(searchURL,
						"gb2312");
			} else if (searchURL.startsWith("http://www.google.com.hk")) {
				serverResponse = SimpleWebClient.getRequestHttp(searchURL,
						"iso-8859-1");
			} else {
				serverResponse = SimpleWebClient.getRequestHttp(searchURL);
			}
		} catch (IOException e) {
			serverResponse = e.getMessage();
		}
		// System.out.println(serverResponse.length());
		if (serverResponse.equals("ERROR")) {
			count = "<font color=red>��ѯ��ʱ</font>";
		} else {
			int pos = serverResponse.indexOf(anchor);
			if (pos > 1) {
				serverResponse = serverResponse
						.substring(pos + anchor.length());
				pos = serverResponse.indexOf(trail);
				String value = serverResponse.substring(0, pos).trim();
				count = value;
			}
		}
		return count;
	}

	public String getReal360Url(String url) {
		try {
			realUrl = ""
					+ getLinks(url, "<a href=\"/share",
							"\" class=\"download-enabled\"></a>");
		} catch (Exception ex) {
			realUrl = ex.getMessage();
		}
		return realUrl;
	}

	public String getRealBaiduUrl(String url) {
		try {
			realUrl = ""
					+ getLinks(url, "http://www.baidupcs.com/file",
							"\" id=\"downFileButtom\">");
		} catch (Exception ex) {
			realUrl = ex.getMessage();
		}
		return realUrl.replaceAll("&amp;", "&");
	}
}
