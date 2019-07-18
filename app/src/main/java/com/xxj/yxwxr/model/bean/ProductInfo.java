package com.xxj.yxwxr.model.bean;


public class ProductInfo {
    private String id;

    private String game_app_id;
    private String game_jump_path;

    private String wx_app_id;
    private String wx_app_secrect;

    private String origin_id;
    private String jump_path;

    private String game_id;
    private String xcx_id;

    private String name;
    private String image;
    private String ico;
    private String desc;
    private String play_num;
    private String type;
    private String path;

    public String getWx_open_status() {
        return wx_open_status;
    }

    public void setWx_open_status(String wx_open_status) {
        this.wx_open_status = wx_open_status;
    }

    private String wx_open_status;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private int release_status;

    public int getRelease_status() {
        return release_status;
    }

    public void setRelease_status(int release_status) {
        this.release_status = release_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGame_app_id() {
        return game_app_id;
    }

    public void setGame_app_id(String game_app_id) {
        this.game_app_id = game_app_id;
    }

    public String getGame_jump_path() {
        return game_jump_path;
    }

    public void setGame_jump_path(String game_jump_path) {
        this.game_jump_path = game_jump_path;
    }

    public String getWx_app_id() {
        return wx_app_id;
    }

    public void setWx_app_id(String wx_app_id) {
        this.wx_app_id = wx_app_id;
    }

    public String getWx_app_secrect() {
        return wx_app_secrect;
    }

    public void setWx_app_secrect(String wx_app_secrect) {
        this.wx_app_secrect = wx_app_secrect;
    }

    public String getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(String origin_id) {
        this.origin_id = origin_id;
    }

    public String getJump_path() {
        return jump_path;
    }

    public void setJump_path(String jump_path) {
        this.jump_path = jump_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPlay_num() {
        return play_num;
    }

    public void setPlay_num(String play_num) {
        this.play_num = play_num;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getXcx_id() {
        return xcx_id;
    }

    public void setXcx_id(String xcx_id) {
        this.xcx_id = xcx_id;
    }


    @Override
    public String toString() {
        return "ProductInfo{" +
                "id='" + id + '\'' +
                ", game_app_id='" + game_app_id + '\'' +
                ", game_jump_path='" + game_jump_path + '\'' +
                ", wx_app_id='" + wx_app_id + '\'' +
                ", wx_app_secrect='" + wx_app_secrect + '\'' +
                ", origin_id='" + origin_id + '\'' +
                ", jump_path='" + jump_path + '\'' +
                ", game_id='" + game_id + '\'' +
                ", xcx_id='" + xcx_id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", ico='" + ico + '\'' +
                ", desc='" + desc + '\'' +
                ", play_num='" + play_num + '\'' +
                ", type='" + type + '\'' +
                ", path='" + path + '\'' +
                ", wx_open_status='" + wx_open_status + '\'' +
                ", release_status=" + release_status +
                '}';
    }
}
