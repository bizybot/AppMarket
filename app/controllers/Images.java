package controllers;

import model.Image;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;

import views.html.*;

public class Images extends Controller {
    
    // -- Actions
  
    /**
     * Home page
     */
    public static Result index() {
      
      return ok(
				images.render(fetchDummyImages())
        		//images.render(fetchImages())
        );
    }

    /**
     * Run Command
     */
    public static Result runcmd(String dockerCommand) throws Exception {
      
        Process process1 = Runtime.getRuntime().exec(new String[]{"touch","/tmp/runCommand"});
        process1.waitFor();
        process1.destroy();

        Process process = Runtime.getRuntime().exec(new String[]{"echo", dockerCommand, ">" , "/tmp/runCommand"});
        //Process process = Runtime.getRuntime().exec(new String[]{dockerCommand});
        process.waitFor();
        process.destroy();
      
        return index();
    }
  
    private static List<Image> fetchImages() {
      Promise<WS.Response> resp = WS.url("http://10.110.9.234:5000/v1/search").get();
      WS.Response response = resp.get(10000);
      List<JsonNode> nodes = response.asJson().findValues("name");
      List<Image> images = new ArrayList<Image>();
      int i = 0;
      if (nodes != null) {
      	for (JsonNode node : nodes) {
           Image image = new Image();
      	  if (node != null) {
            image.id = "id-"+i;
           	image.name = node.asText();
            image.dockerRunCommand = "docker run -d --privileged 10.110.9.234:5000/"+image.name;
            images.add(image);
            i++;
           }
      	}
      }
      return images;
    }
  
    private static List<Image> fetchDummyImages() {
      Image image = new Image();
      image.id = "id-1";
      image.name = "VCALIB Application";
      image.dockerRunCommand = "docker run -d --privileged 10.110.9.234:5000/bizybot/rapi-base-image";
      Image image1 = new Image();
      image1.id = "id-2";
      image1.name = "GPIO Application";
      image1.dockerRunCommand = "docker run -d --privileged 10.110.9.234:5000/bizybot/gpio";
      Image image2 = new Image();
      image2.id = "id-3";
      image2.name = "Camera Application";
      image2.dockerRunCommand = "docker run -d --privileged 10.110.9.234:5000/bizybot/camera";
      List<Image> imagesL = new ArrayList<Image>();
      imagesL.add(image);
      imagesL.add(image1);
      imagesL.add(image2);
      return imagesL;
    }
}
