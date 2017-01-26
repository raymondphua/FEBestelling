package com.infosupport.team2.febestelling;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static android.R.attr.password;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by paisanrietbroek on 25/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest {

    private static LoginActivity loginActivity;
    private String authUrl1 = "http://10.0.3.2:11150/oauth/token?grant_type=password&" +
            "username=test@test.nl&" +
            "password=password123" +
            "&client_id=kantilever&client_secret=kantiSecret";

    private String authUrl2 = "http://10.0.3.2:11150/oauth/token?grant_type=password&" +
            "username=new@mail.com&" +
            "password=admin" +
            "&client_id=kantilever&client_secret=kantiSecret";

    String email = "test@test.nl";
    String password = "password123";


    @Mock
    private MainActivity mainActivity;


    @BeforeClass
    public static void init() {
        loginActivity = new LoginActivity();
    }

    @Test
    public void createAuthUrl() {

        String authUrl = loginActivity.createAuthUrl(email, password);
        assertThat(authUrl, is(authUrl));

        email = "new@mail.com";
        password = "admin";

        authUrl = loginActivity.createAuthUrl(email, password);
        assertThat(authUrl, is(authUrl2));
    }
}