package mang.tools.ftp.download;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * FTP下载框架代码
 * 使用: 用于组织FTP下载框架代码的核心逻辑
 * */
@Component
public class FTPDownloadSkeleton {
	private static final Logger log = LoggerFactory.getLogger(FTPDownloadSkeleton.class);
	
	//这里不能自动注入了 应该用配置文件注入 以后好修改具体的实现类
//	@Autowired
	private FTPDownloadProcessor downloadProcessor;
	
	private List<DownloadAfter> downloadAfterList = new ArrayList<DownloadAfter>();
	
	private List<DownloadFinish> downloadFinishList=new ArrayList<DownloadFinish>();
	
	public void run() {
		downloadProcessor.init();
		boolean isLogIn=downloadProcessor.login();
		
		if(isLogIn){
			//XXX 另关于 FTPConfig 你最好能写活了
			//XXX 下载这块这样返回好么? 我也不知道 如果返回的Map则比较复杂
			List<FtpDownloadInfo> downloadInfoList = downloadProcessor.download();
			Map<String,Object> contextMap=downloadProcessor.getContextMap();
			
			this.after(downloadInfoList, contextMap);
			
			downloadProcessor.logout();
			
			this.finish(downloadInfoList, contextMap);				
		}else{
			log.error("login error,do nothing");
		}
	}
	
	
	public void after(List<FtpDownloadInfo> downloadInfoList,Map<String,Object> contextMap){
		if(downloadAfterList!=null && downloadAfterList.size()>0){
			for (DownloadAfter downloadAfter : downloadAfterList) {
				downloadAfter.after(downloadInfoList,contextMap);
			}
		}
	}
	
	public void finish(List<FtpDownloadInfo> downloadInfoList,Map<String,Object> contextMap){
		if(downloadFinishList!=null && downloadFinishList.size()>0){
			for(DownloadFinish downloadFinsh:downloadFinishList){
				try{
					downloadFinsh.finish(downloadInfoList, contextMap);					
				}catch (Exception e) {
					log.info("execute finish error", e);
				}
			}
		}
	}
	
	public void addDownloadAfter(DownloadAfter downloadAfter) {
		this.downloadAfterList.add(downloadAfter);
	}
	
	public void addDownloadFinsh(DownloadFinish downloadFinish){
		this.downloadFinishList.add(downloadFinish);
	}

	public FTPDownloadProcessor getDownloadProcessor() {
		return downloadProcessor;
	}

	public void setDownloadProcessor(FTPDownloadProcessor downloadProcessor) {
		this.downloadProcessor = downloadProcessor;
	}

	public List<DownloadAfter> getDownloadAfterList() {
		return downloadAfterList;
	}

	public void setDownloadAfterList(List<DownloadAfter> downloadAfterList) {
		this.downloadAfterList = downloadAfterList;
	}

	public List<DownloadFinish> getDownloadFinishList() {
		return downloadFinishList;
	}

	public void setDownloadFinishList(List<DownloadFinish> downloadFinishList) {
		this.downloadFinishList = downloadFinishList;
	}
	
}
