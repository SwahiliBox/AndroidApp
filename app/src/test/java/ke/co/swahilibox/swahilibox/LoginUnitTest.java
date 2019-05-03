package ke.co.swahilibox.swahilibox;

//test login

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import ke.co.swahilibox.swahilibox.views.LogIn;
import ke.co.swahilibox.swahilibox.views.Main;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class LoginUnitTest{


    private static final String FAKE_STRING = "Login Success";
    private static final String FAKE_STRING1 = "Login Failed";

    @Mock Context mContext;
    LogIn login;
    @Mock Main main; //we dont have to instantiate

    @Before
    public void init(){

        MockitoAnnotations.initMocks(this);
        login = new LogIn(mContext);

    }

    @Test
    public void checkLoginStatusUsingContext(){
        //Stores the return value of getHelloWorldString() in result
        String result = login.LoginStatus(FAKE_STRING);
        //Asserts that result is the value of TEST_STRING
        assertThat(result, is("Logged in Successfully"));

    }



}