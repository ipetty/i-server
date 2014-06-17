package net.ipetty.feed.repository;

import java.util.List;

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

	/**
	 * 获取指定主题消息列表的所有图片信息
	 */
	public List<Image> listByFeedIds(Long... feedIds);

}
