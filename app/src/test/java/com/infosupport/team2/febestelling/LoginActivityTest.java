package com.infosupport.team2.febestelling;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static android.R.attr.text;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by paisanrietbroek on 25/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest {

    private static LoginActivity loginActivity;

    @Mock
    private MainActivity mainActivity;


    @BeforeClass
    public static void init() {
        loginActivity = new LoginActivity();
    }

    @Test
    public void createAuthUrl() {
        String email = "test@test.nl";
        String password = "password123";

        String authUrl = loginActivity.createAuthUrl(email, password);
        assertThat(authUrl, is("http://10.0.3.2:11150/oauth/token?grant_type=password&" +
                "username=test@test.nl&" +
                "password=password123" +
                "&client_id=kantilever&client_secret=kantiSecret"));

        email = "new@mail.com";
        password = "admin";

        authUrl = loginActivity.createAuthUrl(email, password);
        assertThat(authUrl, is("http://10.0.3.2:11150/oauth/token?grant_type=password&" +
                "username=new@mail.com&" +
                "password=admin" +
                "&client_id=kantilever&client_secret=kantiSecret"));
    }
}