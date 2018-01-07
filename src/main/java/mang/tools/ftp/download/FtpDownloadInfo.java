package mang.tools.ftp.download;

public class FtpDownloadInfo {
	
	private String remotePath;
	private String localPath;
	public String getRemotePath() {
		return remotePath;
	}
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
}
