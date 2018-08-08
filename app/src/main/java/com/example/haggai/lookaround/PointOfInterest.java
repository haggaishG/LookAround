package com.example.haggai.lookaround;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PointOfInterest {


    private double latitude ;
    private double longitude ;
    private String name = null ;
    private String iconUrl = null;
    private String photoReference = null ;
    private boolean selectedForDisplay = false;

    public double[] getPosition() {
        return new double[]{latitude,longitude};
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public boolean isSelectedForDisplay() {
        return selectedForDisplay;
    }

    public void setSelectedForDisplay(boolean selectedForDisplay) {
        this.selectedForDisplay = selectedForDisplay;
    }


    /*  THIS IS THE EXPECTED JSON:
    {
         "geometry" : {
            "location" : {
               "lat" : 33.038117,
               "lng" : 35.509278
            },
            "viewport" : {
               "northeast" : {
                  "lat" : 33.0394659802915,
                  "lng" : 35.5106269802915
               },
               "southwest" : {
                  "lat" : 33.0367680197085,
                  "lng" : 35.5079290197085
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png",
         "id" : "b84c7d949c7511b8d6e81ab3b94446cc95a06177",
         "name" : "Alma Cave",
         "photos" : [
            {
               "height" : 1371,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/116814032363202226950/photos\"\u003eAndrey Deveykin\u003c/a\u003e"
               ],
               "photo_reference" : "CmRaAAAAhIKajKVj4hTm-3bxjnfqzBrU6ZYMIVwP6jzI7wHhYcF_8lal3anZ59b98geZ_Ml0Grp9h402s3KdHIvU5SYwGquTVz09E4NyO4ZTECKYhUa5Nc_Q4hYuhw_rwh2fqpeCEhBBNLFgabQdXAmL1sbDMFk-GhTPc7gFJQW5w9dSSHGOvPbA7mmnzg",
               "width" : 2048
            }
         ],
         "place_id" : "ChIJX2Vslu8gHBUR9jLp2RSGvtk",
         "plus_code" : {
            "compound_code" : "2GQ5+6P Alma, Israel",
            "global_code" : "8G5Q2GQ5+6P"
         },
         "rating" : 4.5,
         "reference" : "CmRSAAAA8n8V2OESA9m-0QBl9J0ECpwqtAYwDlS8_ZNOyfmd6HfZZJOgicuZPKKL5D3TSBWw_1WnoDP3DThEayeb3SfnIX0Zbw5LtANwsCeMBWhcQ1ARwag8ZjoSedTWpbjLiLNzEhBMG0LHpGRCrW3K06_u94VsGhSFL8kBzZjooR0YHGI7WP6gEq_3SA",
         "scope" : "GOOGLE",
         "types" : [ "natural_feature", "point_of_interest", "establishment" ],
         "vicinity" : "Israel"
      },
     */
    public void parseFromJson(JSONObject poi){

        try {
            JSONObject location = poi.getJSONObject("geometry").getJSONObject("location");
            latitude = location.getDouble("lat");
            longitude = location.getDouble("lng");
            name  = poi.getString("name");
            iconUrl = poi.getString("icon");
            JSONArray photos = poi.getJSONArray("photos");
            if(photos != null && photos.length() > 0){
                JSONObject tmp = photos.getJSONObject(0);
                photoReference = tmp.getString("photo_reference");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
