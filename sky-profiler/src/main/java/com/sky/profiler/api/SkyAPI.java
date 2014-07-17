package com.sky.profiler.api;

import com.sky.commons.RealtimeMethodProfile;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.shiftone.jrat.util.log.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by jcooky on 2014. 7. 7..
 */
public class SkyAPI {
  private static final Logger LOGGER = Logger.getLogger(SkyAPI.class);
  private static String SKY_SERVER_URL = "http://localhost:8080/agent/profile";

  public static Long create() throws IOException, ClassNotFoundException {

    return Long.parseLong(Request.Post(SKY_SERVER_URL)
        .execute().returnContent().asString());
  }

  public static void put(RealtimeMethodProfile profile) throws IOException {

    ByteArrayOutputStream baos = null;
    try {
      ObjectOutputStream oos = new ObjectOutputStream((baos = new ByteArrayOutputStream()));
      oos.writeObject(profile);
      HttpResponse response = Request.Put(SKY_SERVER_URL)
          .bodyByteArray(baos.toByteArray())
          .execute().returnResponse();
      int statusCode = response.getStatusLine().getStatusCode();

      if (statusCode != 200) {
        LOGGER.error("statusCode: " + statusCode);
      }
    } finally {
      if (baos != null) baos.close();
    }
  }

  public static void main(String []args) throws Exception {
    RealtimeMethodProfile profile = new RealtimeMethodProfile();

    create();
    put(profile);
  }
}
