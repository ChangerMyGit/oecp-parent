/************************* 版权声明 *********************************
 * 
 * 版权所有：百洋软件
 * Copyright (c) 2008 by Pearl Ocean.
 * 
 ************************* 变更记录 *********************************
 *
 * 创建者：yongtree   创建日期： 2008-11-4
 * 创建记录：创建类结构。
 * 
 * 修改者：       修改日期：
 * 修改记录：
 ************************* 随   笔 *********************************
 *
 * 这里可以写写感想，感慨，疑问什么的。
 * 
 ******************************************************************
 */

package oecp.framework.page;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;
import org.apache.struts2.components.Component;
import org.apache.struts2.dispatcher.StrutsRequestWrapper;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 构建分页标签体的类
 * 
 * @author yongtree
 * @date 2008-11-4 下午02:52:19
 * @version 1.0
 */
public class Pages extends Component {
	private HttpServletRequest request;
	private String pageNo;
	private String total;
	private String styleClass;
	private String theme;
	private String url;
	private String urlType;

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Pages(ValueStack arg0, HttpServletRequest request) {
		super(arg0);
		this.request = request;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean end(Writer writer, String body) {
		boolean result = super.start(writer);
		try {
			// 从ValueStack中取出数值
			Object obj = this.getStack().findValue(pageNo);
			pageNo = String.valueOf((Integer) obj);
			obj = this.getStack().findValue(total);
			total = String.valueOf((Integer) obj);

			StringBuilder str = new StringBuilder();
			Map cont = this.getStack().getContext();
			StrutsRequestWrapper req = (StrutsRequestWrapper) cont
					.get(StrutsStatics.HTTP_REQUEST);
			if (url == null || "".equals(url)) {
				url = (String) req
						.getAttribute("javax.servlet.forward.request_uri");

			}
			String pageNoStr = "?pageNo=";
			if (url != null && !"dir".equals(url)) {
				if (url.indexOf("?") != -1) {
					pageNoStr = "&pageNo=";
				} else {
					pageNoStr = "?pageNo=";
				}
			}
			if ("dir".equals(urlType)) {// 当url的类型为目录类型时，比如http://localhost:8090/yongtree/page/1
				pageNoStr = "";
				if (url.lastIndexOf("/page/") > -1) {
					url = url.substring(0, url.indexOf("/"));
				} else if(url.charAt(url.length()-1)=='/'){
					url = url + "page/";
				}else{
					url = url + "/page/";
				}
			}

			StringBuffer perUrl = new StringBuffer("");
			if (this.getParameters().size() != 0) {
				Iterator iter = this.getParameters().keySet().iterator();
				if ("dir".equals(urlType)) {
					perUrl.append("?");
				}
				while (iter.hasNext()) {
					String key = (String) iter.next();
					Object o = this.getParameters().get(key);
					if (perUrl == null || "".equals(perUrl.toString())) {
						perUrl.append("&");
					} else {
						if (perUrl != null
								&& (!"".equals(perUrl.toString()))
								&& !(perUrl
										.toString()
										.charAt(
												(perUrl.toString().length() == 0 ? 1
														: perUrl.toString()
																.length()) - 1) == '?')) {
							perUrl.append("&");
						}
					}
					perUrl.append(key).append("=").append(o);
				}
			}

			// perUrl.append(paramStr.toString());

			Integer cpageInt = Integer.valueOf(pageNo);
			str.append("<span ");
			if (styleClass != null) {
				str.append(" class='" + styleClass + "'>");
			} else {
				str.append(">");
			}

			// 文本样式
			if ("text".equals(theme)) {
				// 当前页与总页数相等
				if (pageNo.equals(total)) {
					// 如果total = 1，则无需分页，显示“[第1页] [共1页]”
					if ("1".equals(total)) {
						str.append("[第 " + pageNo + " 页]");
						str.append(" [共 " + total + " 页]");
					} else {
						// 到达最后一页,显示“[首页] [上一页] [末页]”
						str.append("<a href='" + url + pageNoStr + "1" + perUrl
								+ "'>[首页]</a> ");
						str.append("<a href='" + url + pageNoStr
								+ (cpageInt - 1) + perUrl + "'>[上一页]</a>");
						str.append(" <a href='" + url + pageNoStr + total
								+ perUrl + "'>[末页]</a> ");
					}
				} else {
					// 当前页与总页数不相同
					if ("1".equals(pageNo)) {
						// 第一页，显示“[首页] [下一页] [末页]”
						str.append("<a href='" + url + pageNoStr + "1" + perUrl
								+ "'>[首页]</a>");
						str.append("<a href='" + url + pageNoStr
								+ (cpageInt + 1) + perUrl + "'>[下一页]</a>");
						str.append("<a href='" + url + pageNoStr + total
								+ perUrl + "'>[末页]</a>");
					} else {
						// 不是第一页，显示“[首页] [上一页] [下一页] [末页]”
						str.append("<a href='" + url + pageNoStr + "1" + perUrl
								+ "'>[首页]</a>");
						str.append("<a href='" + url + pageNoStr
								+ (cpageInt - 1) + perUrl + "'>[上一页]</a>");
						str.append("<a href='" + url + pageNoStr
								+ (cpageInt + 1) + perUrl + "'>[下一页]</a>");
						str.append("<a href='" + url + pageNoStr + total
								+ perUrl + "'>[末页]</a>");
					}
				}
			} else if ("number".equals(theme)) { // 数字样式 [1 2 3 4 5 6 7 8 9
				// 10 > >>]
				Integer totalInt = Integer.valueOf(total);

				// 如果只有一页，则无需分页
				str.append("[ ");
				if (totalInt == 1) {
					str.append("<strong>1</strong> ");
				} else {
					if (cpageInt > 1) {
						// 当前不是第一组，要显示“<< <”
						// <<：返回前一组第一页
						// <：返回前一页
						str.append("<a href='" + url + pageNoStr + "1" + perUrl
								+ "'>«</a> ");
						str.append("<a href='" + url + pageNoStr
								+ (cpageInt - 1) + perUrl);
						str.append("'>‹</a> ");
					} else {
						str.append("« ‹ ");
					}

					int v = (cpageInt - 4) > 0 ? (cpageInt - 4) : 1;
					int v1 = (cpageInt + 4) < totalInt ? (cpageInt + 4)
							: totalInt;
					if (v1 == totalInt) {
						v = totalInt - 10;
						v = (v <= 0 ? 1 : v); // 如果为负数，则修改为1
					} else if (v == 1 && v1 < totalInt) {
						v1 = totalInt > 10 ? 10 : totalInt;
					}
					// 10个为一组显示
					for (int i = v; i <= v1; i++) {
						if (cpageInt == i) { // 当前页要加粗显示
							str.append("<strong>" + i + "</strong> ");
						} else {
							// str.append("<a href='"+url + i +perUrl+"'>" + i +
							// "</a> ");
							str.append("<a href='" + url + pageNoStr + i
									+ perUrl + "'>" + i + "</a> ");
						}
					}
					// 如果多于1组并且不是最后一组，显示“> >>”
					if (cpageInt < totalInt) {
						// >>：返回下一组最后一页
						// >：返回下一页
						str.append("<a href='" + url + pageNoStr
								+ (cpageInt + 1) + perUrl);
						str.append("'>›</a> ");
						str.append("<a href='" + url + pageNoStr + totalInt
								+ perUrl);
						str.append("'>»</a> ");
					} else {
						str.append("› » ");
					}
				}
				str.append("]");
			} else {
				Integer totalInt = Integer.valueOf(total);

				// 如果只有一页，则无需分页
				str.append(" ");
				if (totalInt > 1) {
					if (cpageInt > 1) {
						// 当前不是第一组，要显示“<< <”
						// <<：返回前一组第一页
						// <：返回前一页
						str.append("<span class='listPage_normal'><a href='"
								+ url + pageNoStr + "1" + perUrl
								+ "'>首页</a></span> ");
						str.append("<span class='listPage_normal'><a href='"
								+ url + pageNoStr + (cpageInt - 1) + perUrl);
						str.append("'>上一页</a></span> ");
					} else {
						str
								.append("<span class='listPage_curpage'><a> 首页</a></span> <span class='listPage_curpage'><a> 上一页</a></span> ");
					}

					int v = (cpageInt - 4) > 0 ? (cpageInt - 4) : 1;
					int v1 = (cpageInt + 4) < totalInt ? (cpageInt + 4)
							: totalInt;
					if (v1 == totalInt) {
						v = totalInt - 10;
						v = (v <= 0 ? 1 : v); // 如果为负数，则修改为1
					} else if (v == 1 && v1 < totalInt) {
						v1 = totalInt > 10 ? 10 : totalInt;
					}
					// 10个为一组显示
					for (int i = v; i <= v1; i++) {
						if (cpageInt == i) { // 当前页要加粗显示
							str
									.append("<span class='listPage_curpage'><strong><a>"
											+ i + "</a></strong></span> ");
						} else {
							// str.append("<a href='"+url + i +perUrl+"'>" + i +
							// "</a> ");
							str
									.append("<span class='listPage_normal'><a href='"
											+ url
											+ pageNoStr
											+ i
											+ perUrl
											+ "'>" + i + "</a> </span>");
						}
					}
					// 如果多于1组并且不是最后一组，显示“> >>”
					if (cpageInt < totalInt) {
						// >>：返回下一组最后一页
						// >：返回下一页
						str.append("<span class='listPage_normal'><a href='"
								+ url + pageNoStr + (cpageInt + 1) + perUrl);
						str.append("'>下一页</a></span> ");
						str.append("<span class='listPage_normal'><a href='"
								+ url + pageNoStr + totalInt + perUrl);
						str.append("'>末页</a></span> ");
					} else {
						str
								.append("<span class='listPage_curpage'> <a >下一页</a></span> <span class='listPage_curpage'><a> 末页</a></span> ");
					}
				}
				str.append("");
			}
			str.append("</span>");

			writer.write(str.toString());

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
