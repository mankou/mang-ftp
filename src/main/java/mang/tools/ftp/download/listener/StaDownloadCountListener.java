package mang.tools.ftp.download.listener;

import java.util.Map;

public class StaDownloadCountListener extends AbstractDownloadListener {
	private int count=0;


	@Override
	public void afterDownload(Map<String, Object> listenerPara) {
		this.count++;
	}
	
	
	public int getDownloadCount(){
		return this.count;
	}

}
