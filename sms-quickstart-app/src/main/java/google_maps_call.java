
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.*;

public class google_maps_call {


    public static String google_directions(String pointA, String pointB, String modeOfTransport) {
        String directionsNicelyFormatted = "";
        String returnedString = "";
        String request_url = "https://maps.googleapis.com/maps/api/directions/json?";
        pointA = pointA.replace(" ", "+");
        pointB = pointB.replace(" ", "+");
        String api = "AIzaSyCrbSsveg2T8ZrfWV2H2InJqm94BXz5ZD8";
        request_url = request_url +
                "origin=" + pointA +
                "&destination=" + pointB +
                "&mode=" + modeOfTransport +
                "&key=" + api;
        try {
            returnedString = http_request.make_get_request(request_url);
            //System.out.println(returnedString);
        } catch (Exception ex) {
            return "I'm lost, man:" + ex.getMessage();
        }

        JSONObject json_data = new JSONObject(returnedString);
        JSONObject temp=null;

        JSONArray listOfRoutes = json_data.getJSONArray("routes");
        if (listOfRoutes.length() == 0){
            return "Could not find route from \"" + pointA.replace("+", " ") + "\" to \"" +
                    pointB.replace("+", " ") + "\". Try being either more or less specific";
        }
        JSONObject firstRoute = listOfRoutes.getJSONObject(0);

        JSONArray listOfLegs = firstRoute.getJSONArray("legs");
        JSONObject firstLeg = listOfLegs.getJSONObject(0);

        String distance = firstLeg.getJSONObject("distance").getString("text");
        String duration = firstLeg.getJSONObject("duration").getString("text");

        JSONArray listOfSteps = firstLeg.getJSONArray("steps");
        int length=listOfSteps.length();

        directionsNicelyFormatted += "** " + modeOfTransport.substring(0, 1).toUpperCase() + modeOfTransport.substring(1) + " Directions  - " + distance + ", approx " + duration + " **\n\n";

        for (int i=0;i<length;i++) {
            // Build SMS string to return to User
            temp = (JSONObject)listOfSteps.get(i);
            directionsNicelyFormatted += temp.getString("html_instructions") + ".\n";
        }

        // regex to remove HTML tags
        directionsNicelyFormatted = directionsNicelyFormatted.replaceAll("<div style=\"font-size:0.9em\">", "\n");
        directionsNicelyFormatted = directionsNicelyFormatted.replaceAll("<[^>]*>", "");
        return directionsNicelyFormatted;
    }
}
