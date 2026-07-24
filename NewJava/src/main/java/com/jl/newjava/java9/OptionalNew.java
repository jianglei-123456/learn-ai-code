package com.jl.newjava.java9;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Optional增强
 */
public class OptionalNew {
    public static void main(String[] args) {
        OptionalNew optionalNew = new OptionalNew();
        Map<Integer,User> cache = new HashMap<>();
        // ifPresentOrElse  Optional不为空执行一种逻辑，为空执行一种逻辑
        optionalNew.findUserById(2).ifPresentOrElse(
                user -> cache.put(user.getId(), user),
                ()   -> System.out.println("user not found")
        );
    }

    // 模拟接口返回 Optional
    public Optional<User> findUserById(Integer id) {
        if (id == null || id <= 0) {
            return Optional.empty();  // 直接返回空
        }
        // 模拟查询
        User user = null;
        if (id.equals(1)){
            user = new User(1);
        }
        return Optional.ofNullable(user);
    }

    class User {
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public User(Integer id) {
            this.id = id;
        }

        private Integer id;

    }
}
