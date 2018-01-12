package mang.tools.ftp.download;

import java.util.List;
import java.util.Map;

public interface FTPDownloadProcessor {
	
	/**
	 * 初始化
	 * */
	public void init();
	
	/**
	 * 登录
	 * */
	public boolean login();
	
	
	/**
	 * 下载
	 * */
	public List<FtpDownloadInfo> download();
	
	/**
	 * 添加下载监听器
	 * 用于在下载文件时做自己一逻辑
	 * */
	public void addDownloadListener(DownloadListener listener);
	
	/**
	 * 获取下载监听器
	 * */
	public List<DownloadListener> getDownloadListener();
	
	
	/**
	 * 获取下载过程中的上下文信息.
	 * 如下载路径等 方便后续处理
	 * */
	public Map<String,Object> getContextMap();
	
	
	/**
	 * FTP登出
	 * */
	public void logout();
	
}
