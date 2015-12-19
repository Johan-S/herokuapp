package webapp;

import httpserver.Http;
import httpserver.Request;
import httpserver.Response;
import static httpserver.Responses.json;
import static httpserver.Responses.respond;
import static httpserver.Responses.safeFile;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import util.MultipartFormData;
import static webapp.Router.UPLOADED_PUBLIC_PATH;


public class Images {

  public static Response publicImageList(Request req) {
    File f = new File(UPLOADED_PUBLIC_PATH);
    StringBuilder sb = new StringBuilder();
    for (File u : f.listFiles()) {
      if (sb.length() > 0) {
        sb.append(',');
      }
      sb.append('"').append("/uploaded/").append(u.getName()).append('"');
    }
    return json("[" + sb.toString() + "]");
  }

  public static Response uploadImage(Request req) {
    InputStream in = new ByteArrayInputStream(req.data);
    String boundary = req.headers.get("Content-Type");
    boundary = "\r\n--" + boundary.substring(boundary.indexOf(";") + 11);
    try {
      MultipartFormData p = MultipartFormData.parse(in, boundary.getBytes());
      for (MultipartFormData.FormData f : p.data) {
        if (f.fileName != null) {
          BufferedImage img = ImageIO.read(new ByteArrayInputStream(f.data));
          if (img.getAlphaRaster() != null) {
            BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            Graphics g = img2.getGraphics();
            g.drawRect(0, 0, img2.getWidth(), img2.getHeight());
            g.drawImage(img, 0, 0, Color.WHITE, null);
            img = img2;
          }
          File fin = safeFile(UPLOADED_PUBLIC_PATH, "/" + f.fileName.split("\\.")[0] + ".jpg");
          ImageIO.write(img, "JPG", fin);
        } else {
        }
      }
      return respond("got " + p.data.size());
    } catch (IOException ex) {
      Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
    }
    return new Response(Http.Status.SERVER_ERROR);
  }
}
