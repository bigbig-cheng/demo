package com.cy.demo.test.autocloseable;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by jiangyi on 2018/7/20.
 */
public class CloseableMain {

    public static void main(String[] args) {
        try(Resource r1 = new Resource(1); Resource r2 = new Resource(2);){

            r1.read();
            r2.read();

        } catch (Exception e){
            System.out.println("error block");
        } finally {
            System.out.println("finally block");
        }

        try(InputStream is = new FileInputStream("");){

            System.out.println("business code");
        }catch (Exception e){
            System.out.println("error block2");
        } finally {
            System.out.println("finally block2");
        }
    }

}
