package com.sky.worker;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jcooky on 2014. 7. 31..
 */
public class Options {

  private Map<Key, String> options = new EnumMap<Key, String>(Key.class);
  private String helpMessage = null;

  public String toString() {
    return helpMessage;
  }

  public void build(String []args) throws ParseException {
    org.apache.commons.cli.Options cliOptions = buildOptions();
    CommandLine cl = new PosixParser().parse(cliOptions, args);
    this.helpMessage = cliOptions.toString();

    for (Key key : Key.values()) {
      String val = cl.getOptionValue(key.longOpt);
      if (val != null) {
        this.options.put(key, val);
      } else if (!this.options.containsKey(key) && key.defaultValue != null) {
        this.options.put(key, key.defaultValue);
      }
    }
  }

  private org.apache.commons.cli.Options buildOptions() {
    org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
    for (Key key : Key.values()) {
      options.addOption(key.shortOpt, key.longOpt, key.hasArg, key.description);
    }

    return options;
  }

  public String get(Key key) {
    return this.options.get(key);
  }

  public boolean has(Key key) {
    return this.options.containsKey(key);
  }

  public void init(String []args) throws ParseException, IOException {
    Properties env = new Properties();
    env.load(ClassLoader.getSystemResourceAsStream("application.properties"));

    for (Key key : Key.values()) {
      String k = "worker.options." + key.name().toLowerCase();
      if (env.containsKey(k)) {
        this.options.put(key, env.getProperty(k));
      }
    }

    this.build(args);
  }

  public static enum Key {
    HELP("h", false, "Print Help Messages"),
    HOST("o", true, "Sky Server Host URL"),
    NAME("n", true, "Worker name"),
    PORT("p", true, "Worker port", "9200");

    private final String longOpt;
    private final String shortOpt;
    private final boolean hasArg;
    private final String description;
    private String defaultValue = null;

    private Key(String shortOpt, boolean hasArg, String description, String defaultValue) {
      this(shortOpt, hasArg, description);
      this.defaultValue = defaultValue;
    }

    private Key(String shortOpt, boolean hasArg, String description) {
      this.shortOpt = shortOpt;
      this.hasArg = hasArg;
      this.description = description;
      this.longOpt = name().toLowerCase().replace('_', '-');
    }
  }
}
