package net.ipetty.feed.service;

import java.io.IOException;

import javax.annotation.Resource;

import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.util.ImageUtils;
import net.ipetty.feed.domain.Image;
import net.ipetty.feed.repository.ImageDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	 * 保存图片文件
	 */
	public Image save(MultipartFile file, String webContextRealPath, Integer author, int authorUid) {
		try {
			Image image = ImageUtils.saveImage(file, webContextRealPath, author, authorUid);
			imageDao.save(image);
			return image;
		} catch (IOException e) {
			throw new BusinessException("保存图片时出错", e);
		}
	}

	/**
	 * 保存图片信息
	 */
	public void save(Image image) {
		imageDao.save(image);
	}

	/**
	 * 根据ID获取图片信息
	 */
	public Image getById(Long id) {
		return imageDao.getById(id);
	}

}
