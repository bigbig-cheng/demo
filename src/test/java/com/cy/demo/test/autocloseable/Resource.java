package com.cy.demo.test.autocloseable;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by jiangyi on 2018/7/20.
 */
public class Resource implements Closeable {

    private int data;

    public Resource(int data){
        this.data = data;
    }

    public void read(){
        System.out.println(String.format("read %d", data));
        if(data%2 == 0){
            throw new RuntimeException(String.format("error %d", data));
        }
    }

    @Override
    public void close() throws IOException {
        System.out.println(String.format("closed %d", data));
    }
}
