package com.devcti.frhec.seasmamon.dummy;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.devcti.frhec.seasmamon.ItemListActivity;
import com.devcti.frhec.seasmamon.SplashActivity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent extends AppCompatActivity{

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static final String PREFS_NAME = "Estados";

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        try {
            new DummyContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DummyContent() throws IOException {

        HashMap<String,String> mapa = ItemListActivity.obj;
        ObjectMapper mapper = new ObjectMapper();

        int i=1;

        for (Map.Entry<String, String> key : mapa.entrySet()) {
            if(key.getKey().toString() != "Key-Secret"){
                JsonNode actualObj = mapper.readTree(key.getValue());
                addItem(createDummyItem(actualObj.get("name").toString(),actualObj.get("secret").toString(),i++,new Date(Long.parseLong(actualObj.get("dia").toString())),actualObj.get("type").toString()));
            }
        }

    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(String nombre,String secret, int position, Date dia, String per) {
        return new DummyItem(String.valueOf(position), nombre, makeDetails(secret,position,dia,per));
    }

    private static String makeDetails(String secret, int position, Date dia, String per) {
        StringBuilder builder = new StringBuilder();
        builder.append("\nId: "+position+"\n\n");
        builder.append("Secret: "+secret.replace("\"","")+"\n\n");
        SimpleDateFormat dt1 = new SimpleDateFormat("dd/mm/yyyy");
        builder.append("Dia: "+dt1.format(dia)+"\n\n");
        //builder.append("Permiso: "+ per);
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
