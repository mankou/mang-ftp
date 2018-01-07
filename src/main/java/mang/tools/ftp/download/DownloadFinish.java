package mang.tools.ftp.download;

import java.util.List;
import java.util.Map;

public interface DownloadFinish {
	
	public void finish(List<FtpDownloadInfo> downloadInfoList,Map<String,Object> contextMap);
}
