package LoginTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

//���������ȫ����jd����һ��
//����Ӧ�þ������ƵĴ�����ƻ���
//������delcart�жϻ���Ҫ����
public class Sn_simulator extends Config {

	// ��ʼ��������Դ
	private NetWork mnet;
	private HashMap<String, String> map;
	// ��һ��ר�ż����ı���
	private int cartItem;

	public Sn_simulator() {
		mnet = new NetWork();
		map = new HashMap<String, String>();
	}

	public void closeAll() {
		mnet.closeAll();
	}

	// �Ż����������ر���Ҫ
	// �߱�һ�����ݴ�����
	public boolean Sn_login(String uname, String passwd) {
		// �Ȼ��ҳ�棬Ȼ���ж��Ƿ���Ҫ��֤��
		System.out.println("sn begin to login");
		try {
			String url = "https://passport.suning.com/ids/login";
			String uuid = "";
			String verifyCode = "";
			// Ԥ��׼������Ҫpost������
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("jsonViewType", "true"));
			nvps.add(new BasicNameValuePair("username", uname));
			nvps.add(new BasicNameValuePair("password", passwd));
			nvps.add(new BasicNameValuePair("loginTheme", "b2c"));
			nvps.add(new BasicNameValuePair("loginChannel", "208000103000"));
			nvps.add(new BasicNameValuePair("rememberMe", "false"));
			HttpPost hp1 = new HttpPost(url);
			hp1.setEntity(new UrlEncodedFormEntity(nvps));
			Response resp1 = mnet.execPost(hp1);
			if (resp1.getState() == 0
					&& resp1.getResp().getStatusLine().getStatusCode() == 200) {
				String content1 = resp1.getContent();
//				System.out.println(content1);
				if (content1.contains("\"success\":true")) {

					System.out.println("login success");
					return true;
				} else if (content1.contains("\"needVerifyCode\":true")) {
					// System.out.println("yes here!");
					// ������Ҫ������֤��
					// ����Ҫ���»��ҳ�������Ŷ!
					HttpGet hg1 = new HttpGet(url);
					Response resp2 = mnet.execGet(hg1);
					if (resp2.getState() == 0
							&& resp2.getResp().getStatusLine().getStatusCode() == 200) {
						String content2 = resp2.getContent();
						// System.out.println("content2");
						// System.out.println(content2);
						// Parser ps1=new Parser(content2);
						uuid = Tool.getValByPattern(content2,
								"(?<=\\+ \").*(?=\" \\+ \"&yys)");
						System.out.println(uuid);
						String src = "https://vcs.suning.com/vcs/imageCode.htm?uuid="
								+ uuid + "&yys=" + (new Date()).getTime();
						System.out.println("��Ҫ������֤��");
						// System.out.println(src);
						VFrame vf1 = new VFrame(mnet, "sn", src, "��֤��");
						verifyCode = vf1.getText();
						// verifyCode=Tool.getScan().next();
						NameValuePair bp1 = new BasicNameValuePair(
								"verifyCode", verifyCode);
						NameValuePair bp2 = new BasicNameValuePair("uuid", uuid);
						nvps.add(bp1);
						nvps.add(bp2);
						hp1 = new HttpPost(url);
						hp1.setEntity(new UrlEncodedFormEntity(nvps));
						Response resp3 = mnet.execPost(hp1);
						if (resp3.getState() == 0
								&& resp3.getResp().getStatusLine()
										.getStatusCode() == 200) {
							String content3 = resp3.getContent();
							System.out.println("content3" + content3);
							if (content3.contains("\"success\":true")) {
								System.out.println("login success!");
								return true;
							} else {
								// �����֤��������
								while (content3.contains("badVerifyCode")) {
									// ˢ��һ������������»��һ����֤��
									System.out.println("��֤�����");
									// System.out.println("������ˢ����֤��!(ˢ�������)");
									// verifyCode=Tool.getScan().next();
									VFrame vf2 = new VFrame(mnet, "sn", src,
											"��֤�����");
									verifyCode = vf2.getText();
									nvps.remove(bp1);
									bp1 = new BasicNameValuePair("verifyCode",
											verifyCode);
									nvps.add(bp1);
									hp1 = new HttpPost(url);
									hp1.setEntity(new UrlEncodedFormEntity(nvps));
									resp3 = mnet.execPost(hp1);
									if (resp3.getState() == 0
											&& resp3.getResp().getStatusLine()
													.getStatusCode() == 200) {
										content3 = resp3.getContent();
										if (content3
												.contains("\"success\":true")) {
											System.out
													.println("login success!");
											return true;
										}
									} else {
										System.out.println("״̬�쳣!");
										break;
									}
								}
							}
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		System.out.println("login fail");
		return false;
	}

	//
	// ĿǰҲֻ��֧���ԼҾ�Ӫ�����
	public Result getCart() {
		System.out.println("sn begin to get cart");
		Result result = new Result();
		// mnet.printAllCookie();
		// ���Ӧ����Ϊ�˻��cookie
		String url1 = "http://cart.suning.com/webapp/wcs/stores/servlet/OrderItemDisplay?langId=-7&storeId=10052&catalogId=10051";
		String url2 = "http://cart.suning.com/webapp/wcs/stores/servlet/getMyCartItems?langId=-7&storeId=10052&catalogId=10051";
		HttpGet hg1 = new HttpGet(url1);
		HttpGet hg2 = new HttpGet(url2);
		try {
			Response resp1 = mnet.execGet(hg1);
			if (resp1.getState() == 0
					&& resp1.getResp().getStatusLine().getStatusCode() == 200) {
				System.out.println("�ɹ���ù��ﳵҳ��1");
				// System.out.println(content1);
				System.out.println("��ʼ��ù��ﳵҳ��2..");
				hg2.setHeader("Accept", "text/html, */*; q=0.01");
				hg2.setHeader("Accept-Encoding", "gzip, deflate, sdch");
				hg2.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
				hg2.setHeader("Cache-Control", "max-age=0");
				hg2.setHeader("Connection", "keep-alive");
				hg2.setHeader("Host", "cart.suning.com");
				hg2.setHeader(
						"Referer",
						"http://cart.suning.com/webapp/wcs/stores/servlet/OrderItemDisplay?langId=-7&storeId=10052&catalogId=10051");
				hg2.setHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.130 Safari/537.36");
				hg2.setHeader("X-Requested-With", "XMLHttpRequest");
				result.extype = "getcart";// 1
				result.httptype = "get";// 2
				result.shop = "sn";// 3
				result.url = url2;// 4
				result.vpn = getVpn();// 5
				result.filetype = "html";// 6
				result.ctime = new Date().getTime();// 7
				Response resp2 = mnet.execGet(hg2);
				if (resp2.getState() == 0
						&& resp2.getResp().getStatusLine().getStatusCode() == 200) {
					map.clear();
					System.out.println("�ɹ���ù��ﳵҳ��2");
					String content2 = resp2.getContent();
					// System.out.println(content2);
					// ��Ҫ�洢productId�Ͷ�Ӧ��itemId
					// �����Ҫ�洢��
					Parser parser = new Parser(content2);
					NodeFilter ft1 = new TagNameFilter("div");
					NodeList nl1 = parser.extractAllNodesThatMatch(ft1);
					NodeList nl2 = new NodeList();
					for (int i = 0; i < nl1.size(); i++) {
						Div div = (Div) nl1.elementAt(i);

						if (div.toHtml().contains("cart-goods-list")) {
							nl2.add(div);
						}
					}
					for (int i = 0; i < nl2.size(); i++) {
						// ���ĳ�������div��������productId��itemId,Ȼ��洢����
						Div dt = (Div) nl2.elementAt(i);
						parser = new Parser(dt.toHtml());
						NodeFilter ft6 = new HasAttributeFilter("name",
								"productId");
						NodeList nl3 = parser.extractAllNodesThatMatch(ft6);
						// productId
						InputTag it = (InputTag) nl3.elementAt(0);
						String id = it.getAttribute("value");
						int j = 0;
						while (id.charAt(j) == '0') {
							j++;
						}
						id = id.substring(j);
						// System.out.println(id);
						// itemId
						parser = new Parser(dt.toHtml());
						NodeFilter ft7 = new HasAttributeFilter("title", "ɾ��");
						NodeList nl4 = parser.extractAllNodesThatMatch(ft7);
						LinkTag lt = (LinkTag) nl4.elementAt(0);
						String cid = lt.getAttribute("onClick");
						cid = cid.substring(21).replaceAll("'\\)", "");
						// System.out.println(cid);
						map.put(id, cid);
					}
					System.out
							.println("there are " + map.size() + " products!");
					//���map���ݽ��в���
//					Iterator iterator = map.entrySet().iterator();
//					while(iterator.hasNext()){
//						Entry	entry = (Entry)iterator.next();
//						System.out.println(entry.getKey()+" "+entry.getValue());
//					}
					// ֻҪ��֤���ﳵ��������һ����Ʒ����ô�Ϳ��Ա�֤��ɹ���
					if (map.size() > 0) {
						System.out.println("get cart success");
						this.cartItem = map.size();
						result.status = 0;// 8
						result.filesize = resp2.getFileSize();// 9
						result.duration = resp2.getDuration();// 10
						result.ip = mnet.getIp();// 11
						result.ptime = mnet.getPing(result.ip);// 12
						return result;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("get cart fail");
		return result;
	}

	// Ҫ���ľ�����Ӧ�����Ʒ,����Ҫ����
	public Result addCart(String curl) {
		System.out.println("sn begin to add cart");
		// ������Ʒҳ����һЩ������Ϣ
		Result result = new Result();
		result.extype = "addcart";// 1
		result.shop = "sn";// 2
		result.httptype = "get";// 3
		result.vpn = getVpn();// 4
		result.filetype = "json";// 5
		try {
			Response resp1 = mnet.execGet(new HttpGet(curl));
			String content1 = "";
			if (resp1.getState() == 0
					&& resp1.getResp().getStatusLine().getStatusCode() == 200) {
				content1 = resp1.getContent();
			}
			// ֱ�ӵó�sn�ƺ���������,����ֻ��
			// ��Щ������Ҫ���ⷢ��������ܹ����
			String partnumber = Tool.getValByPattern(content1,
					"(?<=\"partNumber\":\")\\d+");
			// mnet.printAllCookie();

			String cityId = "9001";
			String districtId = "10008";
			String hurl = "http://www.suning.com/webapp/wcs/stores/ItemPrice/"
					+ partnumber + "__" + cityId + "_" + districtId
					+ "_1.html?callback=showSaleStatus";
			// System.out.println(hurl);
			HttpGet hhg = new HttpGet(hurl);
			Response hresp = mnet.execGet(hhg);
			// System.out.println("ok here!");
			String priceType = "0";
			String supplierCode = "";
			// System.out.println(hresp.getResp().getStatusLine().getStatusCode());
			if (hresp.getState() == 0
					&& hresp.getResp().getStatusLine().getStatusCode() == 200) {
				String hcontent = hresp.getContent();
				priceType = Tool.getValByPattern(hcontent,
						"(?<=\"priceType\":\")\\d+");
				supplierCode = Tool.getValByPattern(hcontent,
						"(?<=\"vendorCode\":\")\\d+");
			}
			String sellType = "";
			// ��������뻻����ʽ���
			Parser ps1 = new Parser(content1);
			NodeFilter nf1 = new TagNameFilter("input");
			NodeFilter nf2 = new HasAttributeFilter("id", "sellType");
			NodeFilter nf3 = new AndFilter(nf1, nf2);
			NodeList nl = ps1.extractAllNodesThatMatch(nf3);
			InputTag it1 = (InputTag) nl.elementAt(0);
			sellType = it1.getAttribute("value");
			/**
			 * �����promotionActiveId�Ǹ�X���أ���Щ��Ʒ����� ����û��ֵ��ʱ������Ӳ��ɹ��ģ�����
			 * */
			String promotionActiveId = "";
			String url = "http://cart.suning.com/emall/addMiniSoppingCart?"
					+ "callback=jQuery172037912692269310355_1443010438717&"
					+ "ERROEVIEW=miniShoppingCartView&"
					+ "URL=miniShoppingCartView&"
					+ "quantity=1&fullInventoryCheck=0&"
					+ "inventoryCheckType=0&" + "fullVoucherCheck=0&"
					+ "voucherCheckType=0&" + "inventoryRemoteCheck=0&"
					+ "voucherRemoteCheck=1&" + "storeId=10052&"
					+ "catalogId=10051&" + "orderId=.&" + "partnumber="
					+ partnumber + "&" + "sellType=" + sellType + "&"
					+ "supplierCode=" + supplierCode + "&" + "priceType="
					+ priceType + "&" + "promotionActiveId="
					+ promotionActiveId + "&" + "_=" + new Date().getTime();

			// System.out.println(url);
			HttpGet hg1 = new HttpGet(url);
			result.url = url;// 6
			result.ctime = new Date().getTime();// 7
			Response resp = mnet.execGet(hg1);
			if (resp.getState() == 0
					&& resp.getResp().getStatusLine().getStatusCode() == 200) {
				String content = resp.getContent();
				// System.out.println(content);
				if (content.contains("\"errorCode\":\"NO\"")) {
					System.out.println("add success!");
					this.cartItem++;
					result.status = 0;// 8
					result.filesize = resp.getFileSize();// 9
					result.duration = resp.getDuration();// 10
					result.ip = mnet.getIp();// 11
					result.ptime = mnet.getPing(result.ip);// 12
					return result;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("add fail");
		return result;
	}

	// ����ж�del cart�Ѿ��ɹ�����
	public Result delCart(String curl) {
		System.out.println("sn begin to del cart");
		int lastItem = this.cartItem;
		Result result = new Result();
		result.vpn = getVpn();// 1
		result.extype = "delcart";// 2
		result.httptype = "get";// 3
		result.shop = "sn";// 4
		result.filetype = "json";// 5
		String pid = Tool.getValByPattern(curl, "\\d+(?=\\.html)");
		String cid = map.get(pid);

		String url = "http://cart.suning.com/webapp/wcs/stores/servlet/SNCartOperationCmd?method=deleteItem&itemId="
				+ cid
				+ "&ts=1443169339976&callback=jQuery17207779537851456553_1443169320577&_=1443169339976";
		 result.url = url;// 6
		HttpGet hg1 = new HttpGet(url);
		try {
			result.ctime = new Date().getTime();// 7
			Response resp = mnet.execGet(hg1);
			if (resp.getState() == 0
					&& resp.getResp().getStatusLine().getStatusCode() == 200) {
				String content = resp.getContent();
//				System.out.println("content:" + content);
				if (content
						.contains("jQuery17207779537851456553_1443169320577()")) {
					// System.out.println(content);
					result.duration = resp.getDuration();// 8
					result.ip = mnet.getIp();// 9
					result.ptime = mnet.getPing(result.ip);// 10
					result.filesize = resp.getFileSize();// 11
					Result gres = getCart();
					if (gres.status == 0) {
						if (lastItem - this.cartItem == 1) {

							map.remove(cid);
							result.status = 0;
							System.out.println("delete success!");
							return result;
						}
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("delete false");
		return result;
	}

	public void loadCookie() {
		mnet.readCookie("sn");
	}

	public void saveCookie() {
		mnet.storeCookie("sn");
	}

	public static void main(String[] args) {

		String uname = "ge-jq@qq.com";
		String passwd = "123456..abc";
		Sn_simulator sns = new Sn_simulator();
		sns.setVpn(Result.vpn);
		String ip=Result.vpn.split("\\+")[1];
		Log.getDateTime("Begin_time:");
		Log.recordLogFile("\t\t"+Result.vpc+"\t"+new Ping().doPing(ip, 3)+"\n");
		// �ʼ����Ҫ��½
		sns.Sn_login(uname, passwd);
		Result result = null;
		result = sns.getCart();
		Log.recordLogFile("\t\t"+result.toString()+"\n");
		System.out.println("----------------------");
		System.out.println(result);
		System.out.println("----------------------");
		
		result = sns
				.addCart("http://product.suning.com/0070063869/106112109.html");
		Log.recordLogFile("\t\t"+result.toString()+"\n");
		System.out.println("----------------------");
		System.out.println(result);
		System.out.println("----------------------");
		
		//delcart֮ǰ������getcart
		result = sns.getCart();
		Log.recordLogFile("\t\t"+result.toString()+"\n");
		System.out.println("----------------------");
		System.out.println(result);
		System.out.println("----------------------");
		
		result = sns
				.delCart("http://product.suning.com/0070063869/106112109.html");
		Log.recordLogFile("\t\t"+result.toString()+"\n");
		Log.getDateTime("End_time:");
		System.out.println("----------------------");
		System.out.println(result);
		System.out.println("----------------------");
//		sns.getCart();
		sns.closeAll();
	}
}
