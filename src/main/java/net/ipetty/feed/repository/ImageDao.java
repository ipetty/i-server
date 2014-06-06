package net.ipetty.feed.repository;

import net.ipetty.feed.domain.Image;

/**
 * ImageDao
 * 
 * @author luocanfeng
 * @date 2014年5月8日
 */
public interface ImageDao {

	/**
	 * 保存图片信息
	 */
	public void save(Image image);

	/**
	 * 根据ID获取图片信息
	 */
	public Image getById(Long id);

	/**
	 * 删除图片
	 */
	public void delete(Long id);

}
