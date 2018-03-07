package com.sun.sunmall.pojo;



import java.util.Date;

/**
 * 购买明细记录
 */

public class Record {
    /**
     * id
     */
    private Integer id;
    /**
     * 用户
     */
    private User user;
    /**
     * 产品
     */
    private Product product;
    /**
     * 1秒杀成功,0秒杀失败,-1重复秒杀,-2系统异常

     */
    private String state;

    private String message;
    private Date date;

    public Record(User user, Product product, String state, String message, Date date) {
        this.user = user;
        this.product = product;
        this.state = state;
        this.message = message;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**

     * 状态的明文标识
     */
    private String stateInfo;
    /**
     * 创建时间
     */
    private Date createTime;
}
