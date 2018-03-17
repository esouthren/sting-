import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import javax.servlet.http.HttpServlet;
import static spark.Spark.*;
import java.util.Arrays;

public class sms_listener extends HttpServlet {

    // Whilst running, script waits for a text message.
    // When it arrives, it takes the user data and sends an appropriate response.

    public static void main(String[] args) {


        post("/sms", (req, res) -> {

            // Retrieve SMS text body and fish out user's text
            String return_text = req.body();
            String first_address = "";
            String second_address = "";
            String transport_method = "";
            String output_message = "";

            int start_of_body = return_text.indexOf("&Body=") + 6;
            int end_of_body = return_text.indexOf("&FromCou");
            return_text = return_text.substring(start_of_body, end_of_body);
            String[] transport_methods = {"walking", "driving", "bicycling", "transit"};
            // Return text is the full string of the user's text message
            System.out.println(return_text);
            String sms_split_words[] = return_text.split(" ");
            if (sms_split_words[0].toLowerCase().equals("directions")) {
                if (Arrays.asList(transport_methods).contains(sms_split_words[1].toLowerCase())) {
                    transport_method = sms_split_words[1];
                    int i = 2;

                    while (!sms_split_words[i].toLowerCase().equals("to")) {
                        first_address += sms_split_words[i] + " ";
                        i++;
                    }
                    i++;
                    while (i < sms_split_words.length) {
                        second_address += sms_split_words[i] + " ";
                        i++;
                    }
                    output_message = google_maps_call.google_directions(first_address, second_address, transport_method);
                    System.out.println(output_message);
                }
            }
            else if (sms_split_words[0].toLowerCase().equals("joke")) {
                output_message = joke_generator.generate_joke();
            }


            // Google Maps API Call

            res.type("application/xml");
            Body body = new Body
                    .Builder(output_message) // Text to be returned in SMS
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