package com.app.dbgoo.util;

import com.app.dbgoo.constant.AppConstant;
import com.google.common.io.Files;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.common.base.Charsets;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
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

    public static String getFileAsString(String jsonFilename) throws IOException {
        try{
            String path = AppConstant.homePath + File.separator + AppConstant.propertiesFolderName + File.separator + jsonFilename;
            File tempFile = new File( path);
            if(tempFile.exists()) {
               return Files.asCharSource(new File(path), Charsets.UTF_8).read();
            } else {
                File file = new File(path);
                file.getParentFile().mkdirs();
                if(file.createNewFile()) {
                    JSONObject emptyObject = new JSONObject();
                    emptyObject.put("connections", new JSONArray());
                    emptyObject.put("sql-history", new JSONArray());
                    FileWriter fileWriter = new FileWriter(path);
                    fileWriter.write(emptyObject.toString());
                    fileWriter.close();
                    return Files.asCharSource(file, Charsets.UTF_8).read();
                }
            }
        }catch (Exception e){
           e.printStackTrace();
        }
        return null;
    }

    public static JSONObject readJsonFromFile() throws IOException, ParseException {
        try {
            return new JSONObject(Objects.requireNonNull(getFileAsString(AppConstant.propertiesFileName)));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getApplicationData(String objectName) {
        JSONArray j = null;
        try {
            JSONObject obj = readJsonFromFile();
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
