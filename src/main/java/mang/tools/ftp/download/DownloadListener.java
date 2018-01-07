package mang.tools.ftp.download;

import java.util.Map;

public interface DownloadListener {
	
	public void beforeDownload(Map<String,Object> listenerPara);
	
	public void afterDownload(Map<String,Object> listenerPara);
}
