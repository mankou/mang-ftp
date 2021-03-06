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

import it.sauronsoftware.ftp4j.FTPClient;
import mang.tools.ftp.FTP4jTool;
import mang.tools.ftp.FTPConfig;
import mang.tools.ftp.FTPTool;
import mang.tools.ftp.download.DownloadListener;
import mang.tools.ftp.download.FTPDownloadProcessor;
import mang.tools.ftp.download.FtpDownloadInfo;
import mang.tools.ftp.sn.SnBuild;
import mang.util.common.FileUtil;

/**
 * FTP下载框架代码简单实现类
 */
@Component
public class SimpleFTPDownloadProcessor implements FTPDownloadProcessor {
	private static final Logger log = LoggerFactory.getLogger(SimpleFTPDownloadProcessor.class);

	@Autowired
	private FTPConfig ftpConfig;

	private FTPTool ftptool;

	private boolean isLogin = false;

	private Map<String, Object> contextMap;

	private List<DownloadListener> downloadListenerList = new ArrayList<DownloadListener>();

	@Autowired
	@Qualifier("simpleDateSn")
	private SnBuild snBuild;

	private String proSn;
	
	@Override
	public void init() {
		this.proSn = this.generateProSn();
		log.info("proSn:" + proSn);
		log.info(this.getFtpConfig().toString());
		contextMap = new HashMap<String, Object>();

		String localPath = this.getFtpConfig().getLocalPath();
		// 注 如果 localPath是 /home/ 其并不认为 c:/home/ 针对这种情况不报错，但也没有创建目录
		FileUtil.forceMkdir(localPath);
	}
	
	@Override
	public boolean login() {
		String host = this.getFtpConfig().getHost();
		String userName = this.getFtpConfig().getUserName();
		String password = this.getFtpConfig().getPassword();
		if (ftptool == null) {
			ftptool = new FTP4jTool(host, userName, password);
		}
		isLogin = ftptool.login();
		return isLogin;
	}

	@Override
	public List<FtpDownloadInfo> download() {
		// 希望是通用的 与业务无关的
		List<FtpDownloadInfo> receiveFileList = new ArrayList<FtpDownloadInfo>();
		String remotePath = this.getFtpConfig().getRemotePath();
		String localPath = this.getFtpConfig().getLocalPath();
		Boolean passive = this.getFtpConfig().getPassive();
		String filter = this.getFtpConfig().getFilter();

		log.info("download file start,FTP path:{}", remotePath + "/" + filter);
		if (isLogin) {
			boolean ispassive = ftptool.isPassive();
			FTPClient client=(FTPClient) ftptool.getClient();
//			Long autoNoopTimeout=client.getAutoNoopTimeout();
//			log.info("pass mode:{}, autoNoopTimeout:{}",new Object[]{ispassive,autoNoopTimeout});
			
			log.info("pass mode:{}",new Object[]{ispassive});
			
			List<String> fileList = ftptool.listPathFileName(remotePath, filter);
			int fileCount=fileList.size();
			log.info("get {} files",new Object[]{fileCount});

			contextMap.put("ftptool", ftptool);
			contextMap.put("proSn", proSn);
			contextMap.put("ftpConfig", ftpConfig);

			
			for (int i=0;i<fileList.size();i++) {
				String fileName=fileList.get(i);
				String ftpFilePath = remotePath + "/" + fileName;
				Map<String, Object> listenerPara = new HashMap<String, Object>();
				listenerPara.putAll(contextMap);
				listenerPara.put("source", ftpFilePath);
				listenerPara.put("target", localPath);
				
				
				beforeDownloadListener(listenerPara);

				boolean downloadResult=ftptool.downloadFile(ftpFilePath, localPath);
				if(downloadResult){
					log.info("download file{} ok {} ", new Object[]{i+1,ftpFilePath});					
				}

				FtpDownloadInfo downloadInfo = new FtpDownloadInfo();
				downloadInfo.setRemotePath(ftpFilePath);
				downloadInfo.setLocalPath(localPath);
				receiveFileList.add(downloadInfo);

				afterDownloadListener(listenerPara);
			}

		}

		log.info("download file end,total download {} files", receiveFileList.size());
		return receiveFileList;
	}
	
	@Override
	public void logout() {
		ftptool.logout();
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
				downloadListener.beforeDownload(listenerPara);
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

	/**
	 * 生成运行时流水号
	 */
	public String generateProSn() {
		String sn = snBuild.getSn("R");
		return sn;
	}

	public List<DownloadListener> getDownloadListener() {
		return downloadListenerList;
	}

	public void setDownloadListenerList(List<DownloadListener> downloadListenerList) {
		this.downloadListenerList = downloadListenerList;
	}
	
	

}
