package ruilelin.com.shifenlife.jsonmodel;

public class ChannelModel {
    private String channel_name;
    private String image;
    private int option;
    private int type;
    private Value value;
    private String name;
    private String goodsNumber;
    private String id;

    public ChannelModel() {

    }

    public ChannelModel(String name) {
        this.name = name;
    }

    public ChannelModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public ChannelModel(String channel_name, String image, int option, int type, Value value) {
        this.channel_name = channel_name;
        this.image = image;
        this.option = option;
        this.type = type;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }
    public String getChannel_name() {
        return channel_name;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return image;
    }

    public void setOption(int option) {
        this.option = option;
    }
    public int getOption() {
        return option;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setValue(Value value) {
        this.value = value;
    }
    public Value getValue() {
        return value;
    }
}
