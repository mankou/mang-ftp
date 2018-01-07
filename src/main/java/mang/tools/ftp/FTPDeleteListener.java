package mang.tools.ftp;

public interface FTPDeleteListener {
	
	/**
	 * 文件删除后的处理操作
	 * */
	public void afterDeleteFile(String deletePath);
}
