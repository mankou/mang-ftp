package mang.tools.ftp.download;

import java.util.List;
import java.util.Map;

public interface DownloadAfter {
	public void after(List<FtpDownloadInfo> downloadInfoList,Map<String,Object> contextMap);
}
