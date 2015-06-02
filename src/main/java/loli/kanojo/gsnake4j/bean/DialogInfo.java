package loli.kanojo.gsnake4j.bean;

/**
 * 提示框信息
 * 
 * @author icgeass@hotmail.com
 * @date 2015年6月1日
 * @version gsnake4j - v1.0.4
 * @url https://github.com/icgeass/gsnake4j
 */
public class DialogInfo {

    private String message;
    private String title;
    private int messageType;

    public DialogInfo(String message, String title, int messageType) {
        this.message = message;
        this.title = title;
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

}
