package model;

import play.data.validation.Constraints.*;

public class Image {
  @Required public String id;
  @Required public String name;
  @Required public String dockerRunCommand;
}
