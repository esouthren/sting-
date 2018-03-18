import com.gtranslate.Language;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import spark.utils.IOUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class google_translate_call {

    public static String translate(String target_language, String text_to_translate) throws Exception{

        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost("https://translation.googleapis.com/language/translate/v2");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("key", "AIzaSyAhAIUxt6cR137G24xx6UVZjPrWdtzpLAE"));
        params.add(new BasicNameValuePair("q", text_to_translate));
        params.add(new BasicNameValuePair("source", "en"));
        params.add(new BasicNameValuePair("target", text_language_to_google_language(target_language)));
        params.add(new BasicNameValuePair("format", "text"));

        request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();

        if (entity != null){
            InputStream inputStream  = entity.getContent();
            String response_content = IOUtils.toString(inputStream);
            JSONObject json_response = new JSONObject(response_content);
            return json_response
                    .getJSONObject("data")
                    .getJSONArray("translations")
                    .getJSONObject(0)
                    .getString("translatedText");
        }
        return "An unknown error occurred";
    }

    private static String text_language_to_google_language(String language){

        switch (language.toLowerCase()){
            case "english": return Language.ENGLISH;
            case "french": return Language.FRENCH;
            case "german": return Language.GERMAN;
            case "dutch": return Language.DUTCH;
            case "spanish": return Language.SPANISH;
        }
        return "UNKNOWN";
    }
}
