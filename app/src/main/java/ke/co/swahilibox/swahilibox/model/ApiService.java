package ke.co.swahilibox.swahilibox.model;

import java.util.List;

import ke.co.swahilibox.swahilibox.model.Messages;
import retrofit2.http.GET;
import rx.Observable;

public interface ApiService {
    @GET("/api/v1/employees")
    Observable<List<Messages>> getMessages();
}
