package com.ziyadmsq.android.lowclub;

import android.media.Image;

public class Publication {
    private String title;
    private String content;
    private String adminsName;
    private Image image;

    public Publication(String title, String content, String adminsName, Image image) {
        this.title = title;
        this.content = content;
        this.adminsName = adminsName;
        this.image = image;
    }
}
