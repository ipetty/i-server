package net.ipetty.feed.domain;

import net.ipetty.core.domain.LongIdEntity;

/**
 * 图片
 * 
 * @author luocanfeng
 * @date 2014年4月30日
 */
public class Image extends LongIdEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 5144903326769461299L;

	private String smallURL; // 缩略图
	private String cutURL; // 裁剪图
	private String originalURL; // 原图

	public Image() {
		super();
	}

	public Image(Integer createdBy, String smallURL, String cutURL, String originalURL) {
		super();
		super.setCreatedBy(createdBy);
		this.smallURL = smallURL;
		this.cutURL = cutURL;
		this.originalURL = originalURL;
	}

	public String getSmallURL() {
		return smallURL;
	}

	public void setSmallURL(String smallURL) {
		this.smallURL = smallURL;
	}

	public String getCutURL() {
		return cutURL;
	}

	public void setCutURL(String cutURL) {
		this.cutURL = cutURL;
	}

	public String getOriginalURL() {
		return originalURL;
	}

	public void setOriginalURL(String originalURL) {
		this.originalURL = originalURL;
	}

}
