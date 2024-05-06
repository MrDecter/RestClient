package com.example.demo;

import com.example.demo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RestClient {
    private static final String GET_USERS_ENDPOINT_URL = "http://94.198.50.185:7081/api/users";
    private static final String ADD_USER_URL = "http://94.198.50.185:7081/api/users";
    private static final String EDIT_USER_URL = "http://94.198.50.185:7081/api/users";
    private static final String DELETE_USER_URL = "http://94.198.50.185:7081/api/users/{id}";

    public volatile static RestTemplate rest = new RestTemplate();
    public static volatile String jsessionid = "";


    public static void main(String[] args) {
        RestClient springRestClient = new RestClient();
        String cookie = springRestClient.getUsers();
        jsessionid = cookie;
        System.out.println(springRestClient.addUser());
        System.out.println(springRestClient.editUser());
        System.out.println(springRestClient.deleteUser());

    }
    private String getUsers() {
        ResponseEntity<String> responseEntity = rest.getForEntity(GET_USERS_ENDPOINT_URL, String.class);
        List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
        System.out.println(cookies);
        String cookie = cookies.get(cookies.size() - 1);
        int start = cookie.indexOf('=');
        int end = cookie.indexOf(';');
        return cookie.substring(start + 1, end);
    }
    private String addUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", "JSESSIONID=" + jsessionid);
        User user = new User(3L,"James","Brown", (byte)40);
        HttpEntity<User> entity = new HttpEntity<User>(user,headers);
        return rest.exchange(ADD_USER_URL, HttpMethod.POST, entity, String.class).getBody();
    }
    private String editUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", "JSESSIONID=" + jsessionid);
        User User = new User(3L,"Thomas","Shelby", (byte)40);
        HttpEntity<User> entity = new HttpEntity<User>(User,headers);
        return rest.exchange(EDIT_USER_URL, HttpMethod.PUT, entity, String.class).getBody();
    }
    private String deleteUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", "JSESSIONID=" + jsessionid);
        HttpEntity<User> entity = new HttpEntity<User>(headers);
        return rest.exchange(EDIT_USER_URL + "/3", HttpMethod.DELETE, entity, String.class).getBody();
    }
}
