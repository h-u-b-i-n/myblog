package com.hubin.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="t_tag")
public class Tag {
    @Id
    @GeneratedValue
    private  Long id;
    private String name;



    @ManyToMany(mappedBy = "tags")
    private List<Blog> blogs=new ArrayList<>();

}
