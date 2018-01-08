package mang.tools.ftp.download.finish;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import mang.tools.ftp.FTPConfig;
import mang.tools.ftp.download.DownloadFinish;
import mang.tools.ftp.download.FtpDownloadInfo;
import mang.util.common.FileUtil;

/**
 * 清除本地下载的文件
 * */
@Component
public class CleanLocalDownloadXml implements DownloadFinish {
	private static final Logger log = LoggerFactory.getLogger(CleanLocalDownloadXml.class);

	@Override
	public void finish(List<FtpDownloadInfo> downloadInfoList, Map<String, Object> contextMap) {
		log.info("clean local download file ...");
		FTPConfig ftpConfig=(FTPConfig) contextMap.get("ftpConfig");
		String localPath=ftpConfig.getLocalPath();
		FileUtil.cleanDir(localPath);
	}

}
