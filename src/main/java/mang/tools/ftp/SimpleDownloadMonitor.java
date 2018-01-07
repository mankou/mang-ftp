package mang.tools.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleDownloadMonitor implements FTPDownListener {
	private static final transient Logger log = LoggerFactory.getLogger(SimpleDownloadMonitor.class);
	
	private List<String> downloadFileRemotePath=new ArrayList<String>();
	private List<String> downloadFileName=new ArrayList<String>();
	
	@Override
	public void afterDownload(String remoteFilePath,String localDirpath) {
		log.info("download {} ok",remoteFilePath);
		downloadFileRemotePath.add(remoteFilePath);
		
		File file=new File(remoteFilePath);
		String fileNames=file.getName();
		downloadFileName.add(fileNames);
	}

	@Override
	public int getDownloadCount() {
		return downloadFileRemotePath.size();
	}

	public List<String> getDownloadFileRemotePath() {
		return downloadFileRemotePath;
	}

	public List<String> getDownloadFileName() {
		return downloadFileName;
	}
	
}
