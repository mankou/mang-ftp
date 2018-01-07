package mang.tools.ftp.download;

import java.util.List;
import java.util.Map;

public interface FTPDownloadProcessor {
	
	/**
	 * 初始化
	 * */
	public void init();
	
	public void login();
	
	
	
	/**
	 * 下载
	 * */
	public List<FtpDownloadInfo> download();
	
	
	public void addDownloadListener(DownloadListener listener);
	
	
	public Map<String,Object> getContextMap();
	
	
	public void logout();
	
}
