package com.cy.demo.test.sqlite3;

import com.cy.demo.core.DbHelper;

import java.sql.*;

/**
 * Created by jiangyi on 2018/7/20.
 */
public class Sqlite3Client {

    public static void main(String[] args) throws SQLException {

        try (Connection conn = DbHelper.getDbConnection()) {

            conn.setAutoCommit(false);

            Statement statement = conn.createStatement();

            // 先清空
            statement.execute("delete from main.users");

            statement.execute("insert into main.users values(1, 'abc', '123')");
            statement.execute("insert into main.users values(2, 'def', '456')");

            DbHelper.commit();

            Thread thread1 = new Thread(){
                @Override
                public void run() {

                    try (Connection connInner = DbHelper.getDbConnection()) {

                        connInner.setAutoCommit(false);
                        Statement statement = connInner.createStatement();

                        statement.execute("insert into main.users values(3, 'ghi', '789')");
                        statement.execute("insert into main.users values(4, 'jkl', '012')");

                        DbHelper.rollback();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread1.start();
            thread1.join();

            ResultSet rs = statement.executeQuery("select * from main.users");
            while(rs.next()){
                System.out.println(String.format("id:%d, userName:%s, password:%s", rs.getInt(1), rs.getString(2), rs.getString(3)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
