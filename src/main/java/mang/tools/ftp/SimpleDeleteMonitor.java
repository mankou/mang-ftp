package mang.tools.ftp;

public class SimpleDeleteMonitor implements FTPDeleteListener {
	
	private int deleteCount=0;

	@Override
	public void afterDeleteFile(String deletePath) {
		deleteCount++;
	}

	public int getDeleteCount() {
		return deleteCount;
	}


}
