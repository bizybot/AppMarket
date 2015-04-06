package model;

import play.data.validation.Constraints.*;

public class Container {
  @Required public String id;
  @Required public String name;
  @Required public String image;
  @Required public String status;
}
