package net.ipetty.feed.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import net.ipetty.core.context.UserContext;
import net.ipetty.core.context.UserPrincipal;
import net.ipetty.core.exception.BusinessException;
import net.ipetty.core.service.BaseService;
import net.ipetty.core.util.ImageUtils;
import net.ipetty.feed.domain.Image;
import net.ipetty.feed.repository.ImageDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * ImageService
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
@Service
@Transactional
public class ImageService extends BaseService {

	@Resource
	private ImageDao imageDao;

	/**
	 * 保存图片文件
	 */
	public Image save(MultipartFile file, String webContextRealPath, Integer author, int authorUid) {
		try {
			Assert.notNull(file, "图片文件不能为空");
			Assert.notNull(author, "发图人不能为空");
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
		Assert.notNull(image, "图片对象不能为空");
		Assert.notNull(image.getCreatedBy(), "发图人不能为空");
		imageDao.save(image);
	}

	/**
	 * 根据ID获取图片信息
	 */
	public Image getById(Long id) {
		Assert.notNull(id, "ID不能为空");
		return imageDao.getById(id);
	}

	/**
	 * 删除图片
	 */
	public void delete(Image image) {
		UserPrincipal principal = UserContext.getContext();
		if (principal == null || image.getCreatedBy() == null || !image.getCreatedBy().equals(principal.getId())) {
			throw new BusinessException("只能删除自己的图片");
		}
		imageDao.delete(image.getId());
	}

	/**
	 * 获取指定主题消息列表的所有图片信息
	 */
	public List<Image> listByFeedIds(Long... feedIds) {
		return imageDao.listByFeedIds(feedIds);
	}

}
