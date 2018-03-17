import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import javax.servlet.http.HttpServlet;
import static spark.Spark.*;

public class sms_listener extends HttpServlet {

    // Whilst running, script waits for a text message.
    // When it arrives, it takes the user data and sends an appropriate response.

    public static void main(String[] args) {


        post("/sms", (req, res) -> {

            // Retrieve SMS text body and fish out user's text
            String return_text = req.body();
            int start_of_body = return_text.indexOf("&Body=") + 6;
            int end_of_body = return_text.indexOf("&FromCou");
            return_text = return_text.substring(start_of_body, end_of_body);

            // Return text is the full string of the user's text message
            System.out.println(return_text);
            // Google Maps API Call
            String da_json = google_maps_call.google_directions("11 The Croft, Didcot", "20 Tavistock Avenue, Didcot", "driving");




            res.type("application/xml");
            Body body = new Body
                    .Builder(da_json) // Text to be returned in SMS
                    .build();
            Message sms = new Message
                    .Builder()
                    .body(body)
                    .build();
            MessagingResponse twiml = new MessagingResponse
                    .Builder()
                    .message(sms)
                    .build();
            return twiml.toXml();
        });

        //String da_json = "hmm that didn't work";
        //da_json = google_maps_call.google_directions("11 The Croft, Didcot", "20 Tavistock Avenue, Didcot", "driving");
        //System.out.println(da_json);
    }
}