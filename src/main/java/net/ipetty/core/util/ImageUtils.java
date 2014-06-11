package net.ipetty.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.ipetty.core.exception.BusinessException;
import net.ipetty.feed.domain.Image;

import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片处理工具类
 * 
 * @author luocanfeng
 * @date 2014年5月9日
 */
public class ImageUtils {

	/** 允许的图片类型 */
	private static final Set<String> ALLOWED_IMAGE_TYPE = new HashSet<String>();

	private static final String ORGINAL_IMAGE_FILE_PREFIX = "l";
	private static final String SMALL_IMAGE_FILE_PREFIX = "m";
	// private static final String CUT_IMAGE_FILE_PREFIX = "s";

	private static final String FILE_SEPARATOR = "/";

	private static final String ROOT_FILE_RELATIVE_PATH = "/files";

	private static final int WIDTH_MAX = 300;
	private static final int HEIGHT_MAX = 400;

	private static final String JPG = "jpg";
	private static final String JPEG = "jpeg";
	private static final String GIF = "gif";
	private static final String PNG = "png";

	static {
		ALLOWED_IMAGE_TYPE.add(JPG);
		ALLOWED_IMAGE_TYPE.add(JPEG);
		ALLOWED_IMAGE_TYPE.add(GIF);
		ALLOWED_IMAGE_TYPE.add(PNG);
	}

	/**
	 * 保存图片文件并返回Image对象
	 */
	public static Image saveImage(MultipartFile file, String webContextRealPath, Integer author, int authorUid)
			throws IOException {
		ImageUtils.verifyImageType(file);

		String originalFilename = file.getOriginalFilename();
		String originalFileExtension = ImageUtils.getFileExtension(originalFilename);
		// gif的缩略图设置为jpg
		String thumbnailFileExtension = GIF.equalsIgnoreCase(originalFileExtension) ? JPG : originalFileExtension;
		String shortUuid = UUIDUtils.generateShortUUID();

		Image image = new Image();
		image.setCreatedBy(author);
		image.setOriginalURL(new StringBuilder(ROOT_FILE_RELATIVE_PATH).append(FILE_SEPARATOR).append(authorUid)
				.append(FILE_SEPARATOR).append(ORGINAL_IMAGE_FILE_PREFIX).append(shortUuid).append(".")
				.append(originalFileExtension).toString());
		image.setSmallURL(new StringBuilder(ROOT_FILE_RELATIVE_PATH).append(FILE_SEPARATOR).append(authorUid)
				.append(FILE_SEPARATOR).append(SMALL_IMAGE_FILE_PREFIX).append(shortUuid).append(".")
				.append(thumbnailFileExtension).toString());
		image.setCutURL(image.getSmallURL()); // 据说客户端不需要裁剪的正方形图；有需要的话再截图。

		File originalImageFile = new File(webContextRealPath + image.getOriginalURL());
		ImageUtils.makeDirsIfNotExists(originalImageFile);
		file.transferTo(originalImageFile);

		File smallImageFile = new File(webContextRealPath + image.getSmallURL());
		ImageUtils.makeDirsIfNotExists(smallImageFile);
		ImageUtils.resizeImage(originalImageFile, WIDTH_MAX, HEIGHT_MAX, smallImageFile);

		return image;
	}

	/**
	 * 保存图片文件并返回图片文件存放的相对地址
	 */
	public static String saveImageFile(MultipartFile file, String webContextRealPath, int authorUid) throws IOException {
		ImageUtils.verifyImageType(file);

		String originalFilename = file.getOriginalFilename();
		String fileExtension = ImageUtils.getFileExtension(originalFilename);
		String shortUuid = UUIDUtils.generateShortUUID();

		String imageURL = new StringBuilder(ROOT_FILE_RELATIVE_PATH).append(FILE_SEPARATOR).append(authorUid)
				.append(FILE_SEPARATOR).append(ORGINAL_IMAGE_FILE_PREFIX).append(shortUuid).append(".")
				.append(fileExtension).toString();

		File imageFile = new File(webContextRealPath + imageURL);
		ImageUtils.makeDirsIfNotExists(imageFile);
		file.transferTo(imageFile);

		return imageURL;
	}

	private static void makeDirsIfNotExists(File file) {
		File folder = file.getParentFile();
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	/**
	 * 校验图片的文件名后缀是否合法
	 */
	private static void verifyImageType(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		String fileExtension = getFileExtension(originalFilename);
		if (StringUtils.isEmpty(fileExtension) || !ALLOWED_IMAGE_TYPE.contains(fileExtension)) {
			throw new BusinessException("仅支持jpg、jpeg、gif、png格式的图片");
		}
	}

	/**
	 * 获取文件名后缀
	 */
	private static String getFileExtension(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
	}

	/**
	 * 根据给定图片文件及宽高限制，生成指定图片文件。
	 */
	private static void resizeImage(File originalImage, int destWidth, int destHeight, File destImage)
			throws IOException {
		BufferedImage bufferedImage = ImageIO.read(new FileInputStream(originalImage));
		if (bufferedImage.getWidth() <= destWidth && bufferedImage.getHeight() <= destHeight) {
			// 如果图片够小，则不进行拉伸
			// Thumbnails.of(bufferedImage).scale(1).toFile(destImage);
			FileUtils.copyFile(originalImage, destImage);
		} else {
			Thumbnails.of(bufferedImage).size(destWidth, destHeight).toFile(destImage);
		}
	}

	public static void main(String[] args) throws IOException {
		String originalFile = "C:\\Users\\Administrator\\Desktop\\test.gif";
		BufferedImage originalImage = ImageIO.read(new FileInputStream(originalFile));
		System.out.println(originalImage.getWidth() + " × " + originalImage.getHeight());
		Thumbnails.of(originalFile).scale(1).toFile("C:\\Users\\Administrator\\Desktop\\test.jpg");
		Thumbnails.of(originalFile).size(300, 300).toFile("C:\\Users\\Administrator\\Desktop\\test_300.jpg");
		Thumbnails.of(originalFile).size(600, 600).toFile("C:\\Users\\Administrator\\Desktop\\test_600.jpg");
		Thumbnails.of(originalFile).size(800, 800).toFile("C:\\Users\\Administrator\\Desktop\\test_800.jpg");
	}

}
