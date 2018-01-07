package mang.tools.ftp.download.after;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import mang.tools.ftp.download.DownloadAfter;
import mang.tools.ftp.download.FtpDownloadInfo;
import mang.util.common.FileUtil;
import mang.util.ftp.ftp4j.FTPTool;

@Component
public class RemoveFtpFileAfter implements DownloadAfter {
	private static final Logger log = LoggerFactory.getLogger(RemoveFtpFileAfter.class);

	private boolean isRemoveFTPFile = true;

	@Value("${ftp.remoteRemovePath:remove}")
	private String remoteRemovePath;

	@Override
	public void after(List<FtpDownloadInfo> downloadInfoList, Map<String, Object> paraMap) {
		if (isRemoveFTPFile) {
			removeFile(downloadInfoList, paraMap);
		} else {
			log.info("远程不删除文件");
		}
	}

	public void removeFile(List<FtpDownloadInfo> downloadInfoList, Map<String, Object> contextMap) {
		FTPTool ftptool = (FTPTool) contextMap.get("ftptool");
		if (downloadInfoList != null && downloadInfoList.size() > 0) {
			for (FtpDownloadInfo downloadInfo : downloadInfoList) {
				String remotePath = downloadInfo.getRemotePath();
				String removePath = getRemovePath(remotePath, contextMap);
				log.info("移除文件{} 到 {}", new Object[] { remotePath, removePath });
				ftptool.rename(remotePath, removePath);
			}
		}
	}

	public String getRemovePath(String remotePath, Map<String, Object> contextMap) {
		String proSn = (String) contextMap.get("proSn");
		String fileName = FileUtil.getFileName(remotePath);
		String removePath = remoteRemovePath + "/" + proSn + "/" + fileName;
		return removePath;
	}

	public boolean isRemoteFTPFile() {
		return isRemoveFTPFile;
	}

	public void setRemoteFTPFile(boolean isRemoteFTPFile) {
		this.isRemoveFTPFile = isRemoteFTPFile;
	}

	public String getRemoteRemovePath() {
		return remoteRemovePath;
	}

	public void setRemoteRemovePath(String remoteRemovePath) {
		this.remoteRemovePath = remoteRemovePath;
	}

}
