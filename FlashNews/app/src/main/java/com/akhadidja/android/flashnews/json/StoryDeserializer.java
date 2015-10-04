package com.akhadidja.android.flashnews.json;

import com.akhadidja.android.flashnews.pojos.Story;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class StoryDeserializer implements JsonDeserializer<Story> {
    @Override
    public Story deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        final JsonObject storyObject = json.getAsJsonObject();
        final String id = storyObject.get("id").getAsString();
        final String title = storyObject.getAsJsonObject(NprApiEndpoints.FIELD_TITLE)
                .get("$text").getAsString();
        final String teaser = storyObject.getAsJsonObject(NprApiEndpoints.FIELD_TEASER)
                .get("$text").getAsString();
        final String storyDate = storyObject.getAsJsonObject(NprApiEndpoints.FIELD_STORY_DATE)
                .get("$text").getAsString();

        final JsonArray linkArray = storyObject.getAsJsonArray("link");
        String shortLink = null;
        String htmlLink = null;
        for (int i = 0; i < linkArray.size(); i++) {
            JsonObject linkObject = linkArray.get(i).getAsJsonObject();
            String type = linkObject.get("type").getAsString();
            if(type.equals("short"))
                shortLink = linkObject.get("$text").getAsString();
            if(type.equals("html"))
                htmlLink = linkObject.get("$text").getAsString();
        }

        final JsonArray textArray = storyObject.get("textWithHtml").getAsJsonObject()
                                                .get("paragraph").getAsJsonArray();
        String text = "";
        for (int i = 0; i < textArray.size(); i++) {
            if(textArray.get(i).getAsJsonObject().has("$text"))
                text += textArray.get(i).getAsJsonObject().get("$text").getAsString();
            //else
                //text += "\n";
        }

        final Story story = new Story();
        story.setId(id);
        story.setShortLink(shortLink);
        story.setHtmlLink(htmlLink);
        story.setStoryDate(storyDate);
        story.setTeaser(teaser);
        story.setTitle(title);
        story.setTextWithHtml(text);

        return story;
    }
}
