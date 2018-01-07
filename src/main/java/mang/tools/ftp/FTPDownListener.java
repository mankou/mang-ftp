package mang.tools.ftp;

public interface FTPDownListener {
	public void afterDownload(String remoteFilePath,String localDirpath);
	
	public int  getDownloadCount();
}
