package mang.tools.ftp.download.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import mang.tools.ftp.FTP4jTool;
import mang.tools.ftp.FTPConfig;
import mang.tools.ftp.FTPTool;
import mang.tools.ftp.download.DownloadListener;
import mang.tools.ftp.download.FTPDownloadProcessor;
import mang.tools.ftp.download.FtpDownloadInfo;
import mang.tools.ftp.sn.SnBuild;
import mang.util.common.FileUtil;



@Component
public class SimpleFTPDownloadProcessor implements FTPDownloadProcessor {
	private static final Logger log = LoggerFactory.getLogger(SimpleFTPDownloadProcessor.class);

	@Autowired
	@Qualifier("simpleDateSn")
	private SnBuild snBuild;

	private String proSn;

	@Autowired
	private FTPConfig ftpConfig;

	private Map<String, Object> contextMap;

	private List<DownloadListener> downloadListenerList = new ArrayList<DownloadListener>();
	
	private FTPTool ftptool;
	
	private boolean isLogin=false;

	@Override
	public void init() {
		this.proSn = snBuild.getSn("R");
		log.info("运行流水号:" + proSn);
		log.info(this.getFtpConfig().toString());
		contextMap = new HashMap<String, Object>();
		
		String localPath = this.getFtpConfig().getLocalPath();
		//注  如果 localPath是  /home/  其并不认为 c:/home/ 针对这种情况不报错，但也没有创建目录
		FileUtil.forceMkdir(localPath);
	}

	@Override
	public List<FtpDownloadInfo> download() {
		// 希望是通用的 与业务无关的
		List<FtpDownloadInfo> receiveFileList = new ArrayList<FtpDownloadInfo>();
		String remotePath = this.getFtpConfig().getRemotePath();
		String localPath = this.getFtpConfig().getLocalPath();
		Boolean passive = this.getFtpConfig().getPassive();
		String filter = this.getFtpConfig().getFilter();

		log.info("下载xml文件开始,FTP路径:{}", remotePath + "/" + filter);
		if (isLogin) {
			boolean ispassive = ftptool.isPassive();
			log.info("pass mode:" + ispassive);
			List<String> fileList = ftptool.listPathFileName(remotePath, filter);
			
			contextMap.put("ftptool", ftptool);
			contextMap.put("proSn", proSn);
			contextMap.put("ftpConfig", ftpConfig);
			
			for (String fileName : fileList) {
				String ftpFilePath = remotePath + "/" + fileName;
				Map<String, Object> listenerPara = new HashMap<String, Object>();
				beforeDownloadListener(listenerPara);
				
				ftptool.downloadFile(ftpFilePath, localPath);
				log.info("download {} ok", ftpFilePath);
				
				
				FtpDownloadInfo downloadInfo = new FtpDownloadInfo();
				downloadInfo.setRemotePath(ftpFilePath);
				downloadInfo.setLocalPath(localPath);
				receiveFileList.add(downloadInfo);
				
				afterDownloadListener(listenerPara);
			}
		
		}

		log.info("下载xml文件结束,总计下载{}个文件", receiveFileList.size());
		return receiveFileList;
	}

	@Override
	public void addDownloadListener(DownloadListener listener) {
		downloadListenerList.add(listener);
	}

	@Override
	public Map<String, Object> getContextMap() {
		if (contextMap == null) {
			contextMap = new HashMap<String, Object>();
		}
		return contextMap;
	}

	private void beforeDownloadListener(Map<String, Object> listenerPara) {
		if (downloadListenerList != null && downloadListenerList.size() > 0) {
			for (DownloadListener downloadListener : downloadListenerList) {
				downloadListener.afterDownload(listenerPara);
			}
		}
	}

	private void afterDownloadListener(Map<String, Object> listenerPara) {
		if (downloadListenerList != null && downloadListenerList.size() > 0) {
			for (DownloadListener downloadListener : downloadListenerList) {
				downloadListener.afterDownload(listenerPara);
			}
		}
	}

	public String getProSn() {
		return proSn;
	}

	public FTPConfig getFtpConfig() {
		return ftpConfig;
	}

	public void setFtpConfig(FTPConfig ftpConfig) {
		this.ftpConfig = ftpConfig;
	}

	@Override
	public void login() {
		String host = this.getFtpConfig().getHost();
		String userName = this.getFtpConfig().getUserName();
		String password = this.getFtpConfig().getPassword();
		if(ftptool==null){
			ftptool = new FTP4jTool(host, userName, password);			
		}
		isLogin=ftptool.login();
	}

	@Override
	public void logout() {
		ftptool.logout();
	}

}
