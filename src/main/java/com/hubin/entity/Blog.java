package com.hubin.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity  //具备和数据库对应生成的能力
@Table(name="t_blog")  //这里指实体类对应对应t_blog
public class Blog {
    //这里指表主键自动增长
    @Id   //@Id 标注用于声明一个实体类的属性映射为数据库的主键列。
    @GeneratedValue   //@GeneratedValue 用于标注主键的生成策略
    private Long id;
    private String title;


    @Basic(fetch = FetchType.LAZY)
    @Lob
    private  String content;
    private String firstPicture;    private  String flag;
    private Integer views=0;
    private boolean appreciation;
    private boolean shareStatement;
    private  boolean published;
    private boolean commentabled;
    private boolean recommend;
    @Temporal(TemporalType.TIMESTAMP) //指定时间生成类型
    private Date creatTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Transient
    private String tagIds;

    private String description;



    @ManyToOne  //对应关系 blog是维护方
    private Type type;
    @ManyToMany(cascade={CascadeType.PERSIST})//表示级联新建
    private List<Tag> tags=new ArrayList<>();
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "blog")
    private List<Comment> comments=new ArrayList<>();


    public void init() {

        this.tagIds = tagsToIds(this.getTags());
    }

    //将tags集合转换成字符串格式的tags集合     uuuuu
    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }


}
