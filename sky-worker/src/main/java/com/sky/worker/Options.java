package com.sky.worker;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by jcooky on 2014. 7. 31..
 */
@Component
public class Options {

  @Resource
  private Environment env;

  private Map<Key, String> options = new EnumMap<Key, String>(Key.class);
  private org.apache.commons.cli.Options cliOptions;
  private CommandLine cl;

  public void printHelp() {
    new HelpFormatter().printHelp("java -jar sky-worker.jar [OPTIONS]", cliOptions);
  }

  private void build(String []args) throws ParseException {
    cliOptions = buildOptions();
    cl = new BasicParser().parse(cliOptions, args);
  }

  private org.apache.commons.cli.Options buildOptions() {
    org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
    for (Key key : Key.values()) {
      options.addOption(key.shortOpt, key.longOpt, key.hasArg, key.description);
    }

    return options;
  }

  public String get(Key key) {
    return cl.getOptionValue(key.longOpt, this.options.get(key));
  }

  public boolean has(Key key) {
    return cl.hasOption(key.longOpt) || this.options.containsKey(key);
  }

  public void init(String []args) throws ParseException, IOException {

    for (Key key : Key.values()) {
      String k = "worker.options." + key.name().toLowerCase();
      if (env.containsProperty(k)) {
        this.options.put(key, env.getProperty(k));
      }
    }

    this.build(args);
  }

  public static enum Key {
    HELP("h", false, "Print Help Messages"),
    HOST("o", true, "Sky Server Host URL"),
    NAME("n", true, "Worker name"),
    PORT("p", true, "Worker port");

    private final String longOpt;
    private final String shortOpt;
    private final boolean hasArg;
    private final String description;

    private Key(String shortOpt, boolean hasArg, String description) {
      this.shortOpt = shortOpt;
      this.hasArg = hasArg;
      this.description = description;
      this.longOpt = name().toLowerCase().replace('_', '-');
    }
  }
}
