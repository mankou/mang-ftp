package mang.tools.ftp.download.finish;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import mang.tools.copy.copyfile.CopyFile;
import mang.tools.copy.copyfile.SimpleCopy;
import mang.tools.ftp.FTPConfig;
import mang.tools.ftp.download.DownloadFinish;
import mang.tools.ftp.download.FtpDownloadInfo;


/**
 * 本地留存文件
 * 按运行流水号备份文件
 * */
@Component
public class LocalBakeFileByProSn implements DownloadFinish {
	private static final Logger log = LoggerFactory.getLogger(LocalBakeFileByProSn.class);

	@Value("${copyFile.localDest.remove:}")
	private String destDir;
	
	@Override
	public void finish(List<FtpDownloadInfo> downloadInfoList, Map<String, Object> contextMap) {
		CopyFile copyFile=new SimpleCopy();
		FTPConfig ftpConfig=(FTPConfig) contextMap.get("ftpConfig");
		String source=ftpConfig.getLocalPath();
		String proSn=(String) contextMap.get("proSn");
		String realDest=destDir+"/"+proSn;
		log.info("[localBakFileByProSn] source:{} dest:{}",new Object[]{source,realDest});
		copyFile.addSource(source);
		copyFile.addDest(realDest);
		copyFile.doCopy();
	}

}
