package com.suma.venus.resource.task;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;

@Component
public class BundleHeartBeatService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleHeartBeatService.class);

	// 检测设备心跳动作的频率
	private final long freqTime = 5 * 1000;

	@Autowired
	BundleDao bundleDao;

	// 记录系统中设备的最后心跳信息
	private final ConcurrentHashMap<String, Long> bunldeStatusMap = new ConcurrentHashMap<String, Long>();

	private ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

	private ScheduledFuture<?> t;

	public ConcurrentHashMap<String, Long> getBunldeStatusMap() {
		return bunldeStatusMap;
	}

	public void removeBundleStatus(String bundle_ip) {
		LOGGER.info("remove heartBeat， bundle_ip=" + bundle_ip);

		bunldeStatusMap.remove(bundle_ip);

		// TODO 设备离线，更新数据库status
		List<BundlePO> bundlePOs = bundleDao.findByDeviceIp(bundle_ip);

		if (!CollectionUtils.isEmpty(bundlePOs)) {
			BundlePO bundlePO = bundlePOs.get(0);
			bundlePO.setOnlineStatus(ONLINE_STATUS.OFFLINE);
			bundleDao.save(bundlePO);
		}

		if (bunldeStatusMap.size() == 0 && t != null) {
			// TODO
			t.cancel(true);
		}

	}

	public void addBundleStatus(String bundle_ip, Long currentTime) {

		LOGGER.info("add=" + bundle_ip + ",time=" + currentTime);

		boolean threadFlag = false;

		if (bunldeStatusMap.size() == 0) {
			threadFlag = true;
		}

		if (bunldeStatusMap.get(bundle_ip) == null) {
			// TODO 设备从离线变为上线，更新数据库status
			List<BundlePO> bundlePOs = bundleDao.findByDeviceIp(bundle_ip);

			if (CollectionUtils.isEmpty(bundlePOs)) {
				LOGGER.info("cannot find bundlePO, return");
				return;
			}

			BundlePO bundlePO = bundlePOs.get(0);

			bundlePO.setOnlineStatus(ONLINE_STATUS.ONLINE);
			bundleDao.save(bundlePO);

		}

		bunldeStatusMap.put(bundle_ip, currentTime);

		// TODO 判断是否需要启动线程
		if (threadFlag) {
			LOGGER.info("new thread");
			BundleHeartBeatMonitorThread thread = new BundleHeartBeatMonitorThread(this);
			t = pool.scheduleAtFixedRate(thread, freqTime, freqTime, TimeUnit.MILLISECONDS);
		}
	}
}