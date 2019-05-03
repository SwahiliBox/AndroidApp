package ke.co.swahilibox.swahilibox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import ke.co.swahilibox.swahilibox.model.ApiService;
import ke.co.swahilibox.swahilibox.model.Messages;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class JsonUnitTest {
    @Before
    public void init(){

        MockitoAnnotations.initMocks(this);

    }
    @Test
    public void testJsonData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dummy.restapiexample.com")//fake data
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Observable<List<Messages>> observable = apiService.getMessages().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new rx.Observer<List<Messages>>() {
            @Override
            public void onCompleted() {
                //a callback when the task gets completed
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Messages> messages) {

                //getAll the data and save it in the database
                String name = messages.get(0).getName();
                String salary = messages.get(0).getSalary();

                assertThat(name, is("testTwo"));
                assertThat(salary, is("123"));


            }
        });
    }
}
