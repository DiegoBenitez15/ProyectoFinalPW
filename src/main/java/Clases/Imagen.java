package Clases;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Imagen {

    private static Imagen instancia;


    public static Imagen getInstancia(){
        if(instancia==null){
            instancia = new Imagen();
        }
        return instancia;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new java.net.URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    public String getByteArrayFromImageURL(String url) {
        try {
            JSONObject json = this.readJsonFromUrl("https://api.linkpreview.net/?key=814cd6715a9ce8fcdc7edbc24721419c&q=" + url);
            java.net.URL imageUrl = new java.net.URL(json.getString("image"));
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
        return null;
    }
}