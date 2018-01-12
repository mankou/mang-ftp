package mang.tools.ftp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("ftpConfig")
public class FTPConfig {
	/**
	 * FTP主机名
	 * */
	@Value("${ftp.host:127.0.0.1}")
	private String host;
	
	/**
	 * FTP 端口
	 * */
	@Value("${ftp.port:21}")
	private int port;
	
	/**
	 * 用户名
	 * */
	@Value("${ftp.userName:maning}")
	private String userName;
	
	/**
	 * 密码
	 * */
	@Value("${ftp.password:1}")
	private String password;
	
	/**
	 * FTP 字符编码
	 * */
	@Value("${ftp.charset:GBK}")
	private String charset;
	
	/**
	 * FTP 远程xml存放路径
	 * */
	@Value("${ftp.remotePath:passinfo}")
	private String remotePath;
	
	
	/**
	 * FTP 远程xml下载后 移除的路径
	 * */
	@Value("${ftp.isRemoveFTPFile:true}")
	private boolean isRemoveFTPFile;
	
	/**
	 * FTP 远程xml下载后 移除的路径
	 * */
	@Value("${ftp.remoteRemovePath:remove}")
	private String remoteRemovePath;
	
	/**
	 * FTP 本地下载路径
	 * */
	@Value("${ftp.localPath:c:/test/receive/}")
	private String localPath;
	
	
	/**
	 * 本地解析xml后的路径
	 * */
	@Value("${ftp.localRemovePath:c:/test/remove/}")
	private String localRemovePath;
	
	
	/**
	 * 删除FTP上N天前的文件
	 * */
	@Value("${ftpDeleteNdays:30}")
	private int ftpDeleteNdays;
	
	/**
	 * FTP主动被动模式
	 * 默认主动模式
	 * */
	@Value("${ftp.passive:false}")
	private Boolean passive;
	
	
	/**
	 * ftp文件名过滤 
	 * */
	@Value("${filter:}")
	private String filter;
	
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public String getRemotePath() {
		return remotePath;
	}
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	public String getRemoteRemovePath() {
		return remoteRemovePath;
	}
	public void setRemoteRemovePath(String remoteRemovePath) {
		this.remoteRemovePath = remoteRemovePath;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	
	public String getLocalRemovePath() {
		return localRemovePath;
	}
	public void setLocalRemovePath(String localRemovePath) {
		this.localRemovePath = localRemovePath;
	}
	
	public int getFtpDeleteNdays() {
		return ftpDeleteNdays;
	}
	public void setFtpDeleteNdays(int ftpDeleteNdays) {
		this.ftpDeleteNdays = ftpDeleteNdays;
	}
	
	public boolean isRemoveFTPFile() {
		return isRemoveFTPFile;
	}
	public void setRemoveFTPFile(boolean isRemoveFTPFile) {
		this.isRemoveFTPFile = isRemoveFTPFile;
	}
	
	
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public Boolean getPassive() {
		return passive;
	}
	public void setPassive(Boolean passive) {
		this.passive = passive;
	}
	
	
	@Override
	public String toString() {
		return "FTPConfig [host=" + host + ", port=" + port + ", userName=" + userName
				+ ", charset=" + charset + ", remotePath=" + remotePath + ", isRemoveFTPFile=" + isRemoveFTPFile
				+ ", remoteRemovePath=" + remoteRemovePath + ", localPath=" + localPath + ", localRemovePath="
				+ localRemovePath + ", ftpDeleteNdays=" + ftpDeleteNdays + ", passive=" + passive + ", filter=" + filter
				+ "]";
	}
	

}
