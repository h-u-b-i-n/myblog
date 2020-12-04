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
@Entity
@Table(name="t_comment")
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    private String nickname;
    private String email;
    private String content;
    private boolean adminComment;
    private String avatar;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;






    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replyComments=new ArrayList<>();//一个父评论包含多个子评论

    @ManyToOne
    private Comment parentComment;//每一个子评论只有一个对应的父评论（即是最近的那个夫父评论）
    @ManyToOne
    private Blog blog;




}
