package net.ipetty.feed.service;

import javax.annotation.Resource;

import net.ipetty.feed.domain.Image;
import net.ipetty.feed.repository.ImageDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ImageService
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
@Service
@Transactional
public class ImageService {

	@Resource
	private ImageDao imageDao;

	/**
	 * 保存位置信息
	 */
	public void save(Image image) {
		imageDao.save(image);
	}

	/**
	 * 根据ID获取位置信息
	 */
	public Image getById(Long id) {
		return imageDao.getById(id);
	}

}
