package com.example.dbgoo.util;

import com.example.dbgoo.HelloApplication;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtil {
    private static final String[] KEYWORDS = new String[] {
            "select", "from", "insert", "into", "set", "values",
            "delete", "where", "group", "by", "update",
            "count", "sum", "order by"};

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "/\\*.*\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    public static String getFileAsString(String jsonfilename) throws IOException {
        URL url = Resources.getResource(jsonfilename);
        return Resources.toString(url, Charsets.UTF_8);
    }

    public static JSONObject readJsonFromFile(String fileName) throws IOException, ParseException {

        InputStream inputStreamObject = HelloApplication.class.getResourceAsStream(fileName);

        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());

            //returns the json object
            return jsonObject;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //if something went wrong, return null
        return null;
    }

    public static JSONArray getApplicationData(String objectName) {
        JSONArray j = null;
        try {
            JSONObject obj = readJsonFromFile("application.json");
            j =  obj.getJSONArray(objectName);
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return j;
    }

    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "plain" :
                                    matcher.group("BRACE") != null ? "object" :
                                            matcher.group("SEMICOLON") != null ? "plain" :
                                                    matcher.group("STRING") != null ? "value" :
                                                            matcher.group("COMMENT") != null ? "comment" :
                                                                    "plain";
            spansBuilder.add(Collections.singleton("plain"), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
