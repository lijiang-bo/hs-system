package com.jsu;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TliasWebManagementApplicationTests {

//    /**
//     * 生成jwt
//     */
//    @Test
//    public void testGenJwt() {
//        Map<String, Object> claims = new HashMap<>();// 自定义信息
//        claims.put("id", 10);// 用户id
//        claims.put("username", "jsu");// 用户名
//
//        String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256, "aXRjYXN0")// 私钥
//                .addClaims(claims)// 自定义信息
//                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000))// 1分钟过期
//                .compact();// 生成jwt
//        System.out.println(jwt);// <KEY>
//    }
//
//
//    /**
//     * 解析jwt
//     */
//    @Test
//    public void testParseJwt() {
//        Claims claims = Jwts.parser().setSigningKey("aXRjYXN0")// 私钥
//                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MTAsInVzZXJuYW1lIjoianN1IiwiZXhwIjoxNzc1MzMyODc0fQ.ha8NXuPajsYgldPQeRrVqXYPV_DIxCAosCRvmOxMopY")
//                .getBody();// 解析jwt
//        System.out.println(claims);
//    }
}
