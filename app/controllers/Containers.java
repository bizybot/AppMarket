package controllers;

import model.Container;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import play.Play;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import views.html.*;

public class Containers extends Controller {

	// -- Actions

	/**
	 * Home page
	 */
	public static Result index() {

		return ok(containers.render(fetchContainers()));
	}

	private static List<Container> fetchContainers() {
		String privRepo = Play.application().configuration()
				.getString("privateRepo");
		if (privRepo.equals("dummy")) {
			return fetchDummyContainers();
		}
		Promise<WS.Response> resp = WS.url(
				"http://localhost:4243/containers/json").get();
		WS.Response response = resp.get(10000);

		List<Container> containers = new ArrayList<Container>();
		JsonNode jsonNode = response.asJson();
		System.out.println(jsonNode.toString());
		Iterator<JsonNode> iter = jsonNode.elements();
		while (iter.hasNext()) {
			JsonNode node = iter.next();
			System.out.println(node.toString());
			Container container = new Container();
			container.id = node.get("Id").asText();
			container.name = node.get("Names").toString();
			container.image = node.get("Image").asText();
			container.status = node.get("Status").asText();

			containers.add(container);
		}
		return containers;
	}

	private static List<Container> fetchDummyContainers() {
		List<Container> containers = new ArrayList<Container>();
		Container container = new Container();
		container.id = "id-1";
		container.name = "app-1";
		container.status = "Running";
		container.image = "bizybot/rapi-base-image";
		containers.add(container);
		container = new Container();
		container.id = "id-2";
		container.name = "app-2";
		container.status = "Running";
		container.image = "bizybot/gpio";
		containers.add(container);
		container = new Container();
		container.id = "id-3";
		container.name = "app-3";
		container.status = "Running";
		container.image = "bizybot/yg-camera";
		containers.add(container);
		return containers;
	}
}