package net.ipetty.user.service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import net.ipetty.core.service.BaseService;
import net.ipetty.user.repository.UidDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Uid工具类，使用此类时一定要使用 synchronized 锁，例如
 * <code>synchronized (uidService) {...}</code>
 * 
 * @author luocanfeng
 * @date 2014年5月7日
 */
@Service
@Transactional
public class UidService extends BaseService {

	@Resource
	private UidDao uidDao;

	private static final Set<Integer> uids = new LinkedHashSet<Integer>(); // 所有可用Uids
	private static final int batchInsertNum = 50; // 每次生成Uid的个数
	private static final int randomRange = 1000; // 生成随机数的范围值
	private static final int poolLowWarningNum = 20; // Uid池个数过低警报阀值
	private int randomFrom = 10001; // 默认随机数起始值

	@PostConstruct
	public void init() {
		// init uids
		this.reloadAvailableUids();

		// init seed below
		Integer maxUid = uidDao.getMaxUid();
		if (maxUid != null) {
			randomFrom = maxUid + 1;
		}
	}

	/**
	 * 获取一个可用的Uid
	 */
	public synchronized int getUid() {
		if (CollectionUtils.isEmpty(uids)) {
			this.produceUids();
		}
		return uids.iterator().next();
	}

	/**
	 * 标记给定Uid为已使用。
	 */
	public synchronized void markAsUsed(int uid) {
		uidDao.markAsUsed(uid);
		uids.remove(uid);

		// 发现池里较少则补充 TODO 改为异步实现
		if (uids.size() <= poolLowWarningNum) {
			produceUids();
		}
	}

	/**
	 * 批量生产Uid
	 */
	public synchronized void produceUids() {
		Set<Integer> randomUids = new HashSet<Integer>(batchInsertNum);
		this.randomUids(randomFrom, randomRange, batchInsertNum, randomUids); // 生成随机Uids
		uidDao.batchInsert(randomUids); // 插入随机生成的Uids
		this.reloadAvailableUids(); // 重新加载可用Uids
		randomFrom = randomFrom + randomRange; // 更新随机字段
	}

	/**
	 * 重新加载可用的Uids
	 */
	private void reloadAvailableUids() {
		List<Integer> all = uidDao.listAllAvailable();
		uids.clear();
		uids.addAll(all);
	}

	/**
	 * 在给定范围内 [min, min + range) 生成指定个数 (n) 的随机数
	 */
	private void randomUids(int min, int range, int n, Set<Integer> resultSet) {
		for (int i = 0; i < n; i++) {
			int num = (int) (Math.random() * range) + min;
			resultSet.add(num);
		}

		int setSize = resultSet.size();
		if (setSize < n) { // 递归
			randomUids(min, range, n - setSize, resultSet);
		}
	}

}
