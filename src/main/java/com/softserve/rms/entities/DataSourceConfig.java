package com.softserve.rms.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "datasourceconfig")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceConfig  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String url;
    private String username;
    private String password;

    private String driverclassname;
    private boolean initialize;

    public DataSourceConfig(String name,String url,String username,String password,String driverclassname){
       this.name = name;
       this.url = url;
       this.username = username;
       this.password = password;
       this.driverclassname = driverclassname;
    }


}