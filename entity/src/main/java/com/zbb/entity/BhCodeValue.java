package com.zbb.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author sunflower
 */
@Data
@Table(name = "bh_code_value_t")
public class BhCodeValue implements Serializable {

    @Id
    private Integer id;

    private String name;

    private String value;

    private static final long serialVersionUID = 1L;

}