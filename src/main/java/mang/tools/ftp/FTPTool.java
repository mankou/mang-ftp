package mang.tools.ftp;

import java.util.List;

import mang.tools.ftp.download.DownloadListener;

public interface FTPTool {
	
	/**
	 * 登录
	 * */
	public boolean login();
	
	/**
	 * 登出
	 * */
	public void logout();
	
	
	/**
	 * 列出当前目录文件名
	 * */
	public List<String> listNames();
	
	/**
	 * 根据传入的路径或通配符 列出文件名
	 * @param fileSpec 路径名
	 * 例1:out/*.txt 列出out目录下的txt文件 <br>
	 * 例2:out 列出out目录下的所有文件
	 * */
	public List<String> listNames(String fileSpec);
	
	/**
	 * 根据传入的路径或通配符 列出文件
	 * @param fileSpec 路径
	 * @return 返回file类型
	 * */
	public Object list(String fileSpec);
	
	/**
	 * 获取某一目录下的文件名
	 * @param dirPath 目录路径
	 * @param filter 文件名过滤 如果不过滤传入空
	 * */
	public List<String> listPathFileName(String dirPath,String filter);
	
	
	/**
	 * 取得客户端
	 * */
	public Object getClient();
	
	
	/**
	 * 下载文件
	 * @param remoteFileName 远程文件路径
	 * @param localFilePath 本地文件路径 如果以/结尾 则认为是目录 则本地文件名与远程文件名一致 如果不是/结尾 则认为是文件 则本地文件名以该参数为准
	 * @return boolean 
	 * */
	public boolean downloadFile(String remoteFileName,String localFilePath);
	
	/**
	 * 下载目录下的文件
	 * 从ftp上下载文件夹下的文件到本地路径下,注是文件夹下的文件,不包含该文件夹
	 * @param remoteDirPath 远程文件夹路径
	 * @param localDirPath 本地文件夹路径
	 * */
	public void downloadDir(String remoteDirPath,String localDirPath);
	
	
	/**
	 * 递归下载
	 * */
	public void downloadRecursive(String remoteDirPath,String fileSpec,String localDirpath,DownloadListener downListener);
	
	
	
	/**
	 * 移动文件
	 * @param oldPath 旧路径
	 * @param newPath 新路径
	 * */
	public void rename(String oldPath,String newPath);
	
	
	/**
	 * 创建目录
	 * @param directoryName 目录名 可以是级联形式  如a/b/c,也可以是单个的目录名 如a
	 * */
	public boolean createDirectory(String directoryName);
	
	/**
	 * 判断目录是否存在
	 * @param directoryName 目录名
	 * */
	public boolean existDirectory(String directoryName);
	
	
	/**
	 * 删除目录
	 * */
	public void deleteDirectory(String directoryName,FTPDeleteListener deleteListener);
	
	/**
	 * 删除文件
	 * */
	public void deleteFile(String fileName);
	
	
	/**
	 * 设置主动 被动模式
	 * */
	public void setPassive(boolean passive);
	
	
	/**
	 * 获取主动 被动模式
	 * */
	public boolean isPassive();
	
	
}
