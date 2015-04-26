package facebook;

/**
 * Created by Deep Trivedi on 4/22/2015.
 */

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("/v2.3")
public class Controller {

    String accessToken = "CAACEdEose0cBAG38rZA8zrQ6P9qYZBaIR5f7KO7PiZCEtR9VxC4Fx5145qEP9Tjuvulp74nrKughPKXs11jU68MFBgjf74qtIZAZBvUqdH1xMyWXWscybSDJqIQfcGQ7LYyPavZAMFIhbF0vuRywI2gzS7Gj5u2b9XWmCO5E04u3AZAp3gQa07KHo40ZAbeEgtDY8BiWYIvbpU6P2I4rHRy4vupNdqXpdg0ZD";


    @RequestMapping(value = "/{user-id}/photos", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    UserPhotos getPhotos(@PathVariable("user-id") String id) {

        FacebookClient fbClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_2);
        User me;
        me = fbClient.fetchObject(id, com.restfb.types.User.class);
        //PhotoAttributes object = new PhotoAttributes();
        UserPhotos object = new UserPhotos();
        //set name and id
        object.setName(me.getName());
        object.setId(me.getId());


        JsonObject photosConnection = fbClient.fetchObject("me/photos", JsonObject.class);
        ArrayList<PhotoAttributes> myPhotos = new ArrayList<PhotoAttributes>();
        int size = photosConnection.getJsonArray("data").length();

        //-------LIKES--------//
        for (int i = 0; i < size; i++) {

            PhotoAttributes obj = new PhotoAttributes();
            String firstPhotoUrl = photosConnection.getJsonArray("data").getJsonObject(i).getString("source");
            obj.setPhotoUrl(firstPhotoUrl);
            List<PersonInfo> nameLikes = new ArrayList<PersonInfo>();
            List<PersonInfo> nameComments = new ArrayList<PersonInfo>();
            List<PersonInfo> nameTags = new ArrayList<PersonInfo>();
            if (photosConnection.getJsonArray("data").getJsonObject(i).has("likes")) {

                JsonArray nameLikesJson = photosConnection.getJsonArray("data").getJsonObject(i).getJsonObject("likes").getJsonArray("data");
                int m = 0;

                for (int n = 0; n < nameLikesJson.length(); n++) {
                    PersonInfo person = new PersonInfo();
                    String temp1 = nameLikesJson.getJsonObject(n).getString("name");
                    String temp2 = nameLikesJson.getJsonObject(n).getString("id");
                    person.setId(temp2);
                    person.setName(temp1);
                    nameLikes.add(person);
                    m++;
                }
                obj.setLikes(nameLikes);
                obj.setNumberOfLikes(m);
            }

            //----------------COMMENTS-------------//
            if (photosConnection.getJsonArray("data").getJsonObject(i).has("comments")) {

                JsonArray nameCommentsJson = photosConnection.getJsonArray("data").getJsonObject(i).getJsonObject("comments").getJsonArray("data");
                int k = 0;
                for (int c = 0; c < nameCommentsJson.length(); c++) {
                    JsonObject fromCommentsJson = nameCommentsJson.getJsonObject(c).getJsonObject("from");
                    String temp3 = fromCommentsJson.getString("name");

                    String temp4 = fromCommentsJson.getString("id");
                    PersonInfo person = new PersonInfo();
                    person.setId(temp4);
                    person.setName(temp3);
                    nameComments.add(person);
                    k++;
                }

                obj.setComments(nameComments);
                obj.setNumberOfComments(k);

            }
            //---------TAGS-----------//
            if (photosConnection.getJsonArray("data").getJsonObject(i).has("tags")) {

                JsonArray nameTagsJson = photosConnection.getJsonArray("data").getJsonObject(i).getJsonObject("tags").getJsonArray("data");
                int k = 0;
                for (int c = 0; c < nameTagsJson.length(); c++) {
                    String temp6 =" ";
                    if(nameTagsJson.getJsonObject(c).has("id")) {
                         temp6 = nameTagsJson.getJsonObject(c).getString("id");
                    }
                    String temp5 = nameTagsJson.getJsonObject(c).getString("name");



                    PersonInfo person = new PersonInfo();
                    person.setId(temp6);
                    person.setName(temp5);
                    nameTags.add(person);
                    k++;
                }

                obj.setTags(nameTags);
                obj.setNumberOfTags(k);

            }

            myPhotos.add(obj);
        }

        object.setPhotos(myPhotos);


        return object;
    }


