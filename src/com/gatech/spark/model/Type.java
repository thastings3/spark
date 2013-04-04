package com.gatech.spark.model;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/3/13
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Type {
    private String name;

    public Type(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
