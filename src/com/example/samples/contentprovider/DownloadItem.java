package com.example.samples.contentprovider;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 9
 * Time: 오후 5:32
 * To change this template use File | Settings | File Templates.
 */
public class DownloadItem {
    public DownloadItem(String filename, int downloadId) {
        this.filename = filename;
        this.downloadId = downloadId;
    }

    public String filename;
    public int downloadId;


}
