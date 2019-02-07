package android.com.chatbox;

import java.util.Date;

public class BlogPost extends BlogPostId {

    public String user_id, desc;
    public Date timestamp;

    public BlogPost() {}

    public BlogPost(String user_id, String desc, Date timestamp) {
        this.user_id = user_id;
        this.desc = desc;
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


}
