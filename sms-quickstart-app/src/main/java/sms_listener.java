import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import javax.servlet.http.HttpServlet;
import static spark.Spark.*;
import java.util.Arrays;

public class sms_listener extends HttpServlet {

    private static String get_directions(String[] sms_split_words) {
        String[] transport_methods = {"walking", "driving", "bicycling", "transit"};
        String first_address = "";
        String second_address = "";

        if (sms_split_words.length < 5) {
            return "Invalid directions query: please use syntax: directions <travel method> <origin> to <destination>";
        }
        if (!Arrays.asList(transport_methods).contains(sms_split_words[1].toLowerCase())) {
            return "Invalid travel method \"" + sms_split_words[1] + "\" Allowed values are walking, driving, bicycling, transit";
        }
        String transport_method = sms_split_words[1];
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
        try {
            return google_maps_call.google_directions(first_address, second_address, transport_method);
        } catch (Exception ex) {
            return "An unexpected error occurred: " + ex.getMessage();
        }
    }

    private static String get_translation(String[] sms_split_words){
        if (len(sms_split_words) < 3){
            return "Invalid translation query. Please use syntax: translate <target language> <text to translate>";
        }
        String target_language = sms_split_words[1];
        if (google_translate_call.text_language_to_google_language(target_language).equals("UNKNOWN")){
            return "Invalid language \"" + target_language + "\"";
        }

        String text_to_translate = "";
        for (int i = 2; i < sms_split_words.length; i++){
            text_to_translate += sms_split_words[i] + " ";
        }
        try {
            return google_translate_call.translate(target_language, text_to_translate);
        } catch (Exception ex){
            return "An unknown error occurred: " + ex.getMessage();
        }
    }

    // Whilst running, script waits for a text message.
    // When it arrives, it takes the user data and sends an appropriate response.

    public static void main(String[] args) {


        post("/sms", (req, res) -> {

            // Retrieve SMS text body and fish out user's text
            String return_text = req.body().replace("%27", "'");

            int start_of_body = return_text.indexOf("&Body=") + 6;
            int end_of_body = return_text.indexOf("&FromCou");
            return_text = return_text.substring(start_of_body, end_of_body);
            String output_message = "";
            // Return text is the full string of the user's text message
            System.out.println(return_text);
            String sms_split_words[] = return_text.split("\\+");
            if (sms_split_words[0].toLowerCase().equals("directions")) {
                output_message = get_directions(sms_split_words);
            }
            else if (sms_split_words[0].toLowerCase().equals("joke")) {
                output_message = joke_generator.generate_joke();
            }
            else if (sms_split_words[0].toLowerCase().equals("translate")) {
                output_message = get_translation(sms_split_words);
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