    // ------------------------------------------------------------------
    //                              API for POSTS
    // ------------------------------------------------------------------
    @RequestMapping(value = "/{user-id}/posts", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    UserPosts getPosts(@PathVariable("user-id") String id) {

        FacebookClient fbClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_2);
        User me;
        me = fbClient.fetchObject(id, com.restfb.types.User.class);
        UserPosts object = new UserPosts();

        //set name and id
        object.setName(me.getName());
        object.setId(me.getId());


        JsonObject postConnection = fbClient.fetchObject("me/posts", JsonObject.class);
        ArrayList<PostAttributes> myPosts = new ArrayList<PostAttributes>();
        int size = postConnection.getJsonArray("data").length();

        for (int i = 0; i < size; i++) {
            PostAttributes obj = new PostAttributes();

            String postType = postConnection.getJsonArray("data").getJsonObject(i).getString("type");
            obj.setType(postType);

            //String message = postConnection.getJsonArray("data").getJsonObject(i).getString("description");
            //obj.setMessage(message);

            String owner = postConnection.getJsonArray("data").getJsonObject(i).getString("from");
            obj.setOwner(owner);

            List<PersonInfo> nameLikes = new ArrayList<PersonInfo>();
            List<PersonInfo> nameComments = new ArrayList<PersonInfo>();
            List<PersonInfo> nameTags = new ArrayList<PersonInfo>();

            if (postConnection.getJsonArray("data").getJsonObject(i).has("likes")) {

                JsonArray nameLikesJson = postConnection.getJsonArray("data").getJsonObject(i).getJsonObject("likes").getJsonArray("data");
                int m = 0;

                for (int n = 0; n < nameLikesJson.length(); n++) {
                    PersonInfo person = new PersonInfo();
                    String temp1 = nameLikesJson.getJsonObject(n).getString("name");
                    String temp2 = nameLikesJson.getJsonObject(n).getString("id");
                    person.setId(temp2);
                    person.setName(temp1);
                    nameLikes.add(person);
                    m++;
                }

                obj.setLikes(nameLikes);
                obj.setNumberOfLikes(m);
            }


            //----------------COMMENTS-------------//
            if (postConnection.getJsonArray("data").getJsonObject(i).has("comments")) {

                JsonArray nameCommentsJson = postConnection.getJsonArray("data").getJsonObject(i).getJsonObject("comments").getJsonArray("data");
                int k = 0;
                for (int c = 0; c < nameCommentsJson.length(); c++) {
                    JsonObject fromCommentsJson = nameCommentsJson.getJsonObject(c).getJsonObject("from");
                    String temp3 = fromCommentsJson.getString("name");

                    String temp4 = fromCommentsJson.getString("id");
                    PersonInfo person = new PersonInfo();
                    person.setId(temp4);
                    person.setName(temp3);
                    nameComments.add(person);
                    k++;
                }

                obj.setComments(nameComments);
                obj.setNumberOfComments(k);

            }
            //---------TAGS-----------//
            if (postConnection.getJsonArray("data").getJsonObject(i).has("tags")) {

                JsonArray nameTagsJson = postConnection.getJsonArray("data").getJsonObject(i).getJsonObject("tags").getJsonArray("data");
                int k = 0;
                for (int c = 0; c < nameTagsJson.length(); c++) {
                    String temp5 = nameTagsJson.getJsonObject(c).getString("name");
                    String temp6 = nameTagsJson.getJsonObject(c).getString("id");


                    PersonInfo person = new PersonInfo();
                    person.setId(temp6);
                    person.setName(temp5);
                    nameTags.add(person);
                    k++;
                }

                obj.setTags(nameTags);
                obj.setNumberOfTags(k);

            }

            myPosts.add(obj);
        }

        object.setPosts(myPosts);


        return object;
    }
}

