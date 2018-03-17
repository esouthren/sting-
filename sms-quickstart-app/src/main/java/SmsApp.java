import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import static spark.Spark.*;

public class SmsApp extends HttpServlet {

    public static void main(String[] args) {



        post("/sms", (req, res) -> {
            System.out.println(req.body());
            String return_text = req.body();
            int start_of_body = return_text.indexOf("&Body=") + 6;
            int end_of_body = 

            res.type("application/xml");
            Body body = new Body
                    .Builder("The Robots are coming! Head for the hills!")
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
    }
}