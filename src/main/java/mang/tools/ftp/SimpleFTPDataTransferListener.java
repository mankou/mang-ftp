package mang.tools.ftp;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class SimpleFTPDataTransferListener implements FTPDataTransferListener {

	@Override
	public void aborted() {
		System.out.println("aborted");

	}

	@Override
	public void completed() {
		System.out.println("completed");

	}

	@Override
	public void failed() {
		System.out.println("failed");

	}

	@Override
	public void started() {
		System.out.println("started");

	}

	@Override
	public void transferred(int arg0) {
		System.out.println("transferred"+arg0);
	}

}